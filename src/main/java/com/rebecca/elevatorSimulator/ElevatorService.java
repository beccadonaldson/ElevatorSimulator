package com.rebecca.elevatorSimulator;


import java.util.List;

import javax.inject.Singleton;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.bson.Document;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;

import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;




@Path("/elevatorservice")
@Singleton
public class ElevatorService {

	// connect to mongodb database using credentials
	MongoClientOptions.Builder options = MongoClientOptions.builder();
	MongoCredential mongoCredential = MongoCredential.createCredential("mongodb4954dr", "mongodb4954", "bo9lic".toCharArray());

	MongoClient mongoClient = new MongoClient(new ServerAddress("danu7.it.nuigalway.ie", 8717), mongoCredential, options.build());
	MongoDatabase database = mongoClient.getDatabase("mongodb4954");


	// add new user to database through JSON input
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/addUser")
	public Response addUser(User u) 
	{
		//grab collection called users and cretae new document in collection.
		MongoCollection<Document> collection = database.getCollection("users");		
		Document document = new Document();
		document.append("userId", u.getUserId());
		document.append("name", u.getName());
		document.append("buildingList", u.getBuildingList());
		collection.insertOne(document);

		//this will return a 201 response if successful. (Status.CREATED == 201)
		return Response.ok(u).status(Status.CREATED).build();
	}

	// using the userId, update document in users collection
	@PUT
	@Path("/{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateUser(@PathParam("id") int userId, User u) 
	{

		MongoCollection<Document>collection = database.getCollection("users");
		collection.updateOne(Filters.eq("userId", userId), Updates.set("name", u.getName()));
		collection.updateOne(Filters.eq("userId", userId), Updates.set("buildingList", u.getBuildingList()));
		return Response.ok().build();

	}

	// grab list of buildings associated with a user

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/user={id}")
	public Response getUserBuildings(@PathParam("id") int userId) 
	{

		//grabbing users collection
		MongoCollection<Document> collection = database.getCollection("users");
		BasicDBObject whereQuery = new BasicDBObject();

		// grab user with userId outlined in path
		whereQuery.put("userId", userId);
		MongoCursor<Document> cursor = collection.find(whereQuery).iterator();

		List<Document> buildingList = null;
		String userName = "";

		// iterate through user and grab buildingList
		while (cursor.hasNext()) {
			Document str = cursor.next();
			buildingList = (List<Document>)str.get("buildingList");
			userName = (String) str.get("name");

		}

		// convert to string and trim first and last characters ([])
		String doc = buildingList.toString();
		String str = doc.substring(1, doc.length() - 1);

		// create array of buildingList content
		String[] elevatorIdString = str.split(", ");

		//convert String array to integer array
		int[] intArray = new int[elevatorIdString.length];
		for(int i = 0; i < elevatorIdString.length; i++) {
			intArray[i] = Integer.parseInt(elevatorIdString[i]);
		}

		String buildNames = "List of buildings associated with user " + userId + "("+ userName + "): \n";

		//iterate through list of buildings to grab building details using buildings collection in database
		for(int i = 0; i < intArray.length; i++) {
			int element = intArray[i];

			MongoCollection<Document> buildCollection = database.getCollection("buildings");
			BasicDBObject buildWhereQuery = new BasicDBObject();
			buildWhereQuery.put("buildId", element);
			MongoCursor<Document> buildCursor = buildCollection.find(buildWhereQuery).iterator();

			while (buildCursor.hasNext()) {
				Document buildStr = buildCursor.next();
				buildNames += buildStr.get("name") + "\n";
			}
		}
		return Response.ok(buildNames).build();
	}


	// get status of all elevators in a building for a user using buildid.
	// dont need usrId here because status of elevators in a building wont change 
	// for whatever user it is
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/build={buildId}")
	public Response getElevatorStatus(@PathParam("buildId") int buildId) 
	{

		MongoCollection<Document> buildCollection = database.getCollection("buildings");
		BasicDBObject buildWhereQuery = new BasicDBObject();
		buildWhereQuery.put("buildId", buildId);
		MongoCursor<Document> buildCursor = buildCollection.find(buildWhereQuery).iterator();

		List<Document> elevatorList = null;
		while (buildCursor.hasNext()) {
			Document str = buildCursor.next();
			elevatorList = (List<Document>)str.get("elevatorList");
		}

		String doc = elevatorList.toString();
		String str = doc.substring(1, doc.length() - 1);
		String[] elevatorIdString = str.split(", ");
		int[] intArray = new int[elevatorIdString.length];
		for(int i = 0; i < elevatorIdString.length; i++) {
			intArray[i] = Integer.parseInt(elevatorIdString[i]);
		}

		String elStatus = "Status of elevators in building " + buildId + ": \n";

		// grab elevator details for each elevator in given building id
		for(int i = 0; i < intArray.length; i++) {
			int element = intArray[i];

			MongoCollection<Document> elevatorCollection = database.getCollection("elevators");
			BasicDBObject elevatorWhereQuery = new BasicDBObject();
			elevatorWhereQuery.put("elevatorId", element);
			MongoCursor<Document> elevatorCursor = elevatorCollection.find(elevatorWhereQuery).iterator();

			while (elevatorCursor.hasNext()) {
				Document elStr = elevatorCursor.next();
				elStatus += elStr.get("name") + ": " + elStr.get("state") + "\n";
			}
		}
		return Response.ok(elStatus).build();
	}

	// summon an elevator using building id and elevator id
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/build={buildId}/elevator={elevatorId}")
	public Response getElevator(@PathParam("buildId") int buildId, @PathParam("elevatorId") int elevatorId) 
	{

		MongoCollection<Document> buildCollection = database.getCollection("buildings");
		BasicDBObject buildWhereQuery = new BasicDBObject();
		buildWhereQuery.put("buildId", buildId);
		MongoCursor<Document> buildCursor = buildCollection.find(buildWhereQuery).iterator();

		String results = "";
		List<Document> elevatorList = null;
		while (buildCursor.hasNext()) {
			Document str = buildCursor.next();
			elevatorList = (List<Document>)str.get("elevatorList");
		}

		String doc = elevatorList.toString();
		String str = doc.substring(1, doc.length() - 1);
		String[] elevatorIdString = str.split(", ");
		int[] intArray = new int[elevatorIdString.length];
		for(int i = 0; i < elevatorIdString.length; i++) {
			intArray[i] = Integer.parseInt(elevatorIdString[i]);
		}

		// check if elevator is in building user is located in
		boolean viableElevator = false;
		int count = 0;

		while(viableElevator == false && count < intArray.length) {

			for(int i = 0; i < intArray.length; i++) {
				if(intArray[i]==elevatorId) {

					viableElevator = true;

					results = "You have summoned elevator " + elevatorId;
				}else{
					count++;
				}
			}
		}

		// if viableElevator is still false i.e. user doesnt choose elevator located in 
		// building they are in, print statement saying the elevator they chose is not 
		// in a building associated with them
		if(viableElevator == false) {

			results = "Enter ID of elevator in building you're in\n"
					+"See list of elevators in you're building below: \n" +
					elevatorList.toString();
			
		}
		return Response.ok(results).build();
	}

	// put API request because we need to update curretnFLoor and state of elevator
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/build={buildId}/elevator={elevatorId}/floor={floorId}")
	public Response getFloor(@PathParam("buildId") int buildId, 
			@PathParam("elevatorId") int elevatorId,@PathParam("floorId") int floorId) 
	{

		MongoCollection<Document> buildCollection = database.getCollection("buildings");
		BasicDBObject buildWhereQuery = new BasicDBObject();
		buildWhereQuery.put("buildId", buildId);
		MongoCursor<Document> buildCursor = buildCollection.find(buildWhereQuery).iterator();

		String results = "";
		List<Document> elevatorList = null;
		while (buildCursor.hasNext()) {
			Document str = buildCursor.next();
			elevatorList = (List<Document>)str.get("elevatorList");
		}

		//check if elevator is in selected bulding id. if its not, print this. if it is, carry on with else statement
		if(!elevatorList.contains(elevatorId)) {
			results = "Enter ID of elevator in building you're in\n"
					+"See list of elevators in you're building below: \n" +
					elevatorList.toString();
		}else {

			String doc = elevatorList.toString();
			String subStr = doc.substring(1, doc.length() - 1);
			String[] elevatorIdString = subStr.split(", ");
			int[] intArray = new int[elevatorIdString.length];
			for(int i = 0; i < elevatorIdString.length; i++) {
				intArray[i] = Integer.parseInt(elevatorIdString[i]);
			}

			boolean viableElevator = false;
			int count = 0;
			int selectedElevatorId = 0;

			while(viableElevator == false && count < intArray.length) {

				for(int i = 0; i < intArray.length; i++) {
					if(intArray[i]==elevatorId) {

						selectedElevatorId = elevatorId;

						viableElevator = true;

						results = "You have summoned elevator " + elevatorId;
					}else{
						count++;
					}
				}
			}

			MongoCollection<Document> elevatorCollection = database.getCollection("elevators");
			BasicDBObject elevatorWhereQuery = new BasicDBObject();
			elevatorWhereQuery.put("elevatorId", selectedElevatorId);
			MongoCursor<Document> elevatorCursor = elevatorCollection.find(elevatorWhereQuery).iterator();

			String elevatorState = null;
			
			// grab state of elevator

			while (elevatorCursor.hasNext()) {
				Document str2 = elevatorCursor.next();
				elevatorState = ((String) str2.get("state"));

			}
			
			// if elevator is out of service, dont move forward & print out of service message
			if(elevatorState.equals(("Out of Service"))) {
				results = "This elevator is out of service";
			}else {

				MongoCollection<Document> floorCollection = database.getCollection("elevators");
				BasicDBObject floorWhereQuery = new BasicDBObject();
				floorWhereQuery.put("elevatorId", selectedElevatorId);
				MongoCursor<Document> floorCursor = floorCollection.find(floorWhereQuery).iterator();

				List<Document> floorList = null;
				int elevatorCurrentFloor = 0;

				while (floorCursor.hasNext()) {
					Document str3 = floorCursor.next();
					elevatorCurrentFloor = (int) str3.get("currentFloor");	
					floorList = (List<Document>) str3.get("floorList");
				}

				String doc2 = floorList.toString();
				String subStr2 = doc2.substring(1, doc2.length() - 1);
				String[] stringArray = subStr2.split(", ");
				int[] intArray2 = new int[stringArray.length];
				for(int i = 0; i < stringArray.length; i++) {
					intArray2[i] = Integer.parseInt(stringArray[i]);
				}

				boolean viableFloor = false;
				int count2 = 0;

				// check currentFloor thats associated with elevator in database. 
				// update state of elevator based on currentFloor
				if(elevatorCurrentFloor==floorId) {
					elevatorCollection.updateOne(Filters.eq("elevatorId", elevatorId), Updates.set("state", "Stopped"));	
				}else if(elevatorCurrentFloor < floorId) {
					elevatorCollection.updateOne(Filters.eq("elevatorId", elevatorId), Updates.set("state", "Up"));
				}else if(elevatorCurrentFloor > floorId) {
					elevatorCollection.updateOne(Filters.eq("elevatorId", elevatorId), Updates.set("state", "Down"));
				}else {
					elevatorCollection.updateOne(Filters.eq("elevatorId", elevatorId), Updates.set("state", "Out of Service"));
				}

				while(viableFloor == false && count2 < intArray2.length) {

					for(int i = 0; i < intArray2.length; i++) {
						if(intArray2[i]==floorId) {

							// update currentFloor in database based on the floor number entered in URI path
							elevatorCollection.updateOne(Filters.eq("elevatorId", elevatorId), Updates.set("currentFloor", floorId));

							viableFloor = true;

							results = "You have selected floor " + floorId;
						}else{
							count2++;
						}
					}
				}

				// if viableElevator is still false, print statement saying
				// the elevator they chose is not in a building associated with them
				if(viableFloor == false) {

					results = "Enter floor number from elevator you're in\n"
							+"See list of floors in you're elevator below: \n" +
							floorList.toString();		
				}
			}
		}
		return Response.ok(results).build();
	}
}

