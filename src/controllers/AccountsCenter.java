package controllers;

import java.util.HashMap;

import entities.PlayerProfile;

public class AccountsCenter {
	private static HashMap<String, PlayerProfile> accounts;

	public static PlayerProfile getAccount(String accountEmail) {
		return getAccounts(accountEmail).get(accountEmail);
	}

	public static HashMap<String, PlayerProfile> getAllAccounts() {
		return getAccounts("");
	}

	public static void addAccount(PlayerProfile profile) {
		if (accounts == null) {
			accounts = new HashMap<String, PlayerProfile>();
		}
		accounts.put(profile.getUsername(), profile);
	}

	private static HashMap<String, PlayerProfile> getAccounts(String accountEmail) {
		if (accounts == null) {
			accounts = new HashMap<String, PlayerProfile>();
		}

		if (accountEmail.length() > 0) {
			HashMap<String, PlayerProfile> accs = new HashMap<String, PlayerProfile>();
			PlayerProfile profile = accounts.get(accountEmail);
			if (profile == null) {
				profile = new PlayerProfile("", "", "");
			}
			accs.put(accountEmail, profile);
			return accs;
		} else {
			return accounts;
		}
	}
}
