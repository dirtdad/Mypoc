package com.ordiway;

import com.bedatadriven.jackson.datatype.jts.JtsModule;
import com.fasterxml.jackson.databind.ObjectMapper;

public class CustomMapper extends ObjectMapper {
	private static final long serialVersionUID = 1L;

	public CustomMapper() {
		super();
		this.registerModule(new JtsModule());
	}
}