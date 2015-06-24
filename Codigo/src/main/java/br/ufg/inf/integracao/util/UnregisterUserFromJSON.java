package br.ufg.inf.integracao.util;

import br.ufg.inf.integracao.exception.InvalidUserException;
import br.ufg.inf.integracao.service.SIADRegistrarService;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.InvalidParameterException;

import static br.ufg.inf.integracao.util.SIADDefaults.JSON_KEY_ALIAS;

public class UnregisterUserFromJSON {
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
