package com.ordiway;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.junit.Test;

import com.ordiway.ReadCurrentDatasource;
import com.ordiway.ReadDataSources;

public class ReadCurrentDatasourceTest {
	private final static Logger logger = Logger.getLogger(ReadDataSources.class.getName());

	@Test
	public final void test() {
		ReadCurrentDatasource rcds = new ReadCurrentDatasource();
		try {
			rcds.run();
		} catch (IOException e) {
			logger.info("io error: " + e);
		}
	}
}
