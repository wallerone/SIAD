package br.ufg.inf.integracao.service;

import br.ufg.inf.integracao.domain.SIADMessage;
import br.ufg.inf.integracao.util.JSONToSIADMessageConverter;
import org.apache.http.*;
import org.apache.http.entity.ContentType;
import org.apache.http.message.BasicHttpEntityEnclosingRequest;
import org.apache.http.nio.entity.NStringEntity;
import org.apache.http.nio.protocol.*;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;

import java.io.IOException;
import java.security.InvalidParameterException;
import java.util.Locale;

public class SIADReceiverRequestHandler implements HttpAsyncRequestHandler<HttpRequest> {
    public HttpAsyncRequestConsumer<HttpRequest> processRequest(final HttpRequest request, final HttpContext context) {
        return new BasicAsyncRequestConsumer();
    }

    public void handle(final HttpRequest request, final HttpAsyncExchange httpexchange, final HttpContext context) throws HttpException, IOException, InvalidParameterException, JSONException {
        HttpResponse response = httpexchange.getResponse();

        String method = request.getRequestLine().getMethod().toUpperCase(Locale.ENGLISH);
        if (!method.equals("POST")) {
            throw new MethodNotSupportedException(method + " method not supported");
        }

        String jsonString = EntityUtils.toString(((BasicHttpEntityEnclosingRequest) request).getEntity());
        SIADMessage message = JSONToSIADMessageConverter.convertJSONToSIADMessage(jsonString);

        response.setStatusCode(HttpStatus.SC_OK);
        NStringEntity entity = new NStringEntity(message.toString(), ContentType.create("text/html", "UTF-8"));
        response.setEntity(entity);

        httpexchange.submitResponse(new BasicAsyncResponseProducer(response));
    }
}