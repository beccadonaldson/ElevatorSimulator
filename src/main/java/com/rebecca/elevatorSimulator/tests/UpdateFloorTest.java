package com.rebecca.elevatorSimulator.tests;

import static org.junit.Assert.*;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.junit.Test;

import com.rebecca.elevatorSimulator.Elevator;

public class UpdateFloorTest {

	@Test
	public void testGetFloor() {
		Client client = ClientBuilder.newClient();
		
		// because this API is a put request (in order to update floor and state)
		// must edit the currentFloor and state of elevator here so that 
		// we can insert this updated object into the put request
		Elevator elevator = new Elevator();
		elevator.setCurrentFloor(20);
		// dont need to set state because this will be done by API depending on previous floor. 

		final Response result = client.target("http://localhost:8080/elevatorSimulator/webapi/elevatorservice/build=2/elevator=4/floor=20")
				.request()
				.put(Entity.entity(elevator, MediaType.APPLICATION_JSON));

		// when update query is ran, a status 200 is returned because update was performed successfully
		assertEquals("Should return status 200", 200, result.getStatus());


	}

}
