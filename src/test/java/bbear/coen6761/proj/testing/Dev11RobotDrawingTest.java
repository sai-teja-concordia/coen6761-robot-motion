package bbear.coen6761.proj.testing;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import bbear.coen6761.proj.RobotDrawing;

public class Dev11RobotDrawingTest {
	private RobotDrawing robot = new RobotDrawing();
	
	@Test
	public void initializationTest() {
		robot.processCommand("i");
		String expectedOutput = "Invalid initialize command: The input format is incorrect. "
                + "The command and number should be separated by a space.\n";
		assertEquals(expectedOutput, robot.getOutputArea().getText());
	}

	@Test
	public void initializationTest1() {
		robot.processCommand("p");
		String expectedOutput = "Error: System not initialized. Please initialize "
        		+ "the system using the 'i' command before executing any other commands.\n";
	assertEquals(expectedOutput, robot.getOutputArea().getText());
	}

 
	@Test
	public void initializationTest2() {
		robot.processCommand("i 8");
		assertTrue(robot.isInitialized());
	}
	  
	@Test
	public void initializationTest3() {
		robot.processCommand("");
		String expectedOutput = "Error: System not initialized. Please initialize "
        		+ "the system using the 'i' command before executing any other commands.\n";
		assertEquals(expectedOutput, robot.getOutputArea().getText());
	}

	@Test
	public void PenDownTest() {
		initializeSystemAndAssert(10);
		robot.processCommand("d");
		assertTrue(robot.isPenDown());
	}
	
	@Test
	public void PenUpTest() {
		//robot.processCommand("i 10");
		initializeSystemAndAssert(10);
		robot.processCommand("u");
		assertFalse(robot.isPenDown());
	}

	@Test
	public void invalidInputTest() {
		robot.initializeSystem(10);
		robot.processCommand("z");
		String expected = "Error: Command not recognized.\n";
		assertEquals(expected, robot.getOutputArea().getText());
	}
	
	
	public void initializeSystemAndAssert(int size){

		robot.initializeSystem(size);
		assertTrue(robot.isInitialized());
		assertEquals(size, robot.getFloor().length);
		for (int[] row : robot.getFloor()) {
			assertEquals(size, row.length);
		}
		assertFalse(robot.isPenDown());
		assertEquals("N",robot.getDirection());
		assertArrayEquals(new int[]{0,0},robot.getPosition());
	}
}