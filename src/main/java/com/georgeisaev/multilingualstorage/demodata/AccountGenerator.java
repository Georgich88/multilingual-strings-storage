package com.georgeisaev.multilingualstorage.demodata;

import com.georgeisaev.multilingualstorage.domain.Account;
import com.georgeisaev.multilingualstorage.domain.MultilingualString;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class AccountGenerator {

	public static List<Account> generateDemoAccounts(final int size) {
		List<Account> accounts = new ArrayList<>(size);
		IntStream.range(0, size).forEach(i -> accounts.add(
				new Account(0, MultilingualString.of("Some name", "Какое-то имя", "어떤 이름"))));
		return accounts;
	}

	public static List<Account> generateDemoPersistedAccounts(final int size) {
		List<Account> accounts = new ArrayList<>(size);
		IntStream.range(0, size).forEach(i -> accounts.add(
				new Account(i + 1, MultilingualString.of("Some name", "Какое-то имя", "어떤 이름"))));
		return accounts;
	}

}
