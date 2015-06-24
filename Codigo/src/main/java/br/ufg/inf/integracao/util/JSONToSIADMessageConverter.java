package br.ufg.inf.integracao.util;


import br.ufg.inf.integracao.domain.SIADMessage;
import br.ufg.inf.integracao.exception.InvalidPayloadException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static br.ufg.inf.integracao.util.SIADDefaults.JSON_KEY_SENDER;
import static br.ufg.inf.integracao.util.SIADDefaults.JSON_KEY_RECEIVERS;
import static br.ufg.inf.integracao.util.SIADDefaults.JSON_KEY_CONTENT;

public class JSONToSIADMessageConverter {
	public static SIADMessage convertJSONToSIADMessage(String jsonString) throws InvalidPayloadException, JSONException {
		JSONObject jsonObject = new JSONObject(jsonString);

		boolean jsonHasAllRequiredFields = jsonObject.has(JSON_KEY_SENDER) && jsonObject.has("receivers") && jsonObject.has("content");
		if (!jsonHasAllRequiredFields) {
			throw new InvalidPayloadException(JSON_KEY_SENDER + ", " + JSON_KEY_RECEIVERS + " and " + JSON_KEY_CONTENT +
					" are required fields. Received \"" + jsonString + "\"");
		}

		String sender = jsonObject.getString(JSON_KEY_SENDER);
		List<String> receivers = new ArrayList<>();

		JSONArray receiversJsonArray = jsonObject.getJSONArray(JSON_KEY_RECEIVERS);
		for (int idx = 0; idx < receiversJsonArray.length(); idx++) {
			receivers.add(receiversJsonArray.getString(idx));
		}

		JSONObject content = jsonObject.getJSONObject(JSON_KEY_CONTENT);

		return new SIADMessage(sender, receivers, content);
	}
}
