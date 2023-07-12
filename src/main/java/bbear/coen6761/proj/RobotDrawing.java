package bbear.coen6761.proj;
import javax.swing.*;

public class RobotDrawing {
    private static int N; // Size of the floor

    private static int[][] floor; // The floor array
    private static int[] position; // Robot's initial position [row, col]
    private static boolean penDown; // Robot's pen status
    private static String direction; // Robot's initial direction
    private static boolean firstMove; // Added variable
    
    private static JTextArea outputArea;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> createAndShowGUI());
    }

    private static void createAndShowGUI() {
        JFrame frame = new JFrame("Robot Drawing");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        
        outputArea = new JTextArea(20, 50);
        outputArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(outputArea);
        mainPanel.add(scrollPane);

        JTextField inputField = new JTextField(50);
        inputField.addActionListener(e -> {
            String command = inputField.getText();
            processCommand(command);
            inputField.setText("");
            
            outputArea.append("CMD: "+command + "\n");
//            printFloor(outputArea);

        });
        mainPanel.add(inputField);

        frame.getContentPane().add(mainPanel);
        frame.pack();
        frame.setVisible(true);
    }

    private static void processCommand(String command) {
	    if (command.toLowerCase().equals("u")) {
	    	penDown = false;
	    }
	    else if (command.toLowerCase().equals("d")) {
	    	penDown = true;
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
	            }
			} else {
	                System.err.println("Invalid move command: " + command);
	        }
		}
		else if (command.toLowerCase().equals("p")) {
            printFloor(outputArea);
		}
		else if (command.toLowerCase().equals("c")) {
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
		        }
		    } else {
		        System.err.println("Invalid command: " + command);
		    }
		}
    }

    // Move the robot forward a given number of steps
    private static void move(int steps) {
        for (int i = 0; i < steps; i++) {
        	if (firstMove && penDown) {
                 floor[position[0]][position[1]] = 1; // Mark the floor with an asterisk
                 firstMove = false; // Reset the flag
                 continue; // Skip to the next loop iteration
            }
            
        	if (direction.equals("N")) {
                if (position[0] < N - 1) {
                    position[0]++;
                }
            } else if (direction.equals("S")) {
                if (position[0] > 0) {
                    position[0]--;
                }
            } else if (direction.equals("W")) {
                if (position[1] > 0) {
                    position[1]--;
                }
            } else if (direction.equals("E")) {
                if (position[1] < N - 1) {
                    position[1]++;
                }
            }
            
            if (penDown) {
                floor[position[0]][position[1]] = 1; // Mark the floor with an asterisk
            }
        }
    }

    // Turn the robot 90 degrees to the right
    private static void turnRight() {
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
    private static void turnLeft() {
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
    private static void printFloor(JTextArea outputArea) {
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
    
    private static void initializeSystem(int size) {
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
    }
}

