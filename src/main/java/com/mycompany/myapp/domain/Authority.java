package com.mycompany.myapp.domain;

import org.springframework.data.neo4j.annotation.GraphId;
import org.springframework.data.neo4j.annotation.Indexed;
import org.springframework.data.neo4j.annotation.NodeEntity;

@NodeEntity
public class Authority {

		@GraphId
		Long id;
		
		@Indexed
		public String name;
		
		public Authority() {}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}
		
	    @Override
	    public String toString() {
	        return "Authority{" +
	                "name='" + name + '\'' +
	                "}";
	    }
}
