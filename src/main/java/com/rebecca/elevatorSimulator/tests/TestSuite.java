package com.rebecca.elevatorSimulator.tests;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ GetAPITest.class, GetResponseTest.class, MediaTypeTest.class, PostAPITest.class, UpdateFloorTest.class,
		UpdateUserTest.class })
public class TestSuite {

}
