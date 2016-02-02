package com.ordiway;

import java.sql.Timestamp;

import org.apache.log4j.Logger;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ordiway.CustomMapper;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.io.WKTReader;

public class test1 {
	private final static Logger logger = Logger.getLogger(test1.class.getName());
	public static void main(String[] args) throws com.vividsolutions.jts.io.ParseException, JsonProcessingException {

		CustomMapper objectMapper = new CustomMapper();
		Timestamp ts = new Timestamp(System.currentTimeMillis());
		logger.info("timestamp: " +  ts);
		logger.info("serialized timestamp: " + objectMapper.writeValueAsString(ts));

		WKTReader fromText = new WKTReader();
		String positionString = "POLYGON((0.0 0.0, 0.0 1.0, 1.0 1.0, 1.0 0.0, 0.0 0.0))";
		Geometry geom = fromText.read(positionString);

		logger.info("Polygon: " +  geom); 
		logger.info("serialized geom: " + objectMapper.writeValueAsString(geom));
	}
}