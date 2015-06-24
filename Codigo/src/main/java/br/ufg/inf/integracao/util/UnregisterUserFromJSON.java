package br.ufg.inf.integracao.util;

import br.ufg.inf.integracao.exception.InvalidUserException;
import br.ufg.inf.integracao.service.SIADRegistrarService;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.InvalidParameterException;

public class UnregisterUserFromJSON {
	public static void unregisterUserFromJSON(String jsonString) throws InvalidParameterException, JSONException, InvalidUserException {
		JSONObject jsonObject = new JSONObject(jsonString);

		boolean jsonHasAllRequiredFields = jsonObject.has("alias");
		if (!jsonHasAllRequiredFields) {
			throw new InvalidParameterException("alias is required field. Received '" + jsonString + "'");
		}

		String alias = jsonObject.getString("alias");

		SIADRegistrarService.getInstance().unregisterUser(alias);
	}
}
