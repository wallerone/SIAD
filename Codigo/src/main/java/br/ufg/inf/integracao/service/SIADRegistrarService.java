package br.ufg.inf.integracao.service;

import br.ufg.inf.integracao.exception.DuplicateUserException;
import br.ufg.inf.integracao.exception.InvalidUserException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static br.ufg.inf.integracao.util.SIADDefaults.JSON_KEY_ALIAS;

public class SIADRegistrarService {

	private static SIADRegistrarService service = new SIADRegistrarService();
	private Map<String, String> users = new HashMap<>();

	public SIADRegistrarService() {
		readUsersFile();
	}

	public static SIADRegistrarService getInstance() {
		return service;
	}

	public Set<String> getUsers() {
		return users.keySet();
	}

	public void registerUser(String alias, String address) throws DuplicateUserException {
		if (users.containsKey(alias)) {
			throw new DuplicateUserException("User with " + JSON_KEY_ALIAS + " \"" + alias + "\" already exists.");
		}

		users.put(alias, address);
		updateUsersFile();
	}

	public void unregisterUser(String alias) throws InvalidUserException {
		if (!users.containsKey(alias)) {
			throw new InvalidUserException("User with " + JSON_KEY_ALIAS + " \"" + alias + "\" does not exist.");
		}

		users.remove(alias);
		updateUsersFile();
	}

	public String getAddressForAlias(String alias) throws InvalidUserException {
		if (!users.containsKey(alias)) {
			throw new InvalidUserException("User with " + JSON_KEY_ALIAS + " \"" + alias + "\" does not exist.");
		}

		return users.get(alias);
	}

	private void updateUsersFile() {
		try {
			JSONObject json = new JSONObject(users);
			JSONFileService.getInstance().saveDataJSONObjectToFile("users", json);
		} catch (IOException e) {
			throw new RuntimeException(e.getMessage());
		}
	}

	private void readUsersFile() {
		try {
			JSONObject jsonObject = JSONFileService.getInstance().readDataJSONObjectFromFile("users", true);
			for (Object key : jsonObject.keySet()) {
				String alias = key.toString();
				String address = jsonObject.getString(alias);

				users.put(alias, address);
			}
		} catch (IOException e) {
			throw new RuntimeException(e.getMessage());
		}
	}
}
