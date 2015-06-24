package br.ufg.inf.integracao.util;

import br.ufg.inf.integracao.exception.DuplicateUserException;
import br.ufg.inf.integracao.exception.InvalidUserException;
import br.ufg.inf.integracao.service.SIADRegistrarService;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.InvalidParameterException;

import static br.ufg.inf.integracao.util.SIADDefaults.JSON_KEY_ALIAS;
import static br.ufg.inf.integracao.util.SIADDefaults.JSON_KEY_ADDRESS;

public class UserUtils {
	public static void registerUserFromJSON(String jsonString) throws InvalidParameterException, JSONException, DuplicateUserException {
		JSONObject jsonObject = new JSONObject(jsonString);

		boolean jsonHasAllRequiredFields = jsonObject.has(JSON_KEY_ALIAS) && jsonObject.has(JSON_KEY_ADDRESS);
		if (!jsonHasAllRequiredFields) {
			throw new InvalidParameterException(JSON_KEY_ALIAS + " and " + JSON_KEY_ADDRESS + " are required fields. Received \"" + jsonString + "\"");
		}

		String alias = jsonObject.getString(JSON_KEY_ALIAS);
		String address = jsonObject.getString(JSON_KEY_ADDRESS);

		SIADRegistrarService.getInstance().registerUser(alias, address);
	}

	public static void unregisterUserFromJSON(String jsonString) throws InvalidParameterException, JSONException, InvalidUserException {
		JSONObject jsonObject = new JSONObject(jsonString);

		boolean jsonHasAllRequiredFields = jsonObject.has(JSON_KEY_ALIAS);
		if (!jsonHasAllRequiredFields) {
			throw new InvalidParameterException(JSON_KEY_ALIAS + " is a required field. Received \"" + jsonString + "\"");
		}

		String alias = jsonObject.getString(JSON_KEY_ALIAS);

		SIADRegistrarService.getInstance().unregisterUser(alias);
	}
}
