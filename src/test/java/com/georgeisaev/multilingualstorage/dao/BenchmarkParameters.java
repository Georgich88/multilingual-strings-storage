package com.georgeisaev.multilingualstorage.dao;

public class BenchmarkParameters {

	private BenchmarkParameters() {

	}

	public static final int ACCOUNT_TEST_DATA_SIZE = 100_000;
	public static final int BENCHMARK_THREADS = 1;
	public static final boolean BENCHMARK_DO_GC = true;
	public static final boolean BENCHMARK_FAIL_ON_ERROR = true;
	public static final int BENCHMARK_FORKS = 0;
	public static final int BENCHMARK_MEASUREMENT_ITERATIONS = 5;
	public static final int BENCHMARK_WARMUP_ITERATIONS = 1;

}
