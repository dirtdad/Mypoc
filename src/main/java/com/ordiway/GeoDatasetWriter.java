package com.ordiway;

import java.util.List;

import org.apache.log4j.Logger;

import org.springframework.batch.item.ItemWriter;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class GeoDatasetWriter implements ItemWriter<GeoDataset>  {
	private static final Logger logger = Logger.getLogger(GeoDatasetWriter.class.getName());
	
	public GeoDatasetWriter(){	}

	public void write(List<? extends GeoDataset> item) throws Exception {
		
		ReadCurrentDatasource readCurrentDatasource = new ReadCurrentDatasource();
		List <String> sourceParams = readCurrentDatasource.run();
		DataSource ds = new DataSource (sourceParams.get(0), sourceParams.get(1));
		String password = sourceParams.get(2);
		GeoCtxSession geoCtxSession= new GeoCtxSession(ds, password);
		Session session = geoCtxSession.getSession();

		logger.info("writer received: " + item + " to write");
		logger.info("item is a: " + item.getClass());
		logger.info("item length: " + item.size());
		GeoDataset geoDataset = item.iterator().next();
		logger.info("Persisting geodataset: " + geoDataset.getId() + " session: " + session);
		
		if(session != null){
			logger.info("Got a session");
			Transaction tx = session.beginTransaction();
			
			//Persist each observation in the geo dataset
			for (Observation myObs: geoDataset.getObservations()){
				logger.info("Persist observation: " + myObs.getPosition());
				session.persist(myObs);
			}

			try{        
			logger.info("Persist the geodataset...");
				session.persist(geoDataset);
				tx.commit();
				logger.info("... geodataset committed.");
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