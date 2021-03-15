package com.rebecca.elevatorSimulator;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class User {
	
	
	private static AtomicInteger id  = new AtomicInteger(1);
	
	private int userId;
	private String name;
	
	private List<Integer> buildingList;
	 
   
	public User() {
		userId = id.getAndIncrement();
	}
	

	public User(String name, List<Integer> buildingList) {
		//assign incremented id to an integer object.
		userId = id.getAndIncrement();
		this.name = name;
		this.buildingList = buildingList;
	}


	public static AtomicInteger getId() {
		return id;
	}


	public int getUserId() {
		return userId;
	}


	public String getName() {
		return name;
	}


	public List<Integer> getBuildingList() {
		return buildingList;
	}


	public static void setId(AtomicInteger id) {
		User.id = id;
	}


	public void setUserId(int userId) {
		this.userId = userId;
	}


	public void setName(String name) {
		this.name = name;
	}


	public void setBuildingList(List<Integer> buildingList) {
		this.buildingList = buildingList;
	}


}
