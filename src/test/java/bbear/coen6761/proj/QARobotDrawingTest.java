package bbear.coen6761.proj;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * The following test cases are written by the QA Team.
 */
public class QARobotDrawingTest {
	private RobotDrawing robot = new RobotDrawing();

	
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
	
	@DisplayName("TC 1 User presses Enter key")
	@Test
	public void initializationTest3() {
		robot.processCommand("");
		String expectedOutput = "Error: System not initialized. Please initialize "
        		+ "the system using the 'i' command before executing any other commands.\n";
		assertEquals(expectedOutput, robot.getOutputArea().getText());
	}
	
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
	
	@DisplayName("TC 5 User inputs <z> ")
	@Test
	public void invalidInputTest() {
		robot.initializeSystem(10);
		robot.processCommand("z");
		String expected = "Error: Command not recognized.\n";
		assertEquals(expected, robot.getOutputArea().getText());

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



	@DisplayName("TC 8 The robot should rotate to its right.")
	@Test
	public void rotateRightTest() {
		initializeSystemAndAssert(10);
		robot.processCommand("r");
		assertEquals("E", robot.getDirection());
		robot.processCommand("R");
		assertEquals("S", robot.getDirection());
		robot.processCommand("r");
		assertEquals("W", robot.getDirection());
		robot.processCommand("R");
		assertEquals("N", robot.getDirection());

	}

	@DisplayName("TC 9 The robot should rotate to its left.")
	@Test
	public void rotateLeftTest() {
		initializeSystemAndAssert(10);
		robot.processCommand("l");
		assertEquals("W", robot.getDirection());
		robot.processCommand("l");
		assertEquals("S", robot.getDirection());
		robot.processCommand("l");
		assertEquals("E", robot.getDirection());
		robot.processCommand("l");
		assertEquals("N", robot.getDirection());
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
		robot.printCurrentPosition();
		String expectedOutput = "Position: 5, 0 - Pen: up - Facing: East\n";
		assertEquals(expectedOutput, robot.getOutputArea().getText());
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
		robot.processCommand("p");
	}


	@DisplayName("TC 15 : Print Robot Position (Pen Up).")
	@Test
	public void robotPositionPenUp() {
		initializeSystemAndAssert(10);
		robot.processCommand("r");
		robot.move(4);
		robot.processCommand("l");
		robot.move(2);
		robot.printCurrentPosition();
		String expectedOutput = "Position: 4, 2 - Pen: up - Facing: North\n";
		assertEquals(expectedOutput, robot.getOutputArea().getText());
	}


	@DisplayName("TC 16 : Print Robot Position (Pen Down).")
	@Test
	public void robotPositionPenDown() {
		initializeSystemAndAssert(10);
		robot.processCommand("r");
		robot.processCommand("d");
		robot.move(4);
		robot.processCommand("l");
		robot.move(2);
		robot.processCommand("r");
		robot.printCurrentPosition();
		String expectedOutput = "Position: 4, 2 - Pen: down - Facing: East\n";
		assertEquals(expectedOutput, robot.getOutputArea().getText());
	}
	
	@DisplayName("The Robot Position should be correctly printed while facing West Direction")
	@Test
	public void printPositionTestWest() {
		initializeSystemAndAssert(10);
		robot.processCommand("l");
		robot.printCurrentPosition();
		String expectedOutput = "Position: 0, 0 - Pen: up - Facing: West\n";
		assertEquals(expectedOutput, robot.getOutputArea().getText());
	}
	
	@DisplayName("The Robot Position should be correctly printed while facing South Direction")
	@Test
	public void printPositionTestSouth() {
		initializeSystemAndAssert(10);
		robot.processCommand("l");
		robot.processCommand("l");
		robot.printCurrentPosition();
		String expectedOutput = "Position: 0, 0 - Pen: up - Facing: South\n";
		assertEquals(expectedOutput, robot.getOutputArea().getText());
	}
	
	
	@DisplayName("Test move method with 0 steps.")
	@Test
	public void moveTestStep0() {
		Exception exception = assertThrows(IllegalArgumentException.class , () -> robot.move(0));
		assertEquals("Error: please enter a number of steps whose value is not empty or zero.", exception.getMessage());
		
	}

	@DisplayName("Test move method with null steps.")
	@Test
	public void moveTestStepNull() {
		Exception exception = assertThrows(IllegalArgumentException.class , () -> robot.move(null));
		assertEquals("Error: please enter a number of steps whose value is not null.", exception.getMessage());
	}
	
	private int[] getInitialRobotPosition() {
		return new int[]{0,0};
	}
}