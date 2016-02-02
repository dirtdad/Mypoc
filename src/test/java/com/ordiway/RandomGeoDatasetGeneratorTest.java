package com.ordiway;

import java.util.Iterator;

import org.apache.log4j.Logger;
import org.junit.Test;

import com.ordiway.GeoDataset;
import com.ordiway.Observation;
import com.ordiway.RandomGeoDatasetReader;

public class RandomGeoDatasetGeneratorTest {
	private final static Logger logger = Logger.getLogger(RandomGeoDatasetGeneratorTest.class.getName());

	@Test
	public final void test() throws Exception {

		RandomGeoDatasetReader rgdr = new RandomGeoDatasetReader();
		GeoDataset geoDS = rgdr.read();

		if (geoDS != null) {

			Iterator<Observation> itr = geoDS.getObservations().iterator();
			while(itr.hasNext()) {
				Observation obs = (Observation) itr.next();    
				logger.info("GeoDataset: " + geoDS.getId() + " " + obs.getPosition() + " " + obs.getTimeStamp() + " " + obs.getGeoDatasets());
			}
		} else {logger.info("null dataset");}
	}
}