package br.ufg.inf.integracao.domain;

import org.json.JSONObject;

import java.util.List;

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
        return "sender: " + sender +
                "\nreceivers: " + receivers +
                "\ncontent: " + content;
    }
}
