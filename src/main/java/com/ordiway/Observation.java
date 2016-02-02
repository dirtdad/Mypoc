package com.ordiway;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.vividsolutions.jts.geom.Polygon;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Table;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

@Entity
@Table(name="observation")
public class Observation {
	
    @Id
    @GeneratedValue(generator="increment")
    @GenericGenerator(name="increment", strategy = "increment")
    @Column(name = "observation_id")
    private Long id;  
	
    @Column(name = "timestamp")
    private Timestamp timeStamp;
         
 /*   @ManyToMany
    @JoinTable(name="geodataset_observation", joinColumns=@JoinColumn(name="observation_id"),
    inverseJoinColumns=@JoinColumn(name="geodataset_id")) */
    
    @ManyToOne 
	@JoinColumn(name = "geodataset_id")
    @JsonBackReference
    private GeoDataset geoDatasets;
    
    @Type(type="org.hibernate.spatial.GeometryType")
    @Column(columnDefinition="Geometry")
    private Polygon position;

    public Polygon getPosition() {
    	return position;
    }
    
    public void setPosition(Polygon position) {
    	this.position = position;
    }
    
    public Long getID() {
        return id;
    }
    
    public void setID(Long id) {
        this.id = id;
    }
    
	public OffsetDateTime getTimeStamp() {
    	LocalDateTime z = timeStamp.toLocalDateTime();
    	ZoneOffset y = ZoneOffset.of("-8"); 	
        return OffsetDateTime.of(z, y);
    }
    
    public void setTimeStamp(OffsetDateTime timeStamp) {	
        this.timeStamp = new Timestamp(1000 * timeStamp.toEpochSecond());
    }
    
    public void setTimeStamp(Timestamp timeStamp) {
        this.timeStamp = timeStamp;
    }
    
    public GeoDataset getGeoDatasets(){
    	return this.geoDatasets;
    }
    
    public void setGeoDatasets(GeoDataset geoDatasets){
    	this.geoDatasets = geoDatasets;
    }
}