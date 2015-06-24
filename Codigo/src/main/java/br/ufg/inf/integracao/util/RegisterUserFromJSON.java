package br.ufg.inf.integracao.util;

import br.ufg.inf.integracao.exception.DuplicateUserException;
import br.ufg.inf.integracao.service.SIADRegistrarService;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.InvalidParameterException;

public class RegisterUserFromJSON {
	public static void registerUserFromJSON(String jsonString) throws InvalidParameterException, JSONException, DuplicateUserException {
		JSONObject jsonObject = new JSONObject(jsonString);

		boolean jsonHasAllRequiredFields = jsonObject.has("alias") && jsonObject.has("address");
		if (!jsonHasAllRequiredFields) {
			throw new InvalidParameterException("alias and address are required fields. Received '" + jsonString + "'");
		}

		String alias = jsonObject.getString("alias");
		String address = jsonObject.getString("address");

		SIADRegistrarService.getInstance().registerUser(alias, address);
	}
}
