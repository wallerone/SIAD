package br.ufg.inf.integracao.service;

import br.ufg.inf.integracao.main.SIADWatcher;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SIADSenderService extends Thread {
	private static final Logger logger = Logger.getLogger(SIADSenderService.class.getName());
	private static SIADSenderService instance = new SIADSenderService();
	private static ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
	private boolean shouldStop = false;

	private Map<String, Boolean> workers = new HashMap<>();

	private SIADSenderService() {
		logger.setLevel(Level.ALL);
		logger.info("SIADSenderService is up and running!");
	}

	public static SIADSenderService getInstance() {
		return instance;
	}

	private void refreshWorkers() {
		logger.info("Refreshing workers...");
		Set<String> users = SIADRegistrarService.getInstance().getUsers();
		for (String user : users) {
			if (workers.containsKey(user) && workers.get(user)) {
				logger.info("Worker for user " + user + " is still alive.");
				continue;
			}

			logger.info("Worker for user " + user + " was created.");
			Thread watcherWorker = new SIADWatcher(user);
			executorService.execute(watcherWorker);
		}
	}

	public void markAsFinished(String user) {
		logger.info("Worker for user " + user + " has stopped.");
		workers.put(user, false);
	}

	public void requestStop() {
		logger.info("Stop requested.");
		this.shouldStop = true;
	}

	@Override
	public void run() {
		try {
			while (true) {
				refreshWorkers();

				if (shouldStop) {
					logger.info("Stop was requested, shutting the workers down.");
					executorService.shutdownNow();
					break;
				}

				logger.info("Workers are up and running. Sleeping...");
				Thread.sleep(10 * 1000);
			}
		} catch (InterruptedException e) {
			throw new RuntimeException(e.getMessage());
		}
	}
}
