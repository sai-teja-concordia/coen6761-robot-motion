package bbear.coen6761.proj.testing;

import static org.junit.jupiter.api.Assertions.*;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import bbear.coen6761.proj.RobotDrawing;
public class RobotDrawingTest {

	private RobotDrawing rb = new RobotDrawing();
	
	// d
	@Test
	public void testPenDown() {
		// initialize system
		rb.processCommand("i 5");
		
		// d
		rb.processCommand("d");
		assertTrue(rb.isPenDown());
		
		// D
		rb.processCommand("D");
		assertTrue(rb.isPenDown());
	}
	// u
	@Test
	public void testPenUp() {
		// initialize system
		rb.processCommand("i 5");
		
		rb.processCommand("d");
		// u
		rb.processCommand("u");
		assertFalse(rb.isPenDown());
		
		rb.processCommand("d");
		// U
		rb.processCommand("U");
		assertFalse(rb.isPenDown());
	}
	
	// r
	@Test
	public void testTurnRight() {
		// initialize system
		rb.processCommand("i 5");
		
		// r 
        rb.processCommand("r");
        assertEquals("E", rb.getDirection());
        
        // R
        rb.processCommand("R");;
        assertEquals("S", rb.getDirection());
	}
	
	// l
	@Test
	public void testTurnLeft() {
		rb.processCommand("i 5");
        rb.processCommand("l");
        assertEquals("W", rb.getDirection());
        
        rb.processCommand("L");;
        assertEquals("S", rb.getDirection());
	}
	
	// m s
    @Test
    public void testMove() {
    	rb.processCommand("i 5");
        rb.processCommand("m 3");
        assertArrayEquals(new int[]{3, 0}, rb.getPosition());
        
        rb.processCommand("M 4");
        assertEquals("Robot can't move out of the board!\n", getLastMessageFromOutputArea());
    }
    
	// p
    @Test
    public void testPrintFloor() {
    	rb.processCommand("i 5");
    	rb.processCommand("d");
    	rb.processCommand("m 2");	
        rb.printFloor(rb.getOutputArea());
        String expectedOutput = 
            "4           \n" +
            "3           \n" +
            "2 *         \n" +
            "1 *         \n" +
            "0 *         \n" +
            "  0 1 2 3 4 \n";
        assertEquals(expectedOutput, rb.getOutputArea().getText());
    }
    
    // c
    @Test
    public void testPrintCurrentPosition() {
    	rb.processCommand("i 5");
    	rb.processCommand("r");
    	rb.processCommand("m 2");	
    	rb.processCommand("C");
         // setting the outputArea
    	String expectedOutput = "Position: 0, 2 - Pen: up - Facing: East\n";
    	assertEquals(expectedOutput, getLastMessageFromOutputArea());
    	
    	rb.processCommand("c");
    	assertEquals(expectedOutput, getLastMessageFromOutputArea());
    }
    
    // q
    @Test
    public void testQuitSystem() {
    	
    }
    
	// i 10
    @Test
    public void testInitializeSystem() {
        testInitializationWithCommand("i 10", 10);
        testInitializationWithCommand("I 12", 12);
    }
    
    private void testInitializationWithCommand(String command, int expectedSize) {
        // process the command
        rb.processCommand(command);
        
        // validate the size
        assertEquals(expectedSize, rb.getN());
        
        // validate the floor array
        int[][] floor = rb.getFloor();
        assertNotNull(floor);
        for (int[] row : floor) {
            for (int cell : row) {
                assertEquals(0, cell);
            }
        }
        
        // validate the position
        int[] expectedPosition = new int[]{0, 0};
        int[] actualPosition = rb.getPosition();
        assertNotNull(actualPosition);
        assertArrayEquals(expectedPosition, actualPosition);
        
        // validate the pen status and direction
        assertFalse(rb.isPenDown());
        assertEquals("N", rb.getDirection());
    }

    private String getLastMessageFromOutputArea() {
        String text = rb.getOutputArea().getText();
        String[] lines = text.split("\n");
        return lines[lines.length - 1] + "\n";
    }
    
    // Test the Process Method passing a null argument, it applies for R1, R2, R3, R4, R5, R6, R7, R8, R9
    @Test
    public void testProcess_NullArgument() {
    	rb.processCommand("i 5");
        rb.processCommand(null);
        String expectedOutput = "Error: please enter a command whose value is not null.\n";
    	assertEquals(expectedOutput, getLastMessageFromOutputArea());
    }
    
    // Test the Move Method passing a null argument, it applies for R1, R2, R3, R4, R5, R6, R7, R8, R9
    @Test
    public void testMove_NullArgument() {
    	rb.processCommand("i 5");

        String expectedErrorMessage = "Error: please enter a number of steps whose value is not null.";
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
        	rb.move(null);
        });

        assertEquals(expectedErrorMessage, exception.getMessage());
        //assertEquals(expectedOutput, getLastMessageFromOutputArea());
    }
    
    // Test the Initialize System Method passing a null argument, it applies for R1, R2, R3, R4, R5, R6, R7, R8, R9
    @Test
    public void testInitializeSystem_NullArgument() {
        String expectedErrorMessage = "Error: please enter a size whose value is not null.";
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
        	rb.initializeSystem(null);
        });

        assertEquals(expectedErrorMessage, exception.getMessage());
    }
    
    // Test the Process Method passing an empty argument, it applies for R1, R2, R3, R4, R5, R6, R7, R8, R9
    @Test
    public void testProcess_EmptyArgument() {
        rb.processCommand("i 5");
        rb.processCommand("");
        String expectedOutput = "Error: please enter a command whose value is not empty.\n";
    	assertEquals(expectedOutput, getLastMessageFromOutputArea());
    	
    }
    
    // Test the Move Method passing an empty argument, it applies for R1, R2, R3, R4, R5, R6, R7, R8, R9
    @Test
    public void testMove_EmptyArgument() {
    	rb.processCommand("i 5");
    	
        String expectedErrorMessage = "Error: please enter a number of steps whose value is not empty or zero.";
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
        	rb.move(0);
        });

        assertEquals(expectedErrorMessage, exception.getMessage());
    }
    
    // Test the Initialize System Method passing an empty argument, it applies for R1, R2, R3, R4, R5, R6, R7, R8, R9
    @Test
    public void testInitializeSystem_EmptyArgument() {
    	String expectedErrorMessage = "Error: please enter a size whose value is not empty or zero.";
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
        	rb.initializeSystem(0);
        });

        assertEquals(expectedErrorMessage, exception.getMessage());
    }
    
    // Test the Process Method passing an non-numeric argument (commands (M s or m s) and (I n or i n)), it applies for R1, R2, R3, R4, R5, R6, R7, R8, R9
    @Test
    public void testProcess_NonNumericFormatSecondArgument() {
        rb.processCommand("i 30");
        String expectedOutput = "The input format is not a number\n";
        
        rb.processCommand("m f");
        System.out.println(getLastMessageFromOutputArea());
    	assertEquals(expectedOutput, getLastMessageFromOutputArea());
    	
    	rb.processCommand("M c");
    	System.out.println(getLastMessageFromOutputArea());
    	assertEquals(expectedOutput, getLastMessageFromOutputArea());
    	
    	rb.processCommand("i x");
    	System.out.println(getLastMessageFromOutputArea());
     	assertEquals(expectedOutput, getLastMessageFromOutputArea());
     	
     	rb.processCommand("I y");
     	System.out.println(getLastMessageFromOutputArea());
     	assertEquals(expectedOutput, getLastMessageFromOutputArea());
     	
    }
    
    // Test the Process Method passing a negative number (commands (M s or m s) and (I n or i n)), it applies for R1, R2, R3, R4, R5, R6, R7, R8, R9
    @Test
    public void testProcess_NonNegativeSecondArgument() {
        rb.processCommand("i 40");
        String expectedOutput1 = "The number of steps must be a positive number\n";
        String expectedOutput2 = "The size of the array must be a positive number\n";
        
        rb.processCommand("m -4");
        System.out.println(getLastMessageFromOutputArea());
    	assertEquals(expectedOutput1, getLastMessageFromOutputArea());
    	
    	rb.processCommand("M -10");
    	System.out.println(getLastMessageFromOutputArea());
    	assertEquals(expectedOutput1, getLastMessageFromOutputArea());
    	
    	rb.processCommand("i -6");
    	System.out.println(getLastMessageFromOutputArea());
     	assertEquals(expectedOutput2, getLastMessageFromOutputArea());
     	
     	rb.processCommand("I -20");
     	System.out.println(getLastMessageFromOutputArea());
     	assertEquals(expectedOutput2, getLastMessageFromOutputArea());
     	
    }
    
    // q
    @Test
    public void testQuitSystem() {
    	rb.processCommand("i 5");
    	  
    	rb.processCommand("q");
    	assertTrue(rb.getOutputArea().getRootPane().getParent() == null);
		
		rb.processCommand("Q");
		assertTrue(rb.getOutputArea().getRootPane().getParent() == null);
    }
}
