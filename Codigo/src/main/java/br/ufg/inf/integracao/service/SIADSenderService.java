package br.ufg.inf.integracao.service;

import br.ufg.inf.integracao.main.SIADWatcher;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SIADSenderService extends Thread {
	private static SIADSenderService instance = new SIADSenderService();
	private static ExecutorService executorService = Executors.newFixedThreadPool(16);
	private boolean shouldStop = false;

	private Map<String, Boolean> workers = new HashMap<>();

	public static SIADSenderService getInstance() {
		return instance;
	}

	private void refreshWorkers() {
		Set<String> users = SIADRegistrarService.getInstance().getUsers();
		for (String user : users) {
			if (workers.containsKey(user) && workers.get(user)) {
				continue;
			}

			Thread watcherWorker = new SIADWatcher(user);
			executorService.execute(watcherWorker);
		}
	}

	public void markAsFinished(String user) {
		workers.put(user, false);
	}

	public void requestStop() {
		this.shouldStop = true;
	}

	@Override
	public void run() {
		try {
			while(true) {
				refreshWorkers();

				if(shouldStop) {
					executorService.shutdownNow();
					break;
				}

				Thread.sleep(10 * 1000);
			}
		} catch (InterruptedException e) {
			throw new RuntimeException(e.getMessage());
		}
	}
}
