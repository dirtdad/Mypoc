package com.ordiway;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name="geodataset")
public class GeoDataset {

	private Long id;
	private Set<Observation> observations;

	@Id
	@GeneratedValue(generator="increment")
	@GenericGenerator(name="increment", strategy = "increment")
	@Column(name = "geodataset_id")
	public Long getId() {
		return id;
	}

	public void setId (Long id) {
		this.id = id;
	}

//	@ManyToMany(cascade=CascadeType.ALL, mappedBy="geoDatasets")
//	@ManyToMany(mappedBy="geoDatasets")
	@OneToMany(mappedBy="geoDatasets")
	@JsonManagedReference
	public Set<Observation> getObservations() {
		return observations;
	}

	public void setObservations(Set<Observation> observations) {
		this.observations = observations;
	} 
}