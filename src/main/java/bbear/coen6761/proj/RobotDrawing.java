package bbear.coen6761.proj;
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

    public static void main(String[] args) {
    	RobotDrawing rd = new RobotDrawing();
        SwingUtilities.invokeLater(() -> rd.createAndShowGUI());
    }

    public void createAndShowGUI() {
        JFrame frame = new JFrame("Robot Drawing");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        
//        outputArea = new JTextArea(20, 50);
        outputArea.setEditable(false);
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
    	
    	if (!initialized && !command.toLowerCase().startsWith("i")) {
            outputArea.append("Error: System not initialized. Please initialize the system using the 'i' command before executing any other commands.\n");
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
		else if (command.toLowerCase().startsWith("m")) {
			String[] parts = command.split(" ");
			if (parts.length == 2) {
				 try {
		                int steps = Integer.parseInt(parts[1]);
		                move(steps);
		            } catch (NumberFormatException e) {
		                System.err.println("Exception: " + e.getMessage());
		            } catch (IllegalArgumentException e) {
		                outputArea.append(e.getMessage() + "\n");
		          }
			} else {
	                System.err.println("Invalid move command: " + command);
	        }
		}
		else if (command.toLowerCase().equals("p")) {
            printFloor(outputArea);
		}
		else if (command.toLowerCase().equals("c")) {
	        printCurrentPosition();
		}
		else if (command.toLowerCase().equals("q")) {
			// stop the program
			System.exit(0);
		}
		else if (command.toLowerCase().startsWith("i")) {
		    String[] parts = command.split(" ");
		    if (parts.length == 2) {
		        try {
		            // n is the size of the array
		            int size = Integer.parseInt(parts[1]);
		            initializeSystem(size);
		        } catch (NumberFormatException e) {
		            System.err.println("Exception: " + e.getMessage());
		        } catch (IllegalArgumentException e) {
	                outputArea.append(e.getMessage() + "\n");
	            }
		    } else {
		        System.err.println("Invalid command: " + command);
		    }
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
		String formattedPosition = String.format("Position: %d, %d - Pen: %s - Facing: %s", position[0], position[1], penStatus,directionFull);
		outputArea.append(formattedPosition + "\n");
	}

    // Move the robot forward a given number of steps
	public void move(int steps) throws IllegalArgumentException {
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
    	    // Print column indexes
    	    sb.append("  "); // Offset for row indexes
    	    for (int i = 0; i < N; i++) {
    	        sb.append(i).append(" ");
    	    }
    	    sb.append("\n");
    	    outputArea.append(sb.toString());
    }
    
    public void initializeSystem(int size) {
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
}

