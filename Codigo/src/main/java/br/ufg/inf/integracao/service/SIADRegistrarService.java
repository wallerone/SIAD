package br.ufg.inf.integracao.service;

import br.ufg.inf.integracao.exception.DuplicateUserException;
import br.ufg.inf.integracao.exception.InvalidUserException;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static br.ufg.inf.integracao.util.SIADDefaults.JSON_KEY_ALIAS;

public class SIADRegistrarService {

	private static SIADRegistrarService service = new SIADRegistrarService();
	private Map<String, String> users = new HashMap<>();

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
	}

	public void unregisterUser(String alias) throws InvalidUserException {
		if (!users.containsKey(alias)) {
			throw new InvalidUserException("User with " + JSON_KEY_ALIAS + " \"" + alias + "\" does not exist.");
		}

		users.remove(alias);
	}

	public String getAddressForAlias(String alias) throws InvalidUserException {
		if (!users.containsKey(alias)) {
			throw new InvalidUserException("User with " + JSON_KEY_ALIAS + " \"" + alias + "\" does not exist.");
		}

		return users.get(alias);
	}
}
