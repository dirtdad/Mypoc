package com.ordiway;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.log4j.Logger;

public class ReadCurrentDatasource {
	private final static Logger logger = Logger.getLogger(ReadCurrentDatasource.class.getName());
	BufferedWriter writer = null;
	public List <String> run() throws IOException {

		//set up currentdatasource.conf
		File currentDSFile = null;
		try {
			ClassLoader classLoader = getClass().getClassLoader();
			currentDSFile = new File(classLoader.getResource("currentdatasource.conf").getFile()); }
		catch (Exception e){
			logger.info("ioerror: " + e.getMessage());
		}

		//read the current datasource from the file
		//Where is this thing at
		String dsPath =  currentDSFile.getCanonicalPath();
		logger.info("reading current datasource from: " + dsPath);
		List <String> sourceParams = Files.lines (Paths.get(dsPath), Charset.defaultCharset())	
				.map(line -> line.split(" "))
				.flatMap(Arrays::stream)
				.map(String::valueOf)
				.collect(Collectors.toList());
		logger.info("sourceParams: " + sourceParams.get(0)
		+ " " + sourceParams.get(1)
		+ " " + sourceParams.get(2));

		return sourceParams;
	}
}