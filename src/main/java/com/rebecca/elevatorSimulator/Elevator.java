package com.rebecca.elevatorSimulator;

import java.util.concurrent.atomic.AtomicInteger;

public class Elevator {
	
	private static AtomicInteger id  = new AtomicInteger(1);
	
	private int elevatorId;
	private String name;
	private int currentFloor;
	private int[] floorList;
	private String state;
	
	public Elevator(String name, int currentFloor, int[] floorList) {
		elevatorId = id.getAndIncrement();
		this.name = name;
		this.currentFloor = currentFloor;
		this.floorList = floorList;
	}
	
	public Elevator() {
		elevatorId = id.getAndIncrement();
	}
	
	//list of constants/predefined list of values
	enum StateofElevator {
        UP, DOWN, STOPPED, OUT_OF_SERVICE
    }
	
	public String getState() {
		return state;
	}
	
	public void setState(String state) {
		this.state = state;
	}

	public static AtomicInteger getId() {
		return id;
	}

	public int getElevatorId() {
		return elevatorId;
	}

	public String getName() {
		return name;
	}

	public int getCurrentFloor() {
		return currentFloor;
	}

	public int[] getFloorList() {
		return floorList;
	}

	public static void setId(AtomicInteger id) {
		Elevator.id = id;
	}

	public void setElevatorId(int elevatorId) {
		this.elevatorId = elevatorId;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setCurrentFloor(int currentFloor) {
		this.currentFloor = currentFloor;
	}

	public void setFloorList(int[] floorList) {
		this.floorList = floorList;
	}
	
	
}
