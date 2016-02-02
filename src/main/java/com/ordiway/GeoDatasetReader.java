package com.ordiway;

import org.apache.log4j.Logger;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.hibernate.Session;

public class GeoDatasetReader implements ItemReader<GeoDataset> {
	private static final Logger logger = Logger.getLogger(GeoDatasetReader.class.getName());
	private GeoCtxSession geoCtxSession;
	private Long geoDSID;
	
	public GeoDatasetReader(){}
	
	public Long getGeoDSID() {
		return geoDSID;
	}
	public void setGeoDSID (Long geoDSID) {
		this.geoDSID = geoDSID;
	}

	public GeoCtxSession getGeoCtxSession() {
		return geoCtxSession;
	}

	public void setSGeoCtxSession (GeoCtxSession geoCtxSession) {
		this.geoCtxSession = geoCtxSession;
	}

	@Override
	public GeoDataset read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
		Session session = geoCtxSession.getSession();
		logger.info("Getting geodataset id#: " + geoDSID);
		GeoDataset geoDataset =  (GeoDataset) session.get(GeoDataset.class, geoDSID);
		logger.info("Got geodataset id#: " + geoDataset.getId() + " with " + geoDataset.getObservations().size() + " observations");
		return geoDataset;
	}
}