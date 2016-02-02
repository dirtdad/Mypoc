package com.ordiway;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.junit.Test;

import com.ordiway.GeoDataset;
import com.ordiway.GeoDatasetDeleter;
import com.ordiway.GeoDatasetWriter;
import com.ordiway.RandomGeoDatasetReader;

public class GeoDatasetDeleterTest {
	private static final Logger logger = Logger.getLogger(GeoDatasetDeleterTest.class.getName());

	@Test
	public final void test() throws Exception {
		
		// make sure we have a gds to delete
		RandomGeoDatasetReader rgdr = new RandomGeoDatasetReader();
		GeoDataset geoDS = rgdr.read();		
		logger.info("id of geodataset created: " + geoDS.getId());
		
		//Write that Geodataset to the database
		GeoDatasetWriter gdsw = new GeoDatasetWriter();
		List<GeoDataset> geoDSList = new ArrayList<GeoDataset>();
		geoDSList.add(geoDS);
		gdsw.write(geoDSList);
		logger.info("id of geodataset from list: " + geoDSList.get(0).getId());
	
		//Now delete the dataset we just created
		GeoDatasetDeleter geoDel = new GeoDatasetDeleter();	
		geoDel.write(geoDSList);
	}
}