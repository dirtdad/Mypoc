package com.ordiway;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;

public class AllGeoDatasetReader implements ItemReader<List<GeoDataset>> {
	Logger LOGGER = Logger.getLogger(AllGeoDatasetReader.class.getName());
	private GeoCtxSession geoCtxSession;
	
	public AllGeoDatasetReader(){}

	public GeoCtxSession getGeoCtxSession() {
		return geoCtxSession;
	}

	public void setGeoCtxSession (GeoCtxSession geoCtxSession) {
		this.geoCtxSession = geoCtxSession;
	}

	@Override
	public List<GeoDataset> read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
		Session session = geoCtxSession.getSession();
		List<GeoDataset> geoDSList = null;		
		geoDSList = session.createCriteria(GeoDataset.class).list();
		return geoDSList;
	}
}