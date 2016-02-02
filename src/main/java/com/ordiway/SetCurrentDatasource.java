package com.ordiway;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.log4j.Logger;

public class SetCurrentDatasource {
	private final static Logger logger = Logger.getLogger(SetCurrentDatasource.class.getName());
	BufferedWriter writer = null;
	public SetCurrentDatasource (DataSource datasource, String password) throws IOException {	
		
		//read datasource.conf from the resource folder
		ClassLoader classLoader = getClass().getClassLoader();
		File currentDSFile = new File(classLoader.getResource("currentdatasource.conf").getFile());
		
		String dsPath =  currentDSFile.getCanonicalPath();
		logger.info("writing current datasource to: " + dsPath);
        
		//Write the current datasource to the file
		writer = new BufferedWriter(new FileWriter(currentDSFile, false));
		writer.write(datasource.getUrl()
				+ " " + datasource.getUser()
				+ " " + password
				+ "\n");
		writer.close();
	}
}