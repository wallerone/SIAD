package br.ufg.inf.integracao.main;

import br.ufg.inf.integracao.service.JSONFileService;
import br.ufg.inf.integracao.service.SIADRegistrarService;
import br.ufg.inf.integracao.service.SIADSenderService;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.json.JSONObject;

import java.io.File;
import java.io.FilenameFilter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.List;

import static br.ufg.inf.integracao.util.SIADDefaults.JSON_EXTENSION;

public class SIADWatcher extends Thread {

	private String receiver;

	public SIADWatcher(String receiver) {
		this.receiver = receiver;
	}

	@Override
	public void run() {
		File jsonDirectory = new File(JSONFileService.getInstance().getFolderPathForReceiver(receiver));

		System.out.println("Vou checar o dir " + jsonDirectory.getAbsolutePath());

		if(!jsonDirectory.exists() || !jsonDirectory.isDirectory()) {
			return;
		}

		try {
			String address = SIADRegistrarService.getInstance().getAddressForAlias(receiver);
			File [] jsonFiles = jsonDirectory.listFiles(new FilenameFilter() {
				@Override
				public boolean accept(File dir, String name) {
					return name.endsWith(JSON_EXTENSION);
				}
			});

			for (File jsonFile : jsonFiles) {
				List<String> jsonLines = Files.readAllLines(jsonFile.toPath(), Charset.defaultCharset());
				JSONObject jsonObject = new JSONObject(StringUtils.join(jsonLines, "\n"));
				sendJsonToAddress(jsonObject, address);
				if(!jsonFile.delete()) throw new RuntimeException("Couldn't delete file at " + jsonFile.getAbsolutePath());
			}

			SIADSenderService.getInstance().markAsFinished(receiver);
		} catch(Exception e) {
			SIADSenderService.getInstance().markAsFinished(receiver);
			throw new RuntimeException(e.getMessage());
		}
	}

	private void sendJsonToAddress(JSONObject jsonObject, String address) throws Exception {
		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpPost httpPost = new HttpPost(address);
		HttpEntity requestBody = new StringEntity(jsonObject.toString(), ContentType.APPLICATION_JSON);
		httpPost.setEntity(requestBody);

		CloseableHttpResponse httpResponse = httpClient.execute(httpPost);
		if(httpResponse.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
			throw new Exception();
		}
	}
}
