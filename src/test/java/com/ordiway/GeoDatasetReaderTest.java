package com.ordiway;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;

import com.ordiway.GeoCtxSession;
import com.ordiway.GeoDataset;
import com.ordiway.GeoDatasetReader;
import com.ordiway.GeoDatasetWriter;
import com.ordiway.RandomGeoDatasetReader;

public class GeoDatasetReaderTest {
	private static final Logger logger = Logger.getLogger(GeoDatasetReaderTest.class.getName());

	@Test
	public final void test() throws UnexpectedInputException, ParseException, NonTransientResourceException, Exception  {		
		GeoCtxSession appCtxSession = new GeoCtxSession();
		appCtxSession.run();
		
		// make sure we have a gds to read by making a new one
		RandomGeoDatasetReader rgdr = new RandomGeoDatasetReader();
		GeoDataset geoDS = rgdr.read();	
		
		//Write that Geodataset to the database
		GeoDatasetWriter gdsw = new GeoDatasetWriter();
		List<GeoDataset> geoDSList = new ArrayList<GeoDataset>();
		geoDSList.add(geoDS);
		gdsw.write(geoDSList);
		logger.info("id of the dataset to be read: " + geoDSList.get(0).getId());
			
		// now read the gds we just made
		GeoDatasetReader gdr = new GeoDatasetReader();
		gdr.setSGeoCtxSession(appCtxSession);
		gdr.setGeoDSID(geoDSList.get(0).getId());
		geoDS = gdr.read();	
		logger.info("read geodataset: " + geoDS.getId() + " with this many obs: " + geoDS.getObservations().size());
	}
}