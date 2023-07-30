package bbear.coen6761.proj.testing;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import bbear.coen6761.proj.RobotDrawing;

public class Dev11RobotDrawingTest {
	private RobotDrawing robot = new RobotDrawing();
	
	@Test
	public void InitializationTest() {
		robot.processCommand("i");
		String expectedOutput = "Invalid initialize command: The input format is incorrect. "
                + "The command and number should be separated by a space.\n";
		assertEquals(expectedOutput, robot.getOutputArea().getText());
	}

	@Test
	public void InitializationTest1() {
		robot.processCommand("p");
		String expectedOutput = "Error: System not initialized. Please initialize "
        		+ "the system using the 'i' command before executing any other commands.\n";
	assertEquals(expectedOutput, robot.getOutputArea().getText());
	}

 
	@Test
	public void InitializationTest2() {
		robot.processCommand("i 8");
		assertTrue(robot.isInitialized());
	}
	  
	@Test
	public void InitializationTest3() {
		robot.processCommand("");
		String expectedOutput = "Error: System not initialized. Please initialize "
        		+ "the system using the 'i' command before executing any other commands.\n";
		assertEquals(expectedOutput, robot.getOutputArea().getText());
	}
	
	@Test
	public void PenDownTest() {
		robot.processCommand("i 10");
		robot.processCommand("d");
		assertTrue(robot.isPenDown());
	}
	
	@Test
	public void PenUpTest() {
		robot.processCommand("i 10");
		robot.processCommand("u");
		assertFalse(robot.isPenDown());
	}
}