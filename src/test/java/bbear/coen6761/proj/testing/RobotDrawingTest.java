package bbear.coen6761.proj.testing;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.security.Permission;

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
        
        rb.getOutputArea().setText("");
    	rb.processCommand("u");
    	rb.processCommand("m 2");	
        rb.printFloor(rb.getOutputArea());
        String expectedOutput2 = 
            "4           \n" +
            "3           \n" +
            "2 *         \n" +
            "1 *         \n" +
            "0 *         \n" +
            "  0 1 2 3 4 \n";
        assertEquals(expectedOutput2, rb.getOutputArea().getText());
    }
    
    
    // Test with command “p” without first initializing the system.
    @Test 
    public void testOtherCMDWithouInitSys() {
         rb.processCommand("p");
         assertEquals("Error: System not initialized. Please initialize "
         		+ "the system using the 'i' command before executing any other commands.\n", getLastMessageFromOutputArea());
    }
    // c
    @Test
    public void testPrintCurrentPosition() {
    	rb.processCommand("i 5");
    	rb.processCommand("r");
    	rb.processCommand("m 2");	
    	rb.processCommand("C");
         // setting the outputArea
    	String expectedOutput = "Position: 2, 0 - Pen: up - Facing: East\n";
    	assertEquals(expectedOutput, getLastMessageFromOutputArea());
    	
    	rb.processCommand("c");
    	assertEquals(expectedOutput, getLastMessageFromOutputArea());
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
    @SuppressWarnings("removal")
	@Test
    public void testQuitSystem() {    	  
		
	    // Set a security manager that prevents exit
	    System.setSecurityManager(new NoExitSecurityManager());

	    // Run processCommand and expect a SecurityException
	    assertThrows(SecurityException.class, () -> {
	        rb.processCommand("q");
	    });
	    assertTrue(rb.isInShutdownState());

	    
	    // Reset the security manager
	    System.setSecurityManager(null);
	    
	    
	    // Set a security manager that prevents exit
	    System.setSecurityManager(new NoExitSecurityManager());
	    assertThrows(SecurityException.class, () -> {
	        rb.processCommand("Q");
	    });
	    assertTrue(rb.isInShutdownState());
	    // Reset the security manager
	    System.setSecurityManager(null);
    }
    
    private static class NoExitSecurityManager extends SecurityManager {
        @Override
        public void checkPermission(Permission perm) {
            // allow anything.
        }
        @Override
        public void checkPermission(Permission perm, Object context) {
            // allow anything.
        }
        @Override
        public void checkExit(int status) {
            super.checkExit(status);
            throw new SecurityException("System exit not allowed in tests");
        }
    }
    
    // Test with "u", "d", "r", "l", "p", "c", "q" commands that the system does not accept any characters or numbers following the command.
    @Test
    public void testInvalidSingleCharacterCommands() {
    	rb.processCommand("i 5");
    	rb.processCommand("u1");
    	assertEquals("Error: Command does not accept additional characters or numbers.\n", getLastMessageFromOutputArea());
    }
    
    // Test with commands where the first character (lowercase) is not in [“u”,”d”,”r”,”l”,”m”,”p”,”c”,”q”,”i”].
    @Test
    public void testFirstCharOfCMDNotRecognized() {
    	rb.processCommand("i 5");
    	rb.processCommand("w");
    	assertEquals("Error: Command not recognized.\n", getLastMessageFromOutputArea());
    }
    
    // Test with "m" and "i" commands that system only accepts inputs where the second character is a space.
    @Test
    public void testMoveCommandRequiresSpaceAfterMorI() {
    	rb.processCommand("i 5");
    }
    
}
