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

}

