package bbear.coen6761.proj;
import javax.swing.*;

public class RobotDrawing {
    private static final int N = 10; // Size of the floor

    private static int[][] floor = new int[N][N]; // The floor array
    private static int[] position = {0, 0}; // Robot's initial position [row, col]
    private static boolean penDown = true; // Robot's pen status
    private static String direction = "S"; // Robot's initial direction

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> createAndShowGUI());
    }

    private static void createAndShowGUI() {
        JFrame frame = new JFrame("Robot Drawing");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        JTextArea outputArea = new JTextArea(10, 30);
        outputArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(outputArea);
        mainPanel.add(scrollPane);

        JTextField inputField = new JTextField(20);
        inputField.addActionListener(e -> {
            String command = inputField.getText();
            processCommand(command);
            inputField.setText("");
            outputArea.append(command + "\n");
            printFloor(outputArea);
        });
        mainPanel.add(inputField);

        frame.getContentPane().add(mainPanel);
        frame.pack();
        frame.setVisible(true);
    }

    private static void processCommand(String command) {
        if (command.startsWith("move")) {
            String[] parts = command.split(" ");
            if (parts.length == 2) {
                try {
                    int steps = Integer.parseInt(parts[1]);
                    move(steps);
                } catch (NumberFormatException e) {
                    System.err.println("Invalid move command: " + command);
                }
            } else {
                System.err.println("Invalid move command: " + command);
            }
        } else if (command.equals("right")) {
            turnRight();
        } else if (command.equals("left")) {
            turnLeft();
        }
    }

    // Move the robot forward a given number of steps
    private static void move(int steps) {
        for (int i = 0; i < steps; i++) {
            if (direction.equals("N")) {
                if (position[0] > 0) {
                    position[0]--;
                }
            } else if (direction.equals("S")) {
                if (position[0] < N - 1) {
                    position[0]++;
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
        for (int[] row : floor) {
            for (int cell : row) {
                if (cell == 1) {
                    sb.append("*");
                } else {
                    sb.append(" ");
                }
            }
            sb.append("\n");
        }
        sb.append("\n");
        outputArea.append(sb.toString());
    }
}

