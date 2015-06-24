package br.ufg.inf.integracao.main;

import br.ufg.inf.integracao.handler.SIADMessageReceiverHandler;
import br.ufg.inf.integracao.handler.SIADRegisterUserHandler;
import br.ufg.inf.integracao.handler.SIADUnregisterUserHandler;
import br.ufg.inf.integracao.service.SIADSenderService;
import org.apache.http.ExceptionLogger;
import org.apache.http.impl.nio.bootstrap.ServerBootstrap;
import org.apache.http.impl.nio.bootstrap.HttpServer;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import static br.ufg.inf.integracao.util.SIADDefaults.*;

public class SIADServer extends Thread {

	private static final Logger logger = Logger.getLogger(SIADServer.class.getName());
	private static HttpServer server;
	private static final SIADServer instance = new SIADServer();

	private SIADServer() {}

	public static void main(String[] args) {

		logger.info("Preparing SIADServer...");
		SIADSenderService.getInstance().start();

		server = ServerBootstrap.bootstrap()
				.setListenerPort(SIAD_SERVER_PORT)
				.setServerInfo(SIAD_SERVER_STRING)
				.setExceptionLogger(ExceptionLogger.STD_ERR)
				.registerHandler(NEW_MESSAGE_ENDPOINT, new SIADMessageReceiverHandler())
				.registerHandler(NEW_USER_ENDPOINT, new SIADRegisterUserHandler())
				.registerHandler(DELETE_USER_ENDPOINT, new SIADUnregisterUserHandler())
				.create();

		instance.start();

		logger.info("Endpoint for new messages: " + NEW_MESSAGE_ENDPOINT);
		logger.info("Endpoint for new users: " + NEW_USER_ENDPOINT);
		logger.info("Endpoint for deleting users: " + DELETE_USER_ENDPOINT);

		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				logger.info("Stopping SIADServer...");
				SIADSenderService.getInstance().requestStop();
				server.shutdown(5, TimeUnit.SECONDS);
			}
		});
	}

	@Override
	public void run() {
		try {
			server.start();
			server.awaitTermination(Long.MAX_VALUE, TimeUnit.DAYS);
		} catch (InterruptedException | IOException e) {
			e.printStackTrace();
		}
	}
}
