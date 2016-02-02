package com.ordiway;

import java.util.Iterator;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;

import com.ordiway.GeoDataset;
import com.ordiway.Observation;
import com.ordiway.RandomGeoDatasetReader;


public class RandomGeoDatasetReaderTest {
	private final static Logger logger = Logger.getLogger(RandomGeoDatasetReaderTest.class.getName());

	@Test
	public final void test() throws UnexpectedInputException, ParseException, NonTransientResourceException, Exception {
		RandomGeoDatasetReader ranTest = new  RandomGeoDatasetReader();
		GeoDataset geoDS = ranTest.read();
	    Iterator<Observation> itr = geoDS.getObservations().iterator();
	    while(itr.hasNext()) {
	         Observation obs = (Observation) itr.next();    
		logger.info("GeoDataset: " + geoDS.getId() + " " + obs.getPosition() + " " + obs.getTimeStamp());
	    }
	}
}