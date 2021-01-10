package com.georgeisaev.multilingualstorage.dao.sameTable;

import com.georgeisaev.multilingualstorage.MultilingualStorageApplication;
import com.georgeisaev.multilingualstorage.domain.Account;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.georgeisaev.multilingualstorage.demodata.AccountGenerator.generateDemoAccounts;
import static com.georgeisaev.multilingualstorage.demodata.AccountGenerator.generateDemoPersistedAccounts;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@SpringBootTest
class SameTableDaoTest {

	public static final int ACCOUNT_TEST_DATA_SIZE = 5;
	private final SameTableDao dao;

	private final List<Account> accountsToInsert;
	private final List<Account> accountsToUpdate;

	@Autowired
	public SameTableDaoTest(SameTableDao dao) {
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