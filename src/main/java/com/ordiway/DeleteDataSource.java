package com.ordiway;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import org.apache.log4j.Logger;

public class DeleteDataSource {
	private final static Logger logger = Logger.getLogger(DeleteDataSource.class.getName());
	BufferedWriter writer = null;

	public DeleteDataSource(){}

	public String run (DataSource datasource) throws IOException {

		//read datasource.conf from the resource folder
		ClassLoader classLoader = getClass().getClassLoader();
		File dataSourceFile = new File(classLoader.getResource("datasource.conf").getFile());
		String location = dataSourceFile.getAbsolutePath();
		File tempFile = new File(location + ".tmp");
		PrintWriter writer = new PrintWriter(new FileWriter(tempFile));

		//Start by reading all datasources
		ReadDataSources rds = new ReadDataSources();
		List<DataSource> listDS = rds.run();

		//Write them all back out to the temp file, except for the one we are deleting
		if (listDS.size() > 0) {
			for (DataSource ds : listDS) {
				if (!datasource.equals(ds)){					
					writer.write(ds.getUrl()
							+ " " + ds.getUser()
							+ "\n");
				}			
			} 
			writer.close();
			
			//Delete the original file
			if (!dataSourceFile.delete()) {
				logger.error("Could not delete file: " + dataSourceFile.getAbsolutePath());
				return "FAILURE";
			} 

			//Rename the temp file to take the place of the original file
			if (!tempFile.renameTo(dataSourceFile)) {
				logger.error("Could not rename file: " + tempFile.getAbsolutePath());
				return "FAILURE";
			} 
		}	
		return "SUCCESS";
	}
}