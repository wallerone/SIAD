package br.ufg.inf.integracao.util;

import br.ufg.inf.integracao.domain.SIADMessage;
import br.ufg.inf.integracao.exception.InvalidPayloadException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static br.ufg.inf.integracao.util.SIADDefaults.JSON_KEY_CONTENT;
import static br.ufg.inf.integracao.util.SIADDefaults.JSON_KEY_RECEIVERS;
import static br.ufg.inf.integracao.util.SIADDefaults.JSON_KEY_SENDER;

public class SIADMessageUtils {
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

	public static Map<String, JSONObject> convertSIADMessageToSingleRecipientJSON(SIADMessage message) {
		Map<String, JSONObject> jsonPerRecipient = new HashMap<>();

		String sender = message.getSender();
		JSONObject content = message.getContent();
		for (String receiver : message.getReceivers()) {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put(JSON_KEY_SENDER, sender);

			JSONArray recepientArray = new JSONArray();
			recepientArray.put(receiver);
			jsonObject.put(JSON_KEY_RECEIVERS, recepientArray);

			jsonObject.put(JSON_KEY_CONTENT, content);

			jsonPerRecipient.put(receiver, jsonObject);
		}

		return jsonPerRecipient;
	}
}
