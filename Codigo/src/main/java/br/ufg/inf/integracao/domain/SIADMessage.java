package br.ufg.inf.integracao.domain;

import org.json.JSONObject;

import java.util.List;

import static br.ufg.inf.integracao.util.SIADDefaults.JSON_KEY_SENDER;
import static br.ufg.inf.integracao.util.SIADDefaults.JSON_KEY_RECEIVERS;
import static br.ufg.inf.integracao.util.SIADDefaults.JSON_KEY_CONTENT;

public class SIADMessage {
	private final String sender;
	private final List<String> receivers;
	private final JSONObject content;

	public SIADMessage(String sender, List<String> receivers, JSONObject content) {
		this.sender = sender;
		this.receivers = receivers;
		this.content = content;
	}

	public String getSender() {
		return sender;
	}

	public List<String> getReceivers() {
		return receivers;
	}

	public JSONObject getContent() {
		return content;
	}

	@Override
	public String toString() {
		String message = "{\"" + JSON_KEY_SENDER + "\":\"" + sender + "\",\"" + JSON_KEY_RECEIVERS + "\":[";
		for (int i = 0, receiversSize = receivers.size(); i < receiversSize; i++) {
			String receiver = receivers.get(i);
			message += "\"" + receiver + "\"";

			if (i < receiversSize - 1) message += ",";
		}

		message += "],\"" + JSON_KEY_CONTENT + "\":" + content.toString() + "}";
		return message;
	}
}
