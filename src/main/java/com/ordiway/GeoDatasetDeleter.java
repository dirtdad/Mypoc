package com.ordiway;

import java.util.List;

import org.apache.log4j.Logger;

import org.springframework.batch.item.ItemWriter;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class GeoDatasetDeleter implements ItemWriter<GeoDataset> {
	private static final Logger logger = Logger.getLogger(GeoDatasetDeleter.class.getName());

	public GeoDatasetDeleter(){}

	public void write(List<? extends GeoDataset> item) throws Exception {

		ReadCurrentDatasource readCurrentDatasource = new ReadCurrentDatasource();
		List <String> sourceParams = readCurrentDatasource.run();
		DataSource ds = new DataSource (sourceParams.get(0), sourceParams.get(1));
		String password = sourceParams.get(2);
		GeoCtxSession geoCtxSession= new GeoCtxSession(ds, password);
		Session session = geoCtxSession.getSession();

		Long geoDatasetID = item.get(0).getId();
		if(session != null){
			logger.info("Got instance of SessionFactory.");
			Transaction tx = session.beginTransaction();
			GeoDataset geoDataset =  (GeoDataset) session.get(GeoDataset.class, geoDatasetID);
			logger.info("Here to delete: " + geoDataset.getId());

			for (Observation obs: geoDataset.getObservations()){
				obs.setGeoDatasets(null);
				session.update(obs);
			}

			try{        
				logger.info("Deleting geodataset");
				session.delete (geoDataset);
				tx.commit();
				logger.info("Geodataset deleted/commited");
			}
			catch(Exception e){
				tx.rollback();
				logger.error(e.getMessage());
				throw e;
			}
			finally{
				session.flush();
				session.close();
			}
		}
	}
}