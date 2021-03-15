package com.rebecca.elevatorSimulator.tests;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;

import org.junit.Test;

import com.rebecca.elevatorSimulator.User;

public class PostAPITest {

	@Test
	public void testPost() {
		// testing adding a new user to database
		Client client = ClientBuilder.newClient();
		List<Integer> buildingList = new ArrayList<>();
		buildingList.add(1);
		buildingList.add(2);
		User user = new User("John Doe", buildingList);

		final String result = client.target("http://localhost:8080/elevatorSimulator/webapi/elevatorservice/addUser")
				.request()
				.post(Entity.json(user), String.class);

		// this is what is returned when API is called when user called John Doe with this building list 
		// is added using JSON.
		assertEquals("{\"buildingList\":[1,2],\"name\":\"John Doe\",\"userId\":1}", result);
	}

}
