package bbear.coen6761.proj.testing;

import static org.junit.jupiter.api.Assertions.*;

import javax.swing.JTextArea;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import bbear.coen6761.proj.RobotDrawing;
public class RobotDrawingTest {

	private RobotDrawing rb;

	@BeforeEach
	public void setup() {
		rb = new RobotDrawing();
		rb.initializeSystem(5);
	}
	
	// d
	@Test
	public void testPenDown() {
		rb.processCommand("d");
		assertTrue(rb.isPenDown());
		rb.processCommand("D");
		assertTrue(rb.isPenDown());
	}
	
	// u
	@Test
	public void testPenUp() {
		rb.processCommand("u");
		assertFalse(rb.isPenDown());
		rb.processCommand("U");
		assertFalse(rb.isPenDown());
	}
	
	// r
	@Test
	public void testTurnRight() {
        rb.processCommand("r");
        assertEquals("E", rb.getDirection());
        rb.processCommand("R");;
        assertEquals("S", rb.getDirection());
	}
	
	// l
	@Test
	public void testTurnLeft() {
        rb.processCommand("l");
        assertEquals("W", rb.getDirection());
        rb.processCommand("L");;
        assertEquals("S", rb.getDirection());
	}
	
	// m s
    @Test
    public void testMove() {
        rb.processCommand("m 3");
        assertArrayEquals(new int[]{3, 0}, rb.getPosition());
        assertThrows(IllegalArgumentException.class, ()-> rb.processCommand("M 6"));
        // Here we might want to also test that floor was marked
    }
    
	// p
    @Test
    public void testPrintFloor() {
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
    	rb.processCommand("r");
    	rb.processCommand("m 2");	
    	rb.printCurrentPosition();
         // setting the outputArea
    	String expectedOutput = "Position: 0, 2 - Pen: up - Facing: East\n";
    	assertEquals(expectedOutput, rb.getOutputArea().getText());
    }
    
    // q
    @Test
    public void testQuitSystem() {
    	
    }
    
	// i 10
    @Test
    public void testInitializeSystem() {
        rb.processCommand("i 10");
        assertEquals(10, rb.getN());
        assertNotNull(rb.getFloor());
        assertNotNull(rb.getPosition());
        assertFalse(rb.isPenDown());
        assertEquals("N", rb.getDirection());
        rb.processCommand("i 0");
        assertEquals(10, rb.getN());
        assertNotNull(rb.getFloor());
        assertNotNull(rb.getPosition());
        assertFalse(rb.isPenDown());
        assertEquals("N", rb.getDirection());
    }


}

