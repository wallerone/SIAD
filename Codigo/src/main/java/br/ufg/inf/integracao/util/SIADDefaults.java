package br.ufg.inf.integracao.util;

public class SIADDefaults {
	public static final String JSON_KEY_SENDER = "sender";
	public static final String JSON_KEY_RECEIVERS = "receivers";
	public static final String JSON_KEY_CONTENT = "content";
	public static final String JSON_KEY_ALIAS = "alias";
	public static final String JSON_KEY_ADDRESS = "address";
	public static final String JSON_EXTENSION = ".json";

	public static final String SIAD_HOME = System.getProperty("user.dir");
	public static final String SIAD_FOLDER_NAME = "siad";
	public static final String SIAD_TIMESTAMP_FORMAT = "yyyyMMddHHmmssSSS";

	public static final String NEW_MESSAGE_ENDPOINT = "/sendMessage";
	public static final String NEW_USER_ENDPOINT = "/registerUser";
	public static final String DELETE_USER_ENDPOINT = "/unregisterUser";

	public static final String SIAD_SERVER_STRING = "SIADServer/0.5";
}
