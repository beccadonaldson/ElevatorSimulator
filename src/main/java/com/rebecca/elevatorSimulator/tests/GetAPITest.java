package com.rebecca.elevatorSimulator.tests;

import static org.junit.Assert.*;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;

import org.junit.Test;

public class GetAPITest {

	@Test
	public void testGetUserBuildings() {
		
		// grab buildings assicated with user with id of 4. 
		Client client = ClientBuilder.newClient();
		final String result = client.target("http://localhost:8080/elevatorSimulator/webapi/elevatorservice/user=4").request().get(String.class);

		// if correct, this will be returned by API:
		assertEquals("List of buildings associated with user 4(Tom Brady): \n" + 
				"Building 1\n" + 
				"Building 2\n", result);
	}

	@Test
	public void testGetElevatorStatus() {
		Client client = ClientBuilder.newClient();
		final String result = client.target("http://localhost:8080/elevatorSimulator/webapi/elevatorservice/build=1").request().get(String.class);

		assertEquals("Status of elevators in building 1: \n" + 
				"Elevator 1: Out of Service\n" + 
				"Elevator 2: Up\n" + 
				"Elevator 3: Stopped\n"
				, result);
	}
	
	@Test
	public void testGetElevator() {
		Client client = ClientBuilder.newClient();
		final String result = client.target("http://localhost:8080/elevatorSimulator/webapi/elevatorservice/build=2/elevator=2").request().get(String.class);

		assertEquals("You have summoned elevator 2", result);
	}
	
}
