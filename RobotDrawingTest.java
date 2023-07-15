package bbear.coen6761.proj.testing;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import bbear.coen6761.proj.RobotDrawing;

import static org.junit.jupiter.api.Assertions.*;

class RobotDrawingTest {
    private RobotDrawing robot;

    @BeforeEach
    void setUp() {
        robot = new RobotDrawing();
    }

    @Test
    void testPenStatusInitiallyDown() {
        assertFalse(robot.isPenDown(), "Pen status should be initially down");
    }

    @Test
    void testPenUpCommand() {
        RobotDrawing.processCommand("u");
        assertFalse(robot.isPenDown(), "Pen status should be up after 'u' command");
    }

    @Test
    void testPenDownCommand() {
        RobotDrawing.processCommand("d");
        assertTrue(robot.isPenDown(), "Pen status should be down after 'd' command");
    }

    @Test
    void testPenUpDownSequence() {
        assertFalse(robot.isPenDown(), "Pen status should be initially down");

        RobotDrawing.processCommand("u");
        assertFalse(robot.isPenDown(), "Pen status should be up after 'u' command");

        RobotDrawing.processCommand("d");
        assertTrue(robot.isPenDown(), "Pen status should be down after 'd' command");
    }

    @Test
    void testPenStatusEffectOnFloorDrawing() {
        RobotDrawing.initializeSystem(5);
        RobotDrawing.processCommand("d"); // Pen down
        RobotDrawing.processCommand("m 3"); // Move 3 steps
        int[][] floorDrawing = robot.getFloorDrawing();

        // Verify that the floor drawing is updated where the pen is down
        for (int i = 0; i < 3; i++) {
            assertEquals(1, floorDrawing[i][0], "Floor drawing should be updated when the pen is down");
        }

        RobotDrawing.processCommand("u"); // Pen up
        RobotDrawing.processCommand("m 2"); // Move 2 steps
        floorDrawing = robot.getFloorDrawing();

        // Verify that the floor drawing is not updated where the pen is up
        for (int i = 3; i < 5; i++) {
            assertEquals(0, floorDrawing[i][0], "Floor drawing should not be updated when the pen is up");
        }
    }
}
