package br.ufg.inf.integracao.service;

import org.apache.http.ExceptionLogger;
import org.apache.http.impl.nio.bootstrap.ServerBootstrap;
import org.apache.http.impl.nio.bootstrap.HttpServer;

import java.util.concurrent.TimeUnit;

public class SIADReceiver {

    public static void main(String[] args) throws Exception {
        int port = 8080;
        final HttpServer server = ServerBootstrap.bootstrap()
                .setListenerPort(port)
                .setServerInfo("SIADReceiver/0.1")
                .setExceptionLogger(ExceptionLogger.STD_ERR)
                .registerHandler("*", new SIADReceiverRequestHandler())
                .create();

        server.start();
        server.awaitTermination(Long.MAX_VALUE, TimeUnit.DAYS);

        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                server.shutdown(5, TimeUnit.SECONDS);
            }
        });
    }
}
