package com.ordiway;

import org.apache.log4j.Logger;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.context.ApplicationContext;

public class RandomGeoDatasetGenerator {
	private static final Logger logger = Logger.getLogger(RandomGeoDatasetGenerator.class.getName());
	
	public void run() throws Exception {
					
		GeoCtxSession appCtxSession = new GeoCtxSession();
		appCtxSession.run();
		ApplicationContext context = appCtxSession.getAppContext(); 
	    JobLauncher jobLauncher = (JobLauncher) context.getBean("jobLauncher");	    
	    Job job = (Job) context.getBean("RandomGeoDatasetGenerator");
	    logger.info("got job: " + job);
	    try {
	        JobExecution execution = jobLauncher.run(job, new JobParameters());
	        logger.info("Exit Status : " + execution.getStatus());

	    } catch (Exception e) {
	    	logger.info("exception running job: " + e);
	    }
	}
}