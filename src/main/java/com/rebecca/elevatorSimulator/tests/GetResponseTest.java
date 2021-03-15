package com.rebecca.elevatorSimulator.tests;

import static org.junit.Assert.*;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.HttpClientBuilder;
import org.junit.Test;

public class GetResponseTest {

	@Test
	public void testIfUserExists()throws ClientProtocolException, IOException {

		// testing response to get request using userId
		HttpUriRequest request = new HttpGet( "http://localhost:8080/elevatorSimulator/webapi/elevatorservice/user=4");

		HttpResponse response = HttpClientBuilder.create().build().execute(request);

		// if user with id of 3 wasnt in databse, 404 error would occur and test case would fail
		assertEquals(response.getStatusLine().getStatusCode(),HttpStatus.SC_OK);
	}

}
