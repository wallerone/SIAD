package br.ufg.inf.integracao.service;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static br.ufg.inf.integracao.util.SIADDefaults.JSON_EXTENSION;
import static br.ufg.inf.integracao.util.SIADDefaults.SIAD_HOME;
import static br.ufg.inf.integracao.util.SIADDefaults.SIAD_FOLDER_NAME;
import static br.ufg.inf.integracao.util.SIADDefaults.SIAD_TIMESTAMP_FORMAT;
import static java.io.File.separator;

public class JSONFileService {

	private static JSONFileService instance = new JSONFileService();

	public static JSONFileService getInstance() {
		return instance;
	}

	public String getFolderPathForReceiver(String receiver) {
		return SIAD_HOME + separator
				+ SIAD_FOLDER_NAME + separator
				+ receiver;
	}

	public String getFolderPathForData() {
		return SIAD_HOME + separator + SIAD_FOLDER_NAME;
	}

	public void saveSingleMessageJSONObjectToFile(String receiver, JSONObject json) throws IOException {
		String timestamp = new SimpleDateFormat(SIAD_TIMESTAMP_FORMAT).format(new Date());

		String folderPath = getFolderPathForReceiver(receiver);
		String filePath = folderPath + separator + timestamp + JSON_EXTENSION;

		FileWriter fileWriter = null;
		File directory = null;
		try {
			directory = new File(folderPath);
			if (directory.isDirectory() || directory.mkdirs()) {
				fileWriter = new FileWriter(filePath);
				fileWriter.write(json.toString());
			} else {
				throw new IOException("Directory " + folderPath + " doesn't exist and can't be created");
			}
		} finally {
			if (fileWriter != null) {
				fileWriter.flush();
				fileWriter.close();
			}
		}
	}

	public void saveDataJSONObjectToFile(String filename, JSONObject json) throws IOException {
		String folderPath = getFolderPathForData();
		String filePath = folderPath + separator + filename + JSON_EXTENSION;

		FileWriter fileWriter = null;
		File directory = null;
		try {
			directory = new File(folderPath);
			if (directory.isDirectory() || directory.mkdirs()) {
				fileWriter = new FileWriter(filePath);
				fileWriter.write(json.toString());
			} else {
				throw new IOException("Directory " + folderPath + " doesn't exist and can't be created");
			}
		} finally {
			if (fileWriter != null) {
				fileWriter.flush();
				fileWriter.close();
			}
		}
	}

	public JSONObject readDataJSONObjectFromFile(String filename, boolean createFile) throws IOException {
		String folderPath = getFolderPathForData();
		String filePath = folderPath + separator + filename + JSON_EXTENSION;
		File file = new File(filePath);

		if(!file.exists()) {
			if (createFile) {
				JSONObject emptyJSONObject = new JSONObject();
				saveDataJSONObjectToFile(filename, emptyJSONObject);
				return emptyJSONObject;
			} else {
				throw new IOException("File at path " + filePath + " doesn't exist");
			}
		}

		List<String> jsonLines = Files.readAllLines(file.toPath(), Charset.defaultCharset());
		if(jsonLines.isEmpty()) {
			return null;
		}

		return new JSONObject(StringUtils.join(jsonLines, "\n"));
	}
}
