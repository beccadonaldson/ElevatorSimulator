package com.rebecca.elevatorSimulator.tests;

import static org.junit.Assert.*;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.HttpClientBuilder;
import org.junit.Test;

public class MediaTypeTest {

	@Test
	public void testIfMediaReturnedIsJSON()throws ClientProtocolException, IOException {

		HttpUriRequest request = new HttpGet( "http://localhost:8080/elevatorSimulator/webapi/elevatorservice/user=3" );

		HttpResponse response = HttpClientBuilder.create().build().execute( request );

		// if data produced (@Produces(MediaType.APPLICATION_JSON)) was not JSON, test case would fail
		String dataProduced = ContentType.getOrDefault(response.getEntity()).getMimeType();
		assertEquals("application/json", dataProduced);
	}

}
