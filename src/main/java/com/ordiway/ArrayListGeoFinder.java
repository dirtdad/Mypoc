package com.ordiway;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.vividsolutions.jts.geom.Polygon;

public class ArrayListGeoFinder {

	private static final Logger LOGGER = Logger.getLogger(ArrayListGeoFinder.class.getName());

	public List<Observation> searchForResources(GeoDataset gdsToSearch, Polygon bounds) throws IOException {
		List<Observation> newObs = new ArrayList<Observation>();

		//Search within all Observations of the given GeoDataset
		LOGGER.info("looking withing this bounds: " + bounds);
		for (Observation oneObs : gdsToSearch.getObservations()){
			LOGGER.info("position to consider: " + oneObs.getPosition());
			
			//If the observation is within the bounding box, add it to a list of observations
			if (bounds.contains (oneObs.getPosition())) {	
				LOGGER.info("this positiion is in: " +oneObs.getPosition());
				newObs.add(oneObs);
			}
		}
				
		//If we founds something, put it into a GDS List
		return newObs;
	}
}