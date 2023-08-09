package bbear.coen6761.proj;
import java.awt.Font;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Arrays;

import javax.swing.*;

public class RobotDrawing {
    private int N; // Size of the floor
    private int[][] floor; // The floor array
    private int[] position; // Robot's initial position [row, col]
    private boolean penDown; // Robot's pen status
    private String direction; // Robot's initial direction
    private boolean firstMove; // Added variable
    private JTextArea outputArea = new JTextArea(20, 50);
    private boolean initialized = false;
    private boolean isShutdown = false;

    public static void main(String[] args) {
    	RobotDrawing rd = new RobotDrawing();
        SwingUtilities.invokeLater(() -> rd.createAndShowGUI());
    }

    public void createAndShowGUI() {
        JFrame frame = new JFrame("Robot Drawing");
        
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        
        outputArea.setEditable(false);
        // Set the font to monospaced
        outputArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scrollPane = new JScrollPane(outputArea);
        mainPanel.add(scrollPane);
        JTextField inputField = new JTextField(50);
        inputField.addActionListener(e -> {
            String command = inputField.getText();
            outputArea.append("CMD: "+command + "\n");
            processCommand(command);
            inputField.setText("");
        });
        mainPanel.add(inputField);

        frame.getContentPane().add(mainPanel);
        frame.pack();
        frame.setVisible(true);
    }

    public void processCommand(String command) {
    	if (command == null) {
    		outputError("Error: please enter a command whose value is not null.\n");
            return;
        }
		if (command.trim().isEmpty()) {
			//in case is empty
			outputError("Error: please enter a command whose value is not empty.\n");
			return;
		}
		
    	String cmd = command.toLowerCase().trim();
    	
        // Check if the command is 'q' first, regardless of system initialization status
        if (cmd.equals("q")) {
            // stop the program
            shutdown();
            return;
        }
    	if (!initialized && !cmd.startsWith("i")) {
    		outputError("Error: System not initialized. Please initialize "
            		+ "the system using the 'i' command before executing any other commands.\n");
            return;
        }
    	// An error message indicating the command does not accept additional characters or numbers.
        if (cmd.matches("^[udrlpcq].+")) {
        	outputError("Error: Command does not accept additional characters or numbers.\n");
            return;
        }
        
        switch(cmd.charAt(0)) {
        	case 'u':
        		setPenDown(false);
        		break;
        	case 'd':
        		setPenDown(true);
        		break;
        	case 'r':
        		turnRight();
        		break;
        	case 'l':
        		turnLeft();
        		break;
        	case 'm':
        		processMoveCommand(cmd);
        		break;
        	case 'p':
        		printFloor(outputArea);
        		break;
            case 'c':
                printCurrentPosition();
                break;
            case 'i':
                processInitializeCommand(cmd);
                break;
            default:
            	outputError("Error: Command not recognized.\n");
                break;
        }
    }
    
    private void processInitializeCommand(String command) {
	    String[] parts = command.split(" ");
	    if (parts[0].toLowerCase().equals("i") && parts.length == 2) {
	        try {
	            int size = Integer.parseInt(parts[1]);
	            if (size >= 0)
                {
	            	initializeSystem(size);
                }
                else
                {
                	outputError("The size of the array must be a positive number\n");
                }
	        } catch (NumberFormatException nfe) {
	        	outputError("The input format is not a number\n");
	        } catch (IllegalArgumentException e) {
	        	outputError(e.getMessage() + "\n");
            }
	    } else {
	    	outputError("Invalid initialize command: The input format is incorrect. "
                     + "The command and number should be separated by a space.\n");
	    }
	}
    
    private void outputError(String message) {
        outputArea.append(message);
    }

	private void processMoveCommand(String command) {
		String[] parts = command.split(" ");
		if (parts[0].toLowerCase().equals("m") && parts.length == 2) {
			 try {
	                int steps = Integer.parseInt(parts[1]);
	                if (steps >= 0){
	                	move(steps);
	                }
	                else{
	                	outputArea.append("The number of steps must be a positive number\n");
	                }
	            } catch (NumberFormatException nfe) {
	            	outputArea.append("The input format is not a number\n");
	            } catch (IllegalArgumentException e) {
	                outputArea.append(e.getMessage() + "\n");
	          }
		} else {
			 outputArea.append("Invalid move command: The input format is incorrect. "
                     + "The command and number should be separated by a space.\n");
        }
    };

	public void printCurrentPosition() {
		// Print current position, pen status and direction
		String penStatus = penDown ? "down" : "up";
		String directionFull;
		switch (direction) {
		    case "N":
		        directionFull = "North";
		        break;
		    case "S":
		        directionFull = "South";
		        break;
		    case "E":
		        directionFull = "East";
		        break;
		    case "W":
		        directionFull = "West";
		        break;
		    default:
		        directionFull = "Invalid Direction";
		}
		String formattedPosition = String.format("Position: %d, %d - Pen: %s - Facing: %s", position[1], position[0], 
				penStatus,directionFull);
		outputArea.append(formattedPosition + "\n");
	}

	// Move the robot forward a given number of steps
	public void move(Integer steps) throws IllegalArgumentException {
	    validateSteps(steps);

	    int[] finalPosition = calculateFinalPosition(steps);
	    validateFinalPosition(finalPosition);

	    for (int i = 0; i < steps; i++) {
	        moveOneStep();
	    }
	}

	private void validateSteps(Integer steps) {
	    if (steps == null) {
	        throw new IllegalArgumentException("Error: please enter a number of steps whose value is not null.");
	    } else if (steps == 0) {
	        throw new IllegalArgumentException("Error: please enter a number of steps whose value is not empty or zero.");
	    }
	}

	private int[] calculateFinalPosition(int steps) {
	    int[] finalPosition = Arrays.copyOf(position, position.length);
	    switch (direction) {
	        case "N": finalPosition[0] += steps; break;
	        case "S": finalPosition[0] -= steps; break;
	        case "W": finalPosition[1] -= steps; break;
	        case "E": finalPosition[1] += steps; break;
	        default:break;
	    }
	    return finalPosition;
	}

	private void validateFinalPosition(int[] finalPosition) {
	    if (finalPosition[0] < 0 || finalPosition[0] >= N || finalPosition[1] < 0 || finalPosition[1] >= N) {
	        throw new IllegalArgumentException("Robot can't move out of the board!");
	    }
	}

	private void moveOneStep() {
	    switch (direction) {
	        case "N": position[0]++; break;
	        case "S": position[0]--; break;
	        case "W": position[1]--; break;
	        case "E": position[1]++; break;
	        default:break;
	    }
	    if (penDown) {
	        floor[position[0]][position[1]] = 1; // Mark the floor with an asterisk
	    }
	}

    // Turn the robot 90 degrees to the right
    public void turnRight() {
        if (direction.equals("N")) {
            direction = "E";
        } else if (direction.equals("S")) {
            direction = "W";
        } else if (direction.equals("W")) {
            direction = "N";
        } else if (direction.equals("E")) {
            direction = "S";
        }
    }

    // Turn the robot 90 degrees to the left
    public void turnLeft() {
        if (direction.equals("N")) {
            direction = "W";
        } else if (direction.equals("S")) {
            direction = "E";
        } else if (direction.equals("W")) {
            direction = "S";
        } else if (direction.equals("E")) {
            direction = "N";
        }
    }

    // Print the floor with the drawing
    public void printFloor(JTextArea outputArea) {
    	 StringBuilder sb = new StringBuilder();
    	    // Start from the last row to flip the vertical axis
    	    for (int i = N - 1; i >= 0; i--) {
    	        sb.append(i).append(" "); // Print row index
    	        for (int j = 0; j < N; j++) {
    	            if (floor[i][j] == 1) {
    	                sb.append("* ");
    	            } else {
    	                sb.append("  ");
    	            }
    	        }
    	        sb.append("\n");
    	    }
    	    // Print column indexes(last line)
    	    sb.append("  "); // Offset for row indexes
    	    for (int i = 0; i < N; i++) {
    	        sb.append(i).append(" ");
    	    }
    	    sb.append("\n");
    	    outputArea.append(sb.toString());
    }

    public void initializeSystem(Integer size) {
    	if (size == null) {
	        throw new IllegalArgumentException("Error: please enter a size whose value is not null.");
	    }
    	else if (size == 0){
    		throw new IllegalArgumentException("Error: please enter a size whose value is not empty or zero.");
    	}
    	
        // Initialize size of the floor
        N = size;

        // Initialize the floor array
        floor = new int[N][N];

        // Initialize robot's position
        position = new int[] {0, 0};
        firstMove = true;

        // Lift the pen up
        penDown = false;

        // Initialize robot's direction facing North
        direction = "N";
        
        // System is initialized
        initialized = true;
    }

	public int getN() {
		return N;
	}

	public int[][] getFloor() {
		return floor;
	}

	public int[] getPosition() {
		return position;
	}

	public boolean isPenDown() {
		return penDown;
	}

	public String getDirection() {
		return direction;
	}

	public boolean isFirstMove() {
		return firstMove;
	}
	
	// : set the pen down and mark the current position
	public void setPenDown(boolean penDown) {
		this.penDown = penDown;
		if (penDown) {
			// If the pen is being set down, mark the current position on the floor
			floor[position[0]][position[1]] = 1;
		}
	}

	public JTextArea getOutputArea() {
		return outputArea;
	} 
	
	public boolean isInitialized() {
        return initialized;
    }
	
	public boolean isInShutdownState() {
	    return isShutdown;
	}
	
	// Call this when 'q' or 'Q' command is processed
	public void shutdown() {
	    // Perform any necessary cleanup here
	    // Then set the shutdown state
	    this.isShutdown = true;
	    System.exit(0);
	}
}
