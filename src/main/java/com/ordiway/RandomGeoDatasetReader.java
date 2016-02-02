package com.ordiway;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.io.WKTReader;

public class RandomGeoDatasetReader implements ItemReader<GeoDataset> {
	
	// Variable that allows the reader to return null eventually
	private boolean EOT;
	
	public RandomGeoDatasetReader(){
		EOT = false;
	}

	Logger LOGGER = Logger.getLogger(RandomGeoDatasetReader.class.getName());
	
	@Override
	public GeoDataset read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
		
		LOGGER.info("EOT: " + EOT);
		//End the I/O and flip the EOT flag
		if (EOT) {
			EOT = false;
			return null;
		}
		
		// Generate a random number of observations from 6-10
		Random rand = new Random();
		int numberObs = rand.nextInt(5) + 5;
		
		Set<Observation> myObs = new HashSet<Observation>(); 
		GeoDataset geoDS = new GeoDataset();

		double minLat=30.0, maxLat = 47.0, minLong = -120.0, maxLong = -76.0;

		for (Integer obsNum=0; obsNum<numberObs; obsNum++) {
			 rand = new Random();	

			Observation oneObs = new Observation();

			int year = rand.nextInt(1800) + 215;
			int month = rand.nextInt(11) + 1;
			int day = rand.nextInt(27) + 1;
			int hour = rand.nextInt(23) + 1;
			int minute = rand.nextInt(59) + 1;
			int second = rand.nextInt(59) + 1;
			int nano = 0;

			ZoneOffset zoneOffset = ZoneOffset.of("-8");
			OffsetDateTime offsetDateTime = OffsetDateTime.of(year, month, day, hour, minute, second, nano, zoneOffset);

			double lat1=0, lat2=0, long1=0, long2=0;

			while (lat1==lat2) {
				lat1 = minLat + (maxLat-minLat)*rand.nextFloat();
				lat2 = minLat + (maxLat-minLat)*rand.nextFloat();
			}

			while (long1==long2) {
				long1 = minLong + (maxLong-minLong)*rand.nextFloat();
				long2 = minLong + (maxLong-minLong)*rand.nextFloat();
			}	

			String positionString = "POLYGON((" + long1+ " " + lat1 + "," +
					long1+ " " + lat2 + "," +
					long2+ " " + lat2 + "," +
					long2+ " " + lat1 + "," +
					long1+ " " + lat1 +"))";
			LOGGER.info("Polygon string: " + positionString);

			WKTReader fromText = new WKTReader();
			Geometry geom = null;
			try {
				geom = fromText.read(positionString);
			} 
			catch (ParseException e) {
				throw new RuntimeException("Not a WKT string:" + positionString);
			}

			oneObs.setTimeStamp(offsetDateTime);
			oneObs.setGeoDatasets(geoDS);
			oneObs.setPosition((Polygon) geom);
			myObs.add(oneObs);		
		}
		geoDS.setObservations(myObs);
		
		EOT = true;
		return geoDS;
	}
}