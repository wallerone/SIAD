package br.ufg.inf.integracao.util;


import br.ufg.inf.integracao.domain.SIADMessage;
import br.ufg.inf.integracao.exception.InvalidPayloadException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class JSONToSIADMessageConverter {
	public static SIADMessage convertJSONToSIADMessage(String jsonString) throws InvalidPayloadException, JSONException {
		JSONObject jsonObject = new JSONObject(jsonString);

		boolean jsonHasAllRequiredFields = jsonObject.has("sender") && jsonObject.has("receivers") && jsonObject.has("content");
		if (!jsonHasAllRequiredFields) {
			throw new InvalidPayloadException("sender, receivers and content are required fields. Received '" + jsonString + "'");
		}

		String sender = jsonObject.getString("sender");
		List<String> receivers = new ArrayList<String>();

		JSONArray receiversJsonArray = jsonObject.getJSONArray("receivers");
		for (int idx = 0; idx < receiversJsonArray.length(); idx++) {
			receivers.add(receiversJsonArray.getString(idx));
		}

		JSONObject content = jsonObject.getJSONObject("content");

		return new SIADMessage(sender, receivers, content);
	}
}
