package com.ordiway;

import org.apache.log4j.Logger;
import org.springframework.batch.item.ItemProcessor;

public class GeoDatasetProcessor implements ItemProcessor<GeoDataset, Object>  {
	private static final Logger logger = Logger.getLogger(GeoDatasetProcessor.class.getName());
	
	public GeoDatasetProcessor(){}

	@Override
	public Object process(GeoDataset arg0) throws Exception {
		return null;
	}
}