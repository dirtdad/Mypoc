package com.ordiway;

import java.io.IOException;
import java.util.List;

import org.apache.log4j.Logger;
import org.junit.Test;

import com.ordiway.DataSource;
import com.ordiway.ReadDataSources;

public class ReadDataSourcesTest {
	private final static Logger logger = Logger.getLogger(ReadDataSourcesTest.class.getName());

	@Test
	public final void test() throws IOException {
		ReadDataSources rds = new ReadDataSources();
		List<DataSource> x = rds.run();
		logger.info("found this number of data sources: " + x.size());
		if (x.size() == 0) {
			logger.info("no datasource file");
		} else {
			x.forEach (y -> logger.info("Datasource: " + y.getUrl() + " " + y.getUser()));
		}
	}
}