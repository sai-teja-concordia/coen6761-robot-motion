package bbear.coen6761.proj.testing;

import static org.junit.jupiter.api.Assertions.*;

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
	
	// u
	@Test
	public void testPenUp() {
	}
	
	// d
	@Test
	public void testPenDown() {
		
	}
	
	
	// r
	@Test
	public void testTurnRight() {
        rb.turnRight();
        assertEquals("E", rb.getDirection());
	}
	
	// l
	@Test
	public void testTurnLeft() {
        rb.turnLeft();
        assertEquals("W", rb.getDirection());
	}
	
	// m 3
    @Test
    public void testMove() {
        rb.processCommand("d");
        rb.move(3);
        assertArrayEquals(new int[]{0, 3}, rb.getPosition());
        // Here we might want to also test that floor was marked
    }
	
    // p
    @Test
    public void testPrintCurrentPosition() {
    	
    }
    
    // q
    @Test
    public void testQuitSystem() {
    	
    }
    
	// i 10
    @Test
    public void testInitializeSystem() {
        rb.initializeSystem(10);
        assertEquals(10, rb.getN());
        assertNotNull(rb.getFloor());
        assertNotNull(rb.getPosition());
        assertFalse(rb.isPenDown());
        assertEquals("N", rb.getDirection());
    }


}

