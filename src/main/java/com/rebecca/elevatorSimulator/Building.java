package com.rebecca.elevatorSimulator;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class Building {
	
	private static AtomicInteger id  = new AtomicInteger(1);
	
	private int buildId;
	private String name;
	private String location;
	
	private List<Integer> elevatorList;

	public Building(String name, String location, List<Integer> elevatorList) {
		buildId = id.getAndIncrement();
		this.name = name;
		this.location = location;
		this.elevatorList = elevatorList;
	}

	public Building() {
		buildId = id.getAndIncrement();
		name = null;
		location = null;
	}

	public static AtomicInteger getId() {
		return id;
	}

	public int getBuildId() {
		return buildId;
	}

	public String getName() {
		return name;
	}

	public String getLocation() {
		return location;
	}

	public List<Integer> getElevatorList() {
		return elevatorList;
	}

	public static void setId(AtomicInteger id) {
		Building.id = id;
	}

	public void setBuildId(int buildId) {
		this.buildId = buildId;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public void setElevatorList(List<Integer> elevatorList) {
		this.elevatorList = elevatorList;
	}
	
	

}
