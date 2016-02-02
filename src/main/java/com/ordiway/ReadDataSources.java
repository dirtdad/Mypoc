package com.ordiway;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.apache.log4j.Logger;

public class ReadDataSources {
	private final static Logger logger = Logger.getLogger(ReadDataSources.class.getName());
	public List<DataSource> run () throws IOException {

		//open datasource.conf from the resource folder
		ClassLoader classLoader = getClass().getClassLoader();
		File dsFile = null;
		try {
			URL url = classLoader.getResource("datasource.conf");
			dsFile = new File(url.getFile());}
		catch (Exception e){
			logger.info("ioerror: " + e.getMessage());
			return null;
		}

		//Where is this thing at
		String dsPath =  dsFile.getCanonicalPath();
		logger.info("reading datasources from: " + dsPath);
		List<DataSource> dsList = new ArrayList<DataSource>();

		Stream<String> stream = Files.lines (Paths.get(dsPath),Charset.defaultCharset());		

		stream
		.filter(line -> !line.isEmpty())
		.map(line -> line.split(" "))
		.forEach(x -> dsList.add(new DataSource(x[0], x[1])));
		stream.close();

		return dsList;
	}
}