package br.ufg.inf.integracao.service;

import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

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

	public void saveJSONObjectToFile(String destination, JSONObject json) throws IOException {
		String timestamp = new SimpleDateFormat(SIAD_TIMESTAMP_FORMAT).format(new Date());

		String folderPath = SIAD_HOME + separator
				+ SIAD_FOLDER_NAME + separator
				+ destination;

		String filePath = folderPath + separator + timestamp + JSON_EXTENSION;

		FileWriter fileWriter = null;
		File directory = null;
		try {
			directory = new File(folderPath);
			if(directory.isDirectory() || directory.mkdirs()) {
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
}
