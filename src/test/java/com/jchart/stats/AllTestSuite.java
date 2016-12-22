package com.jchart.stats;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

	@RunWith(Suite.class)
	@Suite.SuiteClasses({
		TestCorrelation.class,
		TestRegression.class,
		TestStd.class,
		TestZscore.class
	})

	public class AllTestSuite {}