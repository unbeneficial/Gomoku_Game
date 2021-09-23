import javafx.application.Application;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.event.EventHandler;
import javafx.event.ActionEvent;
import javafx.scene.paint.Color;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.geometry.Insets;
import java.util.List;

/**
 * How to play: Two players alternate until one wins. One wins the game by getting exactly five pieces in a row.
 * Black piece moves first.
 * @author Benjamin Wang
 */

public class Gomoku extends Application {

    /** Tester Colors Map */
    private static int GREEN = 0;
    private static int BLACK = 1;
    private static int WHITE = 2;

    /** Tester Directions Map */
    private static int NORTH = 0;
    private static int NORTHEAST = 1;
    private static int EAST = 2;
    private static int SOUTHEAST = 3;
    private static int SOUTH = 4;
    private static int SOUTHWEST = 5;
    private static int WEST = 6;
    private static int NORTHWEST = 7;

    /** Row in the grid. */
    private int rows;
    /** Column in the grid. */
    private int columns;

    /** Two dimensional array of Buttons */
    private Button[][] grid;

    /** Two dimensional array of ints, representing colors (mirror for testing) */
    private int[][] gridInt;

    /** Whether game is won! */
    private boolean isGameWon = false;
    /** Current color */
    private Color color = Color.BLACK;
    /** the number of pieces in a row needed to win game */
    private int winNumber;

    /** Current color player int starts at 1, or Black */
    private int colorCurrent = BLACK;


    /** Enum represents possible directions in the game.
     * x and y displacements (neg is up or left one, pos is down or right one)
     * COORDINATE MAP
     * pointer[0] = north (up 1)
     * pointer[1] = northeast (right 1, up 1)
     * pointer[2] = east (right 1)
     * pointer[3] = southeast (right 1, down 1)
     * pointer[4] = south (down 1)
     * pointer[5] = southwest (left 1, down 1)
     * pointer[6] = west (left 1)
     * pointer[7] = northwest (left 1, up 1)
     */
    private int[][] pointer = { {0, -1}, {1, -1}, {1, 0}, {1, 1}, {0, 1}, {-1, 1}, {-1, 0}, {-1, -1} };

    /**
     * Returns the value shifting through the x-axis by
     * @param direction Any number between 0 and 7 from the eight directions in pointer
     * @return int
     */
    public int getXDisplacement(int direction) {
        int[] displacement = pointer[direction];
        return displacement[1];
    }

    /**
     * Returns the value shifting through the y-axis by
     * @param direction Any number between 0 and 7 from the eight directions in pointer
     * @return int
     */
    public int getYDisplacement(int direction) {
        int[] displacement = pointer[direction];
        return displacement[0];
    }

    /**
     * Getter for rows
     * @return rows
     */
    public int getRows() {
        return this.rows;
    }

    /**
     * Setter for rows
     * @param rows
     */
    public void setRows(int rows) {
        this.rows = rows;
    }

    /**
     * Getter for columns
     * @return columns
     */
    public int getColumns() {
        return this.columns;
    }

    /**
     * Setter for columns
     * @param columns
     */
    public void setColumns(int columns) {
        this.columns = columns;
    }

    /**
     * Setter for number of tiles needed in a row
     * @param winNumber
     */
    public void setWinNumber(int winNumber) {
        this.winNumber = winNumber;
    }

    /**
     * Getter for number of tiles needed in a row
     * @return winNumber
     */
    public int getWinNumber() {
        return winNumber;
    }

    /**
     * Getter for current player color
     * @return color
     */
    public Color getPlayerColor() {
        return color;
    }

    /**
     * Setter for current player color
     * @param color
     */
    public void setPlayerColor(Color color) {
        this.color = color;
    }

    /**
     * Getter for int representation of current player color
     * @return color
     */
    public int getPlayerColorInt() {
        return colorCurrent;
    }

    /**
     * Setter for int representation of current player color
     * @param colorCurrent
     */
    public void setPlayerColorInt(int colorCurrent) {
        this.colorCurrent = colorCurrent;
    }

    /**
     * Gets the button color
     * @param button
     * @return Color
     */
    public Color getButtonColor(Button button) {
        List<BackgroundFill> fills = button.getBackground().getFills();
        return (Color)(fills.get(fills.size() - 1).getFill());
    }

    /*
     * Gets the integer representation of color at a row and column
     * @param gridInt
     * @param row
     * @param column
     * @return int
     */
    public int getButtonIntColor(int[][] gridInt, int row, int column) {
        return gridInt[row][column];
    }

    /**
     * Helper method returns the grid of ints for testing
     * @return gridInt
     */
    public int[][] getGridInt() {
        return gridInt;
    }

    /**
     * Helper method sets the grid of ints for testing
     * @param gridInt
     */
    public void setGridInt(int[][] gridInt) {
        this.gridInt = gridInt;
    }

    /**
     * Set game won helper method for testing
     * @param isGameWon
     */
    public void setGameWon(boolean isGameWon) {
        this.isGameWon = isGameWon;
    }

//    /**
//     * Get game won helper method for testing
//     * @return isGameWon
//     */
//    public boolean getGameWon() {
//        return isGameWon;
//    }


    /**
     * Method start must be overridden for Application
     * @param primaryStage
     * @throws Exception
     */
    public void start (Stage primaryStage) {

        BorderPane pane = new BorderPane();
        GridPane gridpane = new GridPane();

        // Sets default game
        // 19x19 game
        if (this.getParameters().getRaw().size() == 0) {
            this.setWinNumber(5);
            this.setRows(19);
            this.setColumns(19);
        }

        // Sets the board if given one parameter
        if (this.getParameters().getRaw().size() == 1) {
            try {
                if (Integer.parseInt(this.getParameters().getRaw().get(0)) > 4) {
                    this.setWinNumber(Integer.parseInt(this.getParameters().getRaw().get(0)));
                } else {
                    System.out.println("Incorrect input: win number must be at least 5");
                }

                this.setRows(19);
                this.setColumns(19);
            } catch (NumberFormatException e) {
                System.out.println("Incorrect input: takes in integer");
            }
        }

        // Sets the board if given two parameters
        if (this.getParameters().getRaw().size() == 2) {
            try {
                this.setWinNumber(5);
                this.setRows(Integer.parseInt(this.getParameters().getRaw().get(0)));
                this.setColumns(Integer.parseInt(this.getParameters().getRaw().get(1)));
            } catch (NumberFormatException e) {
                System.out.println("Incorrect input: takes in integer");
            }
        }

        // Sets the board if given three parameters
        if (this.getParameters().getRaw().size() == 3) {
            try {
                if (Integer.parseInt(this.getParameters().getRaw().get(0)) > 4) {
                    this.setWinNumber(Integer.parseInt(this.getParameters().getRaw().get(0)));
                } else {
                    System.out.println("Incorrect input: win number must be at least 5");
                }
                this.setRows(Integer.parseInt(this.getParameters().getRaw().get(1)));
                this.setColumns(Integer.parseInt(this.getParameters().getRaw().get(2)));
            } catch (NumberFormatException e) {
                System.out.println("Incorrect input: takes in integer");
            }
        }

        grid = new Button[getRows()][getColumns()];
        gridInt = new int[getRows()][getColumns()];

        // iterates through rows and columns to add buttons to grid!
        for (int i = 0; i < getRows(); i++) {
            for (int j = 0; j < getColumns(); j++) {
                // creates a new button for every row and column place
                Button button = new Button();
                grid[i][j] = button;
                button.setPrefHeight(25);
                button.setPrefWidth(25);
                button.setMinHeight(20);
                button.setMinWidth(20);
                button.setBackground(new Background(new BackgroundFill(Color.GREEN, CornerRadii.EMPTY, new Insets(1))));
                button.setOnAction(new ProcessClick(i, j));
                //adds the button to grid pane
                gridpane.add(button, j, i);

            }
        }

        pane.setCenter(gridpane);
        Scene scene = new Scene(pane);

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * The main method
     * @param args
     */
    public static void main(String[] args) {
        Application.launch(args);
    }

    /**
     * Class for handling Button Click!
     */
    public class ProcessClick implements EventHandler<ActionEvent> {

        // Stores whether the button was placed in the past!
        private boolean alreadyPlaced = false;

        // Stores the location of the clicked button
        private int xCoor;
        private int yCoor;

        // the color at the xcoor and y coor
        private int testBoard;

        /**
         * Constructor for process click, assigning xCoor and yCoor
         * @param xCoor
         * @param yCoor
         */
        public ProcessClick (int xCoor, int yCoor) {
            this.xCoor = xCoor;
            this.yCoor = yCoor;
        }

        /**
         * Sets x coordinate
         * @param xCoor
         */
        public void setXCoor (int xCoor) {
            this.xCoor = xCoor;
        }

        /**
         * Sets y coordinate
         * @param yCoor
         */
        public void setYCoor (int yCoor) {
            this.yCoor = yCoor;
        }

        /**
         * Gets the x coordinate of the button clicked
         * @return xCoor
         */
        public int getXCoor() {
            return xCoor;
        }

        /**
         * Gets the x coordinate of the button clicked
         * @return yCoor
         */
        public int getYCoor() {
            return yCoor;
        }

        /**
         * Response to a button being clicked
         * @param e Action occurred
         */
        public void handle(ActionEvent e) {

            if (isGameWon) {
                return;
            }

            Button button = (Button)e.getSource();

            // if the button is open
            if (!alreadyPlaced) {

                // important: changes the int board before altering the graphics for rule check
                createTestBoard(gridInt);

                // check if fourFour or threeThree apply
                if (fourFour() || threeThree()) {
                    displayAlerts();
                    button.setBackground(new Background(new BackgroundFill(Color.GREEN, CornerRadii.EMPTY, new Insets(1))));
                    // resets the int board to "green"
                    resetTestBoard(gridInt);

                } else {

                    button.setBackground(new Background(new BackgroundFill(Color.GREEN, CornerRadii.EMPTY, new Insets(1)),
                            new BackgroundFill(getPlayerColor(), new CornerRadii(50), new Insets(3))));

                    createTestBoard(gridInt);

                    alreadyPlaced = true;
                    checkWin(button);

                    alternateColor();
                }
            }

        }

        /**
         * Method alternating the player color (white and black)
         */
        public void alternateColor() {
            // statement switching colors
            if (getPlayerColorInt() == BLACK) {
                setPlayerColor(Color.WHITE);
                setPlayerColorInt(WHITE);
            } else {
                setPlayerColor(Color.BLACK);
                setPlayerColorInt(BLACK);

            }
        }

        /**
         * Java FX alert method
         */
        public void displayAlerts() {
            // messages appearing for four four and three three
            if (fourFour()) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("WARNING");
                alert.setHeaderText(null);
                alert.setContentText("This violates the four-four rule! Try a different spot.");
                alert.showAndWait();
            } else if (threeThree()) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("WARNING");
                alert.setHeaderText(null);
                alert.setContentText("This violates the three-three rule! Try a different spot.");
                alert.showAndWait();
            }
        }

        /**
         * Places a color int at certain x-coor and y-coor in a 2D board
         * @param board double array of int used in ProcessClick
         */
        public void createTestBoard(int[][] board) {
            board[xCoor][yCoor] = getPlayerColorInt();
            testBoard = board[xCoor][yCoor];
        }

        /**
         * Helper method for testing createTestBoard
         * @return testBoard
         */
        public int getTestBoard() {
            return testBoard;
        }

        /**
         * Helper method that resets the board at a certain x and y coor to green.
         * @param board double array of int used in ProcessClick
         */
        public void resetTestBoard(int[][] board) {
            board[xCoor][yCoor] = GREEN;
            testBoard = board[xCoor][yCoor];
        }

        /**
         * Checks to see if there is a game won, or 5 pieces of the same color in a row.
         * @param button The button clicked
         */
        public void checkWin(Button button) {
            // stores the number of same color pieces
            int counter = 0;
            int counterINT = 0;

            // check if not empty, while having a counter
            if (!isEmpty(button)) {

                // goes through each direction
                for (int i = 0; i < 4; i++) {
                    // counter for all four directions
                    counterINT = count4DirectionsIntBoard(i);

                    // when the win number equals the pieces in a row, isGameWon loop exits
                    if (getWinNumber() == counterINT) {
                        isGameWon = true;
                    }

                } // post condition: all directions are checked

            }

            declareWinner();
        }

        /**
         * This method counts the number of buttons in a row starting from the given button
         * @param direction the int representation of the eight directions
         * @return counter the number of same colors in row
         */
        public int count4DirectionsIntBoard (int direction) {
            // doesn't account for the button you're on (starts at )
            int counterINT = -1;

            // checks in the direction entered
            counterINT += numberInLineInt(getGridInt(), getXCoor(), getYCoor(), direction);
            // checks in opposite direction (180 degrees)
            counterINT += numberInLineInt(getGridInt(), getXCoor(), getYCoor(), direction + 4);

            return counterINT;
        }

        /**
         * Checks to see if there is a four Four, regardless of open spaces
         * @return The boolean representation of whether there was a four four.
         */
        public boolean fourFour() {
            // sets number for 4-4 rule
            int max4 = getWinNumber() - 1;
            int counter = 0;

            // counts the number of pieces in every direction
            for (int direction = 0; direction < 4; direction++) {
                // counts the number of fours created by pieces of the same color
                if (count4DirectionsIntBoard(direction) == max4) {
                    counter++;
                }
            }

            // when the counter of the number of directions created with the max is 2 or more groups, set boolean to true

            if (counter >= 2) {
                System.out.println("This violates the four-four rule! Try a different spot.");
            }

            return counter >= 2;
        }

        /**
         * Checks to see if there is a three-three. Player can't make a move that simultaneously creates two or more groups
         * of three
         * @return The boolean stating whether there is a three three
         */
        public boolean threeThree() {
            // sets number for 3-3 rule
            int max3 = getWinNumber() - 2;
            int counter = 0;
            boolean threeThree = false;

            // counts the number of pieces in every direction
            for (int direction = 0; direction < 4; direction++) {
                // counts the number of fours created by pieces of the same color
                if (count4DirectionsIntBoard(direction) == max3 && (isOpen(getGridInt(), getXCoor(), getYCoor(), direction) && isOpen(getGridInt(), getXCoor(), getYCoor(), direction + 4))) {
                    counter++;
                }
            }

            // when the counter of the number of directions created with the max is 2 or more groups, set boolean to true

            if (counter >= 2) {
                System.out.println("This violates the three-three rule! Try a different spot.");
            }

            return counter >= 2;

        }
    }

    /**
     * Declares victory!
     * Uses JavaFX to create a popup window
     */
    public void declareWinner () {
        if (isGameWon) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Declare the Winner");
            alert.setHeaderText(null);
            // when the game is won and the last piece placed was BLACK
            if (getPlayerColorInt() == BLACK) {
                System.out.println("Black is the winner!");
                alert.setContentText("Black is the winner!");
            }

            // when the game is won and the last piece placed was WHITE
            if (getPlayerColorInt() == WHITE) {
                System.out.println("White is the winner!");
                alert.setContentText("White is the winner!");
            }
            alert.showAndWait();
        }
    }

    /**
     * This method returns the number of pieces of the same color in a row including the button in params.
     * @param grid The 2D array of ints representing the board
     * @param row The row position of the currently played piece
     * @param column The column position of the currently played piece
     * @param direction The direction to search (vert, horiz, right diag, left diag)
     * @return The number of consecutive same color pieces counting the first starting one
     */
    public int numberInLineInt (int[][] grid, int row, int column, int direction) {
        // sets how much we're moving by
        int dx = getXDisplacement(direction);
        int dy = getYDisplacement(direction);
        // counter starts at 1 to account for the button you're on
        int counter = 1;

        try {
            // I believe the problem is here, so int color is not correct. It should be green
            int color = getButtonIntColor(grid, row, column);
            // precondition: the dx and dy are set correctly
            // ensures that while counting, does not exit the board

            // makes sure is counting pieces of same color
            while (getButtonIntColor(grid, row+dx, column+dy) == color) {
                row += dx;
                column += dy;
                counter++;
            }
            // postcondition: counted same color

            // doesn't account for the button you're on!
            return counter;

        } catch (ArrayIndexOutOfBoundsException e) {
            return counter;
        }
    }

    /**
     * Checks whether JavaFX button is empty or not
     * @param button the JavaFX feature passed in
     * @return boolean
     */
    public boolean isEmpty(Button button) {
        if (getButtonColor(button) == Color.GREEN) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Determines if there is an open space following the input direction at position row, column on the board.
     * Directions consists of up, right, down, left, and the four diagonals.
     * @param grid The two D array of buttons representing the board of the game.
     * @param row The position for the row of the currently played piece.
     * @param column The position for the column of the currently played piece.
     * @param direction The direction to search (up, right, down, left, and the four diagonals).
     * @return The boolean representation if there is an open space following the direction starting at the row, column on the board.
     */
    public boolean isOpen (int[][] grid, int row, int column, int direction) {

        // counters for row and columns
        int r = row;
        int c = column;

        // stores whether the direction is open
        boolean open = false;

        // ensures that while counting, does not exit the board
        boolean exit = false;

        // assigns color int
        int color = getButtonIntColor(grid, row, column);

        if (getButtonIntColor(grid, r, c) == GREEN) {
            open = false;
            exit = true;
        }

        // NORTH (up 1)
        if (direction == NORTH) {
            while ((r > 0) && (exit == false)) {

                // 1. check if the above is open (green)
                // return open true, exit true
                // 2. check if the above is the same color
                // return exit false;
                if (getButtonIntColor(grid, r-1, c) == GREEN) {
                    open = true;
                    exit = true;
                } else if (getButtonIntColor(grid, r-1, c) == color) {
                    open = false;
                    exit = false;
                } else {
                    open = false;
                    exit = true;
                }
                r--;
            }
        }

        // NORTHEAST (right 1, up 1)
        else if (direction == NORTHEAST) {
            while ((r > 0) && (c < getColumns()-1) && (exit == false)) {
                if (getButtonIntColor(grid, r-1, c+1) == GREEN) {
                    open = true;
                    exit = true;
                } else if (getButtonIntColor(grid, r-1, c+1) == color) {
                    open = false;
                    exit = false;
                } else {
                    open = false;
                    exit = true;
                }
                r--;
                c++;
            }
        }

        // EAST (right 1)
        else if (direction == EAST) {
            while ((c < getColumns()-1) && (exit == false)) {
                if (getButtonIntColor(grid, r, c+1) == GREEN) {
                    open = true;
                    exit = true;
                } else if (getButtonIntColor(grid, r, c+1) == color) {
                    open = false;
                    exit = false;
                } else {
                    open = false;
                    exit = true;
                }
                c++;
            }
        }

        // SOUTHEAST (right 1, down 1)
        else if (direction == SOUTHEAST) {
            while ((r < getRows()-1) && (c < getColumns()-1) && (exit == false)) {
                if (getButtonIntColor(grid, r+1, c+1) == GREEN) {
                    open = true;
                    exit = true;
                } else if (getButtonIntColor(grid, r+1, c+1) == color) {
                    open = false;
                    exit = false;
                } else {
                    open = false;
                    exit = true;
                }
                r++;
                c++;
            }
        }

        // SOUTH (down 1)
        else if (direction == SOUTH) {
            while ((r < getRows()-1) && (exit == false)) {
                if (getButtonIntColor(grid, r+1, c) == GREEN) {
                    open = true;
                    exit = true;
                } else if (getButtonIntColor(grid, r+1, c) == color) {
                    open = false;
                    exit = false;
                } else {
                    open = false;
                    exit = true;
                }
                r++;
            }
        }

        // SOUTHWEST (left 1, down 1)
        else if (direction == SOUTHWEST) {
            while ((r < getRows()-1) && (c > 0) && (exit == false)) {
                if (getButtonIntColor(grid, r+1, c-1) == GREEN) {
                    open = true;
                    exit = true;
                } else if (getButtonIntColor(grid, r+1, c-1) == color) {
                    open = false;
                    exit = false;
                } else {
                    open = false;
                    exit = true;
                }
                r++;
                c--;
            }
        }

        // WEST (left 1)
        else if (direction == WEST) {
            while ((c > 0) && (exit == false)) {
                if (getButtonIntColor(grid, r, c-1) == GREEN) {
                    open = true;
                    exit = true;
                } else if (getButtonIntColor(grid, r, c-1) == color) {
                    open = false;
                    exit = false;
                } else {
                    open = false;
                    exit = true;
                }
                c--;
            }
        }

        // NORTHWEST (left 1, up 1)
        else if (direction == NORTHWEST) {
            while ((r > 0) && (c > 0) && (exit == false)) {
                if (getButtonIntColor(grid, r-1, c-1) == GREEN) {
                    open = true;
                    exit = true;
                } else if (getButtonIntColor(grid, r-1, c-1) == color) {
                    open = false;
                    exit = false;
                } else {
                    open = false;
                    exit = true;
                }
                r--;
                c--;
            }
        }

        return open;
    }
}