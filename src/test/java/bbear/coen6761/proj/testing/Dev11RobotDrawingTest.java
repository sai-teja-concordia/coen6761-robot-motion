package bbear.coen6761.proj.testing;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import bbear.coen6761.proj.RobotDrawing;
import org.junit.jupiter.api.DisplayName;


public class Dev11RobotDrawingTest {
	private RobotDrawing robot = new RobotDrawing();
	
	@DisplayName("TC 2 User inputs <i> without defining size of floor")
	@Test
	public void initializationTest() {
		robot.processCommand("i");
		String expectedOutput = "Invalid initialize command: The input format is incorrect. "
                + "The command and number should be separated by a space.\n";
		assertEquals(expectedOutput, robot.getOutputArea().getText());
	}

	@DisplayName("TC 3 User inputs <p> without initiliazing the system")
	@Test
	public void initializationTest1() {
		robot.processCommand("p");
		String expectedOutput = "Error: System not initialized. Please initialize "
        		+ "the system using the 'i' command before executing any other commands.\n";
	assertEquals(expectedOutput, robot.getOutputArea().getText());
	}

	@DisplayName("TC 4 User inputs <I 8> to initiliaze the array")
	@Test
	public void initializationTest2() {
		robot.processCommand("i 8");
		assertTrue(robot.isInitialized());
	}

	@DisplayName("TC 1 User presses Enter key")
	@Test
	public void initializationTest3() {
		robot.processCommand("");
		String expectedOutput = "Error: System not initialized. Please initialize "
        		+ "the system using the 'i' command before executing any other commands.\n";
		assertEquals(expectedOutput, robot.getOutputArea().getText());
	}
	@DisplayName("TC 6 User inputs <d> or <D> to keep pen down")
	@Test
	public void PenDownTest() {
		initializeSystemAndAssert(10);
		robot.processCommand("d");
		assertTrue(robot.isPenDown());
	}
	
	@DisplayName("TC 7 User inputs <u> or <U> to keep pen up")
	@Test
	public void PenUpTest() {
		//robot.processCommand("i 10");
		initializeSystemAndAssert(10);
		robot.processCommand("u");
		assertFalse(robot.isPenDown());
	}

	@DisplayName("TC 5 User inputs <z> ")
	@Test
	public void invalidInputTest() {
		robot.initializeSystem(10);
		robot.processCommand("z");
		String expected = "Error: Command not recognized.\n";
		assertEquals(expected, robot.getOutputArea().getText());

	}

	@DisplayName("TC 8 From the Direction North, the User inputs <r> or <R> expected direction is East")
	@Test
	public void rotateRightTest() {
		initializeSystemAndAssert(10);
		robot.processCommand("r");
		String expected = "E";
		assertEquals(expected, robot.getDirection());

	}

	@DisplayName("TC 9 From the Direction West, the User inputs <l> or <L> expected direction is South")
	@Test
	public void rotateLeftTest() {
		initializeSystemAndAssert(10);
		robot.processCommand("l");
		robot.processCommand("l");
		String expected = "S";
		assertEquals(expected, robot.getDirection());
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
		assertArrayEquals(getInitialRobotPosition(),robot.getPosition());
	}

	@DisplayName("TC 10 : Invalid robot movement with the command - <m y>")
	@Test
	public void invalidMovementTest() {
		initializeSystemAndAssert(10);
		robot.processCommand("m y");
		assertEquals("The input format is not a number\n", robot.getOutputArea().getText());
	}

	@DisplayName("TC 11 : Invalid robot movement with the command - <m>")
	@Test
	public void invalidMovementTest2() {
		initializeSystemAndAssert(10);
		robot.processCommand("m");
		assertEquals("Invalid move command: The input format is incorrect. The command and number should be separated by a space.\n", robot.getOutputArea().getText());
	}

	@DisplayName("TC 12 : Valid robot movement with the command - <M 5>")
	@Test
	public void robotValidMovement() {
		initializeSystemAndAssert(10);
		robot.turnRight();
		robot.processCommand("M 5");
		assertArrayEquals(new int[]{5,0}, robot.getPosition());
	}

	@DisplayName("TC 13 : Out of the boundary robot movement")
	@Test
	public void robotOutOfTheBoundaryMovement() {
		initializeSystemAndAssert(10);
		Exception exception = assertThrows(IllegalArgumentException.class , () -> robot.move(15));
		assertEquals("Robot can't move out of the board!", exception.getMessage());
		assertArrayEquals(getInitialRobotPosition(), robot.getPosition());
	}

	@DisplayName("TC 14 : Out of the boundary robot movement")
	@Test
	public void robotOutOfTheBoundaryMovement2() {
		initializeSystemAndAssert(10);
		robot.processCommand("r");
		robot.move(3);
		robot.processCommand("l");
		robot.move(4);
		robot.processCommand("l");
		Exception exception = assertThrows(IllegalArgumentException.class , () -> robot.move(4));
		assertEquals("Robot can't move out of the board!", exception.getMessage());
		assertArrayEquals(new int[]{4,3}, robot.getPosition());
	}

	private int[] getInitialRobotPosition() {
		return new int[]{0,0};
	}
}