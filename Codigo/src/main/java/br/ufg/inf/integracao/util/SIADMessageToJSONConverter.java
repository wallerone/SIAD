package br.ufg.inf.integracao.util;

import br.ufg.inf.integracao.domain.SIADMessage;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SIADMessageToJSONConverter {
	public static Map<String, JSONObject> convertSIADMessageToSingleRecipientJSON(SIADMessage message) {
		Map<String, JSONObject> jsonPerRecipient = new HashMap<String, JSONObject>();

		String sender = message.getSender();
		JSONObject content = message.getContent();
		for (String receiver : message.getReceivers()) {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("sender", sender);

			JSONArray recepientArray = new JSONArray();
			recepientArray.put(receiver);
			jsonObject.put("receivers", recepientArray);

			jsonObject.put("content", content);

			jsonPerRecipient.put(receiver, jsonObject);
		}

		return jsonPerRecipient;
	}
}
