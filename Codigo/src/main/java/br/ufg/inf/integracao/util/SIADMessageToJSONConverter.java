package br.ufg.inf.integracao.util;

import br.ufg.inf.integracao.domain.SIADMessage;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static br.ufg.inf.integracao.util.SIADDefaults.JSON_KEY_SENDER;
import static br.ufg.inf.integracao.util.SIADDefaults.JSON_KEY_RECEIVERS;
import static br.ufg.inf.integracao.util.SIADDefaults.JSON_KEY_CONTENT;

public class SIADMessageToJSONConverter {
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
