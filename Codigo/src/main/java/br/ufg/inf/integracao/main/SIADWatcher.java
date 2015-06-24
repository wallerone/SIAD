package br.ufg.inf.integracao.main;

import br.ufg.inf.integracao.exception.InvalidUserException;
import br.ufg.inf.integracao.service.JSONFileService;
import br.ufg.inf.integracao.service.SIADRegistrarService;
import br.ufg.inf.integracao.service.SIADSenderService;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.json.JSONObject;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static br.ufg.inf.integracao.util.SIADDefaults.JSON_EXTENSION;

public class SIADWatcher extends Thread {

	private static final Logger logger = Logger.getLogger(SIADWatcher.class.getName());

	private String receiver;

	public SIADWatcher(String receiver) {
		logger.setLevel(Level.ALL);
		this.receiver = receiver;
	}

	@Override
	public void run() {
		File jsonDirectory = new File(JSONFileService.getInstance().getFolderPathForReceiver(receiver));

		if (!jsonDirectory.exists() || !jsonDirectory.isDirectory()) {
			logger.info("Creating directory for user with alias " + receiver);
			boolean createdDirs = jsonDirectory.mkdirs();

			if(createdDirs) logger.info("Directory was created successfully.");
			else logger.warning("Could not create directory at " + jsonDirectory.getAbsolutePath());

			return;
		}

		try {
			String address = SIADRegistrarService.getInstance().getAddressForAlias(receiver);
			File[] jsonFiles = jsonDirectory.listFiles(new FilenameFilter() {
				@Override
				public boolean accept(File dir, String name) {
					return name.endsWith(JSON_EXTENSION);
				}
			});

			for (File jsonFile : jsonFiles) {
				logger.info("Got message at " + jsonFile.getAbsolutePath());
				List<String> jsonLines = Files.readAllLines(jsonFile.toPath(), Charset.defaultCharset());
				JSONObject jsonObject = new JSONObject(StringUtils.join(jsonLines, "\n"));
				sendJsonToAddress(jsonObject, address);
				if (!jsonFile.delete()) throw new RuntimeException("Couldn't delete file at " + jsonFile.getAbsolutePath());
			}

		} catch (HttpResponseException e) {
			logger.warning("Could not send message to " + receiver + ". Response code: " + e.getStatusCode());
		} catch (InvalidUserException e) {
			logger.severe("User " + receiver + " is not registered. Can't send messages to user until registration.");
		} catch (ClientProtocolException e) {
			logger.severe("Could not connect to user " + receiver + ". Details: " + e.getMessage());
		} catch (IOException e) {
			logger.severe("Could not retrive message from storage or user connection was aborted. Details: " + e.getMessage());
		}  finally {
			reportFinishedJob();
		}
	}

	private void sendJsonToAddress(JSONObject jsonObject, String address) throws IOException {
		logger.info("Trying to send message to " + address);

		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpPost httpPost = new HttpPost(address);
		HttpEntity requestBody = new StringEntity(jsonObject.toString(), ContentType.APPLICATION_JSON);
		httpPost.setEntity(requestBody);

		CloseableHttpResponse httpResponse = httpClient.execute(httpPost);
		if (httpResponse.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
			throw new HttpResponseException(httpResponse.getStatusLine().getStatusCode(), "A message could not be sent.");
		}
	}

	private void reportFinishedJob() {
		logger.info("Reporting finished job.");
		SIADSenderService.getInstance().markAsFinished(receiver);
	}
}
