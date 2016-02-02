package com.ordiway;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.List;

import org.apache.log4j.Logger;

public class AddDataSource {
	private final static Logger logger = Logger.getLogger(AddDataSource.class.getName());
	BufferedWriter writer = null;
	public String run (DataSource datasource){
		try {			

			//read datasource.conf from the resource folder
			ClassLoader classLoader = getClass().getClassLoader();
			File dataSourceFile = new File(classLoader.getResource("datasource.conf").getFile());
            
            //Start by reading all datasources
            ReadDataSources rds = new ReadDataSources();
            List<DataSource> listDS = rds.run();
            
			if (listDS.size() == 0) {
				logger.info("no datasource file");
				return "FAILURE";
			}
            
            //Only add to list if it is new
            for (DataSource ds : listDS) {if (datasource.equals(ds)) return "EXISTS";}

			writer = new BufferedWriter(new FileWriter(dataSourceFile, true));
			writer.write(datasource.getUrl()
		            + " " + datasource.getUser()
		            + "\n");
			writer.close();
		} catch (Exception e) {
			return "FAILURE";
		} 
		return "SUCCESS";
	}
}