package com.georgeisaev.multilingualstorage.dao.extensionTable;

import com.georgeisaev.multilingualstorage.MultilingualStorageApplication;
import com.georgeisaev.multilingualstorage.dao.sameTable.SameTableDao;
import com.georgeisaev.multilingualstorage.domain.Account;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
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

@Slf4j
@SpringBootTest
@State(Scope.Benchmark)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class SameTableDaoBenchmarkTest {

	public static final int ACCOUNT_TEST_DATA_SIZE = 1_000_000;
	public static final int BENCHMARK_THREADS = 1;
	public static final boolean BENCHMARK_DO_GC = true;
	public static final boolean BENCHMARK_FAIL_ON_ERROR = true;
	public static final int BENCHMARK_FORKS = 0;
	public static final int BENCHMARK_MEASUREMENT_ITERATIONS = 5;
	public static final int BENCHMARK_WARMUP_ITERATIONS = 1;
	@Autowired
	private SameTableDao dao;
	private final List<Account> accountsToInsert;
	private List<Account> accountsToUpdate;

	public SameTableDaoBenchmarkTest() {
		this.accountsToInsert = generateDemoAccounts(ACCOUNT_TEST_DATA_SIZE);
	}

	@Setup
	public void setup() {
		ConfigurableApplicationContext context = new SpringApplication(MultilingualStorageApplication.class).run();
		this.dao = context.getBean(SameTableDao.class);
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
	public void runBenchmarks() throws Exception {
		Options opts = new OptionsBuilder()
				.include("\\." + this.getClass().getSimpleName() + "\\.")
				.warmupIterations(BENCHMARK_WARMUP_ITERATIONS)
				.measurementIterations(BENCHMARK_MEASUREMENT_ITERATIONS)
				.forks(BENCHMARK_FORKS)
				.threads(BENCHMARK_THREADS)
				.shouldDoGC(BENCHMARK_DO_GC)
				.shouldFailOnError(BENCHMARK_FAIL_ON_ERROR)
				.jvmArgs("-server")
				.build();

		new Runner(opts).run();
	}

	@Benchmark
	public void benchmark01Create() {
		dao.save(accountsToInsert);
	}

	@Benchmark
	public void benchmark02Retrieve() {
		accountsToUpdate = dao.findAll();
	}

	@Benchmark
	public void benchmark03Update() {
		accountsToUpdate = dao.findAll();
		dao.save(accountsToUpdate);
	}

	@Benchmark
	public void benchmark04Delete() {
		dao.deleteAll();
	}

}