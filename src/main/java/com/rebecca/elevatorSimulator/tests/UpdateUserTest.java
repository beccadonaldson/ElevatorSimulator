package com.rebecca.elevatorSimulator.tests;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.junit.Test;

import com.rebecca.elevatorSimulator.User;

public class UpdateUserTest {

	@Test
	public void testUpdateUser() {
		// testing updating user in database
		Client client = ClientBuilder.newClient();
		
		// set new values for user
		List<Integer> buildingList = new ArrayList<>();
		buildingList.add(1);
		buildingList.add(3);
		User user = new User("Jane Doe", buildingList);

		// input these new values using target below and 'put' them into user with id of 3 using JSON format
		final Response result = client.target("http://localhost:8080/elevatorSimulator/webapi/elevatorservice/3")
				.request()
				.put(Entity.entity(user, MediaType.APPLICATION_JSON));

		// when update query is ran, a status 200 is returned because update was performed successfully
		assertEquals("Should return status 200", 200, result.getStatus());


	}
}
