package com.ordiway;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.hibernate.exception.GenericJDBCException;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.io.WKTReader; 

@RestController
@RequestMapping("/gis")
public class GisController
{
	private static final Logger LOGGER = Logger.getLogger(GisController.class.getName());
	private GeoCtxSession geoCtxSession;
	private UUID currentUUID = UUID.randomUUID();

	public GeoCtxSession getGeoCtxSession() {
		return geoCtxSession;
	}

	public void setGeoCtxSession(GeoCtxSession geoCtxSession) {
		this.geoCtxSession = geoCtxSession;
	}
	
	public GisController(){}

	//Set the Data Source
	@RequestMapping(value = "/openDataSource", method=RequestMethod.GET)
	public String openDataSource
	(@RequestParam (value="url", defaultValue="") String url,
			@RequestParam (value="userid", defaultValue="") String userid,
			@RequestParam (value="passwd", defaultValue="") String passwd) {
		DataSource datasource = new DataSource(url, userid);
		LOGGER.info("Changing datasources");

		try {
			geoCtxSession = new GeoCtxSession(datasource, passwd);
		} catch (GenericJDBCException e) {
			LOGGER.info("jdbc error: " + e);
			return "FAIL " + e.getMessage();
		} catch (IOException e) {
			LOGGER.info("Error setting current datasource: " + e);
			return "FAIL " + e.getMessage();
		}
		return "SUCCESS";
	} 

	//Save the Data Source
	@RequestMapping(value = "/saveDataSource", method=RequestMethod.GET)
	public String saveDataSource
	(@RequestParam (value="url", defaultValue="") String url,
			@RequestParam (value="userid", defaultValue="") String userid) {
		DataSource datasource = new DataSource(url, userid);
		AddDataSource ads = new AddDataSource();
		String result = ads.run(datasource);
		return result;
	} 
	
	//Delete a Data Source
	@RequestMapping(value = "/delDataSource", method=RequestMethod.GET)
	public String deleteDataSource
	(@RequestParam (value="url", defaultValue="") String url,
			@RequestParam (value="userid", defaultValue="") String userid) {
		DataSource datasource = new DataSource(url, userid);
		DeleteDataSource dds = new DeleteDataSource();
		String status = "FAILURE";
		try {
			status = dds.run(datasource);
		} catch (IOException e) {
			return "FAILURE";
		}
		return status;
	} 
	
	//Read the Data Sources
	@RequestMapping(value = "/readDataSources", method=RequestMethod.GET)
	public List<DataSource> readDataSources() throws IOException {
		List<DataSource> dsList = new ArrayList<DataSource>();
		ReadDataSources rds = new ReadDataSources();
		dsList = rds.run();
		return dsList;
	} 

	@RequestMapping(value = "/search/{datasetID}", method=RequestMethod.GET)
	public List<Observation> searchSingleDataset
	(@PathVariable String datasetID,			
			@RequestParam(value="upperLeftLat", defaultValue="") String upperLeftLat,
			@RequestParam (value="upperLeftLon", defaultValue="")String upperLeftLon,
			@RequestParam(value="lowerRightLat", defaultValue="")String lowerRightLat,
			@RequestParam(value="lowerRightLon", defaultValue="")String lowerRightLon)
					throws UnexpectedInputException, ParseException, NonTransientResourceException, Exception 
	{	
		ArrayListGeoFinder finder = new ArrayListGeoFinder();
		List<Observation> hits = null;
		
		LOGGER.info("search dataset, coords: ullat, ulLong, lrlat, lrlon " + datasetID 
				+ " " + upperLeftLat
				+ " " + upperLeftLon
				+ " " + lowerRightLat
				+ " " + lowerRightLon);
		String positionString = "POLYGON((" + 
				upperLeftLon + " " + lowerRightLat + "," +
				upperLeftLon + " " + upperLeftLat + "," +
				lowerRightLon+ " " + upperLeftLat + "," +
				lowerRightLon+ " " + lowerRightLat + "," +
				upperLeftLon+ " " + lowerRightLat +"))";

		WKTReader fromText = new WKTReader();
		Geometry geom = null;
		try {
			geom = fromText.read(positionString);
		} 
		catch (ParseException e) {
			throw new RuntimeException("Not a WKT string:" + positionString);
		}

		Polygon env = (Polygon) geom;   
		GeoDatasetReader gdr = new GeoDatasetReader();
		gdr.setSGeoCtxSession(geoCtxSession);
		gdr.setGeoDSID(Long.parseLong(datasetID));
		GeoDataset geoDS = gdr.read();	

		LOGGER.info("read geodataset " + geoDS.getId() + " with this many obs: " + geoDS.getObservations().size());	
		LOGGER.info("obs in geods " + geoDS.getId() + ":");
		for(Observation obs : geoDS.getObservations()){
			LOGGER.info("observation: " + obs.getID() + " " + obs.getPosition());
		}

		hits = finder.searchForResources(geoDS, env);
		LOGGER.info("Search completed.");

		LOGGER.info("Number of hits: " + hits.size());
		return hits; 
	}  

	//create a dataset and and return the GeoDataset
	@RequestMapping(value = "/newGeoDataset", method=RequestMethod.GET)
	public GeoDataset createNewGeoDataset () throws Exception{
		
		// make sure we have a gds to read by making a new one
		RandomGeoDatasetReader rgdr = new RandomGeoDatasetReader();
		GeoDataset geoDS = rgdr.read();	
		
		//Write that Geodataset to the database
		GeoDatasetWriter gdsw = new GeoDatasetWriter();
		List<GeoDataset> geoDSList = new ArrayList<GeoDataset>();
		geoDSList.add(geoDS);
		gdsw.write(geoDSList);
		LOGGER.info("id of the new dataset: " + geoDSList.get(0).getId());
		return geoDSList.get(0);
	} 

	//return a list of all geodatasets
	@RequestMapping(value = "/allGeoDatasets", method=RequestMethod.GET)
	public List<GeoDataset> readAllDatasets() throws UnexpectedInputException, ParseException, NonTransientResourceException, Exception {
		AllGeoDatasetReader allGeoDatasetReader = new AllGeoDatasetReader();
		allGeoDatasetReader.setGeoCtxSession(geoCtxSession);
		List<GeoDataset> allGeoDSList = allGeoDatasetReader.read();
		return allGeoDSList;
	} 

	//return the current uuid
	@RequestMapping(value = "/getUUID", method=RequestMethod.GET)
	public UUID readUUID()  {
		return currentUUID;
	}

	//delete a dataset by dataset id
	@RequestMapping(value = "/remove/{datasetID}", method=RequestMethod.GET)
	public String delDataset (@PathVariable String datasetID) {

		// make a minimal gds with just an id and delete the DS we just created
		Long gdsID = Long.parseLong(datasetID);;
		GeoDataset idOnlyGDS = new GeoDataset();
		idOnlyGDS.setId(gdsID);
		List<GeoDataset> geoDSList = new ArrayList<GeoDataset>();
		geoDSList.add(idOnlyGDS);

		GeoDatasetDeleter geoDel = new GeoDatasetDeleter();
		try {
			geoDel.write(geoDSList);
		} catch (Exception e) {
			e.printStackTrace();
			return "FAIL";
		}	
		return "SUCCESS";
	}
}