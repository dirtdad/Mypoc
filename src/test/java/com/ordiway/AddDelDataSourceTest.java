package com.ordiway;

import java.io.IOException;
import java.util.List;

import org.apache.log4j.Logger;
import org.junit.Test;

import com.ordiway.AddDataSource;
import com.ordiway.DataSource;
import com.ordiway.DeleteDataSource;
import com.ordiway.ReadDataSources;

public class AddDelDataSourceTest {
	private final static Logger logger = Logger.getLogger(AddDelDataSourceTest.class.getName());

	@Test
	public final void test() throws IOException {
		DataSource ds1 = new DataSource ("abc","def");
		DataSource ds2 = new DataSource ("123","456");
		DataSource ds3 = new DataSource("uvw", "xyz");
		AddDataSource addDS = new AddDataSource();

		logger.info("Result of add data source1: " + addDS.run(ds1));
		ReadDataSources rds = new ReadDataSources();
		List<DataSource> listDS = rds.run();
		listDS.forEach ( y -> logger.info("datasources after the add abc: " + y.getUrl()));
			
		logger.info("Result of add data source2: " + addDS.run(ds2));
		rds = new ReadDataSources();
		listDS = rds.run();
		listDS.forEach ( y -> logger.info("datasources after the add 123: " + y.getUrl()));
		
		logger.info("Result of adding duplicate data source2: " + addDS.run(ds2));		
		rds = new ReadDataSources();
		listDS = rds.run();
		listDS.forEach ( y -> logger.info("datasources after the duplicate add 123: " + y.getUrl()));
		
		logger.info("Result of add data source3: " + addDS.run(ds3));
		rds = new ReadDataSources();
		listDS = rds.run();
		listDS.forEach ( y -> logger.info("datasources after the add uvw: " + y.getUrl()));
		
		DeleteDataSource delDS = new DeleteDataSource();
		logger.info("Result of delete  data abc: " + delDS.run(ds1));
		rds = new ReadDataSources();
		listDS = rds.run();
		logger.info("number of datasets after delete abc: " + listDS.size());
		listDS.forEach ( y -> logger.info("datasources after delete abc: " + y.getUrl()));
		
		logger.info("Result of delete  data 123: " + delDS.run(ds2));
		rds = new ReadDataSources();
		listDS = rds.run();
		listDS.forEach ( y -> logger.info("datasources after delete 123: " + y.getUrl()));
		
		logger.info("Result of delete  data uvw: " + delDS.run(ds3));		
		rds = new ReadDataSources();
		listDS = rds.run();
		listDS.forEach ( y -> logger.info("datasources after delete uvw: " + y.getUrl())); 
	}
}