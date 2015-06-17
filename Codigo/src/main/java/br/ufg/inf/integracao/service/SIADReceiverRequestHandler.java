package br.ufg.inf.integracao.service;

import br.ufg.inf.integracao.domain.SIADMessage;
import org.apache.http.*;
import org.apache.http.entity.ContentType;
import org.apache.http.message.BasicHttpEntityEnclosingRequest;
import org.apache.http.nio.entity.NStringEntity;
import org.apache.http.nio.protocol.*;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class SIADReceiverRequestHandler implements HttpAsyncRequestHandler<HttpRequest> {
    public HttpAsyncRequestConsumer<HttpRequest> processRequest(final HttpRequest request, final HttpContext context) {
        return new BasicAsyncRequestConsumer();
    }

    public void handle(final HttpRequest request, final HttpAsyncExchange httpexchange, final HttpContext context) throws HttpException, IOException {
        HttpResponse response = httpexchange.getResponse();

        String method = request.getRequestLine().getMethod().toUpperCase(Locale.ENGLISH);
        if (!method.equals("POST")) {
            throw new MethodNotSupportedException(method + " method not supported");
        }

        String jsonString = EntityUtils.toString(((BasicHttpEntityEnclosingRequest) request).getEntity());
        JSONObject jsonObject = new JSONObject(jsonString);

        String sender = jsonObject.getString("sender");
        List<String> receivers = new ArrayList<String>();

        JSONArray receiversJsonArray = jsonObject.getJSONArray("receivers");
        for (int idx = 0; idx < receiversJsonArray.length(); idx++) {
            receivers.add(receiversJsonArray.getString(idx));
        }

        JSONObject content = jsonObject.getJSONObject("content");

        SIADMessage message = new SIADMessage(sender, receivers, content);

        response.setStatusCode(HttpStatus.SC_OK);
        NStringEntity entity = new NStringEntity(message.toString(), ContentType.create("text/html", "UTF-8"));
        response.setEntity(entity);

        httpexchange.submitResponse(new BasicAsyncResponseProducer(response));
    }
}