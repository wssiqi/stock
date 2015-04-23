package com.dev.web.model;

public class MemDB extends BaseDB {

	@Override
	protected String getConnectionUrl() {
		return "jdbc:h2:mem:db1";
	}

}
