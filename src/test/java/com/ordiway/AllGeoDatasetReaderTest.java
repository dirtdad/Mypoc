package com.ordiway;

import java.util.List;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;

import com.ordiway.AllGeoDatasetReader;
import com.ordiway.GeoCtxSession;
import com.ordiway.GeoDataset;

public class AllGeoDatasetReaderTest {
	private final static Logger logger = Logger.getLogger(AllGeoDatasetReaderTest.class.getName());

	@Test
	public final void test() throws UnexpectedInputException, ParseException, NonTransientResourceException, Exception {
		GeoCtxSession geoCtxSession = new GeoCtxSession();
		geoCtxSession.run();
		
		// read all geodatasets	
		AllGeoDatasetReader allGeoDatasetReader = new AllGeoDatasetReader();
		allGeoDatasetReader.setGeoCtxSession(geoCtxSession);
		List<GeoDataset> allGeoDSList = allGeoDatasetReader.read();
		for (GeoDataset x: allGeoDSList){
			logger.info("GeoDS List: " + x.getId() + " number of observations: "+ x.getObservations().size() +
					" first position: " +x.getObservations().iterator().next().getPosition());
		}		
	}
}