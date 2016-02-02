package com.ordiway;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.exception.GenericJDBCException;
import org.hibernate.exception.JDBCConnectionException;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.ordiway.CustomMapper;
import com.ordiway.DataSource;
import com.ordiway.GeoCtxSession;
import com.ordiway.GeoDataset;
import com.ordiway.GisController;
import com.ordiway.Observation;
import org.junit.Test;

public class GisControllerTest {
	private final static Logger logger = Logger.getLogger(GisControllerTest.class.getName());

	@Test
	public final void test() throws Exception {		
		logger.info("in giscontroller test");
		DataSource ds = new DataSource("jdbc:postgresql://test.cpuimxjku7wp.us-west-2.rds.amazonaws.com/test", "testuser");
		logger.info("datasource: " + ds);
		GisController testGC = new GisController();
		GeoCtxSession geoCtxSession = new GeoCtxSession(ds, "testtest");
		testGC.setGeoCtxSession(geoCtxSession);

		List<GeoDataset> geoDSList = testGC.readAllDatasets();		
		// test serialization to json
		GeoDataset myGeoDS = geoDSList.get(0);
		CustomMapper objectMapper = new CustomMapper();
		objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true);

		Observation testObs = myGeoDS.getObservations().iterator().next();
		logger.info("first obs serialized: " + objectMapper.writeValueAsString(testObs));

		GeoDataset gds = testGC.createNewGeoDataset();
		Long dsid = gds.getId();
		logger.info("created geodataset: " + dsid);
		List<Observation> obsList = testGC.searchSingleDataset(dsid.toString(), "45.0", "-117.0", "31.0", "-78");
		logger.info("searched and found this many observations: " + obsList.size());
		if (obsList.size() > 0) {logger.info("First observation position looks like: " + obsList.iterator().next().getPosition());
		} else {logger.info("test search had zero results"); }
		testGC.delDataset(dsid.toString());

		//Make a bad connection
		try {
			new GeoCtxSession(new DataSource("jdbc:postgresql://192.168.10.90/badtest", "testuser"), "test");
		} catch (GenericJDBCException | JDBCConnectionException e) {
			logger.info("jdbc error: " + e.getMessage());
		}
		geoDSList = testGC.readAllDatasets();
		String result = testGC.saveDataSource("123", "456");
		logger.info("add source result: " + result);
		if (result =="SUCCESS")	testGC.deleteDataSource("123", "456");
		List<DataSource> dsList = testGC.readDataSources();
		if (dsList != null) 
		dsList.forEach(x -> logger.info("datasource: " + x.getUrl() + " " + x.getUser()));
		result = testGC.openDataSource("jdbc:postgresql://test.cpuimxjku7wp.us-west-2.rds.amazonaws.com/test", "testuser", "testtest");
		logger.info("result of changing datasource: " + result);
	}
}