package com.georgeisaev.multilingualstorage.dao;

import com.georgeisaev.multilingualstorage.domain.Account;

import java.util.List;

public interface Dao {

	void save(List<Account> accounts);

	List<Account> findAll();

	void deleteAll();

}
