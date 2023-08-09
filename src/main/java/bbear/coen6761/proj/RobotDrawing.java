package bbear.coen6761.proj;
import java.awt.Font;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

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
        
//        outputArea = new JTextArea(20, 50);
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
//            printFloor(outputArea);
        });
        mainPanel.add(inputField);

        frame.getContentPane().add(mainPanel);
        frame.pack();
        frame.setVisible(true);
    }

    public void processCommand(String command) {
    	if (command == null) {
    		outputArea.append("Error: please enter a command whose value is not null.\n");
            return;
        }
        // Check if the command is 'q' first, regardless of system initialization status
        if (command.toLowerCase().equals("q")) {
            // stop the program
            shutdown();
            return;
        }
    	if (!initialized && !command.toLowerCase().startsWith("i")) {
            outputArea.append("Error: System not initialized. Please initialize "
            		+ "the system using the 'i' command before executing any other commands.\n");
            return;
        }
    	// An error message indicating the command does not accept additional characters or numbers.
        if (command.toLowerCase().matches("^[udrlpcq].+")) {
            outputArea.append("Error: Command does not accept additional characters or numbers.\n");
            return;
        }
	    if (command.toLowerCase().equals("u")) {
	    	setPenDown(false);
	    }
	    else if (command.toLowerCase().equals("d")) {
	    	setPenDown(true);
	    }
		else if (command.toLowerCase().equals("r")) {
			turnRight();
		}
		else if (command.toLowerCase().equals("l")) {
			turnLeft();
		}
//	    "m  3" => [m,"",3]
		else if (command.toLowerCase().startsWith("m")) {
			String[] parts = command.split(" ");
			if (parts[0].toLowerCase().equals("m") && parts.length == 2) {
				 try {
		                int steps = Integer.parseInt(parts[1]);
		                if (steps >= 0)
		                {
		                	move(steps);
		                }
		                else
		                {
		                	outputArea.append("The number of steps must be a positive number\n");
		                }
		            } catch (NumberFormatException nfe) {
		                //System.err.println("Exception: " + nfe.getMessage());
		            	outputArea.append("The input format is not a number\n");
		            } catch (IllegalArgumentException e) {
		                outputArea.append(e.getMessage() + "\n");
		          }
			} else {
				 outputArea.append("Invalid move command: The input format is incorrect. "
                         + "The command and number should be separated by a space.\n");
	        }
		}
		else if (command.toLowerCase().equals("p")) {
            printFloor(outputArea);
		}
		else if (command.toLowerCase().equals("c")) {
	        printCurrentPosition();
		}
		else if (command.toLowerCase().startsWith("i")) {
		    String[] parts = command.split(" ");
		    if (parts[0].toLowerCase().equals("i") && parts.length == 2) {
		        try {
		            // n is the size of the array
		            int size = Integer.parseInt(parts[1]);
		            if (size >= 0)
	                {
		            	initializeSystem(size);
	                }
	                else
	                {
	                	outputArea.append("The size of the array must be a positive number\n");
	                }
		        } catch (NumberFormatException nfe) {
		            //System.err.println("Exception: " + e.getMessage());
		        	outputArea.append("The input format is not a number\n");
		        } catch (IllegalArgumentException e) {
	                outputArea.append(e.getMessage() + "\n");
	            }
		    } else {
		    	 outputArea.append("Invalid initialize command: The input format is incorrect. "
                         + "The command and number should be separated by a space.\n");
		    }
		}
		else if (command.trim().isEmpty())
		{
			//in case is empty
			outputArea.append("Error: please enter a command whose value is not empty.\n");
		}
	    // Handle commands that start with an unexpected character
	    else if (!command.toLowerCase().matches("^[udrlmpcqi].*")) {
	        outputArea.append("Error: Command not recognized.\n");
	        return;
	    }
    }

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
		if (steps == null) {
	        throw new IllegalArgumentException("Error: please enter a number of steps whose value is not null.");
	    }
		else if (steps == 0) {
			throw new IllegalArgumentException("Error: please enter a number of steps whose value is not empty or zero.");
		}
		
	    for (int i = 0; i < steps; i++) {
	        // Store next position
	        int[] nextPosition = new int[]{position[0], position[1]};
	        
	        if (firstMove && penDown) {
	             floor[position[0]][position[1]] = 1; 
	             firstMove = false; // Reset the flag
	        }
	        
	        if (direction.equals("N")) {
	            nextPosition[0]++;
	        } else if (direction.equals("S")) {
	            nextPosition[0]--;
	        } else if (direction.equals("W")) {
	            nextPosition[1]--;
	        } else if (direction.equals("E")) {
	            nextPosition[1]++;
	        }

	        // Check if next position is valid
	        if (nextPosition[0] < 0 || nextPosition[0] >= N || nextPosition[1] < 0 || nextPosition[1] >= N) {
	            throw new IllegalArgumentException("Robot can't move out of the board!");
	        }

	        // Update position
	        position = nextPosition;

	        if (penDown) {
	            floor[position[0]][position[1]] = 1; // Mark the floor with an asterisk
	        }
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
	
	public void setPenDown(boolean penDown) {
		this.penDown = penDown;
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
