package com.ordiway;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.junit.Test;

import com.ordiway.DataSource;
import com.ordiway.GeoCtxSession;
import com.ordiway.GeoDataset;
import com.ordiway.GeoDatasetWriter;
import com.ordiway.RandomGeoDatasetReader;

public class GeoCtxSessionTest {
	private final static Logger logger = Logger.getLogger(GeoCtxSessionTest.class.getName());

	@Test
	public final void test() throws Exception {
		
		String url = "jdbc:postgresql://test.cpuimxjku7wp.us-west-2.rds.amazonaws.com/test";
		logger.info("Getting a session by java at: " + url);
		DataSource ds = new DataSource (url, "testuser");
		GeoCtxSession appCtxSess = new GeoCtxSession(ds, "testtest");
		
		logger.info("Manager, SF: " +  appCtxSess.getSessionFactory());	
		Session session = appCtxSess.getSessionFactory().openSession();
		
		//Do a test by writing and reading a geodataset
		//create a geodataset		
		RandomGeoDatasetReader rgdr = new RandomGeoDatasetReader();
		GeoDataset newGds = rgdr.read();
		logger.info(" new gds, number of obs, first obs: " + newGds.getId() + " " + newGds.getObservations().size() + " " +
		newGds.getObservations().iterator().next().getPosition());
		
		//write the geodataset to the database, thus testing the connection
		List<GeoDataset> gdsList = new ArrayList<GeoDataset>();
		gdsList.add(newGds);
		logger.info("first gds from list position is : " + gdsList.listIterator().next().getObservations().iterator().next().getPosition());
		GeoDatasetWriter gdw = new GeoDatasetWriter();
		gdw.write(gdsList); 
		
		//lets do it again on a different datasource
/*		url = "jdbc:postgresql://192.168.10.90/geo01";
		logger.info("Getting a session by java at: " + url);
		ds = new DataSource (url, "testuser");
		appCtxSess = new GeoCtxSession(ds, "test");
		
		logger.info("Manager, SF: " +  appCtxSess.getSessionFactory());	
		session = appCtxSess.getSessionFactory().openSession(); */
		 
		//Do a test by writing and reading a geodataset
		//create a geodataset		
		rgdr = new RandomGeoDatasetReader();
		newGds = rgdr.read();
		logger.info(" new gds, number of obs, first obs: " + newGds.getId() + " " + newGds.getObservations().size() + " " +
		newGds.getObservations().iterator().next().getPosition());
		
		//write the geodataset to the database, thus testing the connection
		gdsList = new ArrayList<GeoDataset>();
		gdsList.add(newGds);
		logger.info("first gds from list position is : " + gdsList.listIterator().next().getObservations().iterator().next().getPosition());
		gdw = new GeoDatasetWriter();
		gdw.write(gdsList); 
	}
}