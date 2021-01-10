package com.georgeisaev.multilingualstorage.dao.extensionTable;

import com.georgeisaev.multilingualstorage.MultilingualStorageApplication;
import com.georgeisaev.multilingualstorage.dao.sameTable.SameTableDao;
import com.georgeisaev.multilingualstorage.domain.Account;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.openjdk.jmh.annotations.Setup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.List;

import static com.georgeisaev.multilingualstorage.demodata.AccountGenerator.generateDemoAccounts;
import static com.georgeisaev.multilingualstorage.demodata.AccountGenerator.generateDemoPersistedAccounts;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@SpringBootTest
class ExtensionTableDaoTest {

	public static final int ACCOUNT_TEST_DATA_SIZE = 5;
	private final ExtensionTableDao dao;

	private final List<Account> accountsToInsert;
	private final List<Account> accountsToUpdate;

	@Autowired
	public ExtensionTableDaoTest(ExtensionTableDao dao) {
		this.dao = dao;
		this.accountsToInsert = generateDemoAccounts(ACCOUNT_TEST_DATA_SIZE);
		this.accountsToUpdate = generateDemoPersistedAccounts(ACCOUNT_TEST_DATA_SIZE);
	}

	@BeforeEach
	void setUp() {
		dao.deleteAll();
	}

	@AfterEach
	void tearDown() {
		dao.deleteAll();
	}


	@Test
	@Order(1)
	void create() {
		assertDoesNotThrow(() -> dao.save(accountsToInsert));
	}

	@Test
	@Order(2)
	void retrieve() {
		assertDoesNotThrow(dao::findAll);
	}

	@Test
	@Order(3)
	void update() {
		assertDoesNotThrow(() -> dao.save(accountsToInsert));
		assertDoesNotThrow(() -> dao.save(accountsToUpdate));
	}

	@Test
	@Order(4)
	void delete() {
		assertDoesNotThrow(() -> dao.save(accountsToInsert));
		assertDoesNotThrow(dao::deleteAll);
	}

}