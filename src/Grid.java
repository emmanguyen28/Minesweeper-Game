import objectdraw.*;
import java.awt.*;
/**
 * This is a class to manipulate the gameboard - a grid of cells, represented by a 2D array
 *
 * @HaNguyen (your name)
 * @12/04/17 (a version number or a date)
 */
public class Grid
{

    //declare contants
    private static final int NUM_OF_ROW = 10;
    private static final int NUM_OF_COL = 10;
    private static final int NUM_OF_MINES = 10;
    private static final int CELL_SIZE = 28;
    private static final int MARGIN_LEFT = 60;
    private static final int MARGIN_TOP = 10;

    //declare instance variables
    private GridCell[][] grid;
    private boolean gameOver = false;

    //construct a grid
    public Grid(DrawingCanvas canvas) {
        grid = new GridCell[NUM_OF_ROW][NUM_OF_COL];
        for (int row = 0; row < grid.length; row++) {
            for (int col = 0; col < grid[row].length; col++) {
                grid[row][col] = new GridCell(row,col, canvas);
            }
        }

        //randomly place mines in the grid
        placeMines();
    }

    //a method to return the number of mines in the grid
    public int getNumMines() {
        return NUM_OF_MINES;
    }

    //a method to randomly place the mines
    private void placeMines() {
        int minePlaced = 0;
        while (minePlaced < NUM_OF_MINES) {
            //create a random int generator
            RandomIntGenerator mines = new RandomIntGenerator(0, grid.length);
            int mineRow = mines.nextValue();
            int mineCol = mines.nextValue();

            //place the mine
            for (int row = 0; row < grid.length; row++) {
                for (int col = 0; col < grid[row].length; col++) {
                    if (row == mineRow && col == mineCol && grid[row][col] != null ) {
                        if (!grid[row][col].containsMine()) {
                            grid[row][col].placeMine();
                            minePlaced++;
                        }
                    }
                }
            }
        }
    }

    //a method to show all the mines (method used for testing)
    public void showAllMines() {
        for (int row = 0; row < grid.length; row++) {
            for (int col = 0; col < grid[row].length; col++) {
                if (grid[row][col] != null && grid[row][col].containsMine()) {
                    grid[row][col].showMine(row,col);
                }
            }
        }
    }

    //a method to count a cell's neighbors that hold mines
    private int calculateMines(int row, int col) {
        int startRow;
        if (row == 0) {
            startRow = 0;
        } else {
            startRow = row - 1;
        }

        int startCol ;
        if (col == 0) {
            startCol = 0;
        } else {
            startCol = col - 1;
        }

        int endRow;
        if (row == 9) {
            endRow = 9;
        } else {
            endRow = row + 1;
        }

        int endCol;
        if (col == 9) {
            endCol = 9;
        } else {
            endCol = col + 1;
        }

        int minesFound = 0;
        for (int r = startRow; r <= endRow; r++) {
            for (int c = startCol; c <= endCol; c++) {
                if (grid[r][c] != grid[row][col]) {
                    if (grid[r][c] != null && grid[r][c].containsMine()) {
                        minesFound++;
                    }
                }
            }
        }
        return minesFound;
    }

    //a method to display all neighbors (method used for testing)
    public void displayAllNeighbors() {
        for (int row = 0; row < grid.length; row++) {
            for (int col = 0; col < grid[row].length; col++) {
                if (grid[row][col] != null && !grid[row][col].containsMine()) {
                    grid[row][col].revealNeighborCount(calculateMines(row,col));
                }
            }
        }
    }

    //a method to uncover a cell at specific location
    public void uncoverCell(Location point) {

        //translate x y coordinates into row and column
        int pointRow = translateX(point);
        int pointCol = translateY(point);

        //if the cell is not exposed yet, not flagged yet and the user hasn't won or lost
        if (!grid[pointRow][pointCol].isUncovered() && !grid[pointRow][pointCol].isFlagged() && !gameOver) {
            /*if the cell doesn't contain mine, it is exposed and show the number of neighbors
             * with mines. 
             */ 
            if (!grid[pointRow][pointCol].containsMine()) {
                grid[pointRow][pointCol].revealNeighborCount(calculateMines(pointRow,pointCol));
            } 
            //if the cell contains a mine, then it is exploded and the winner loses
            else if (grid[pointRow][pointCol].containsMine()) {
                lose(grid[pointRow][pointCol]);
            }
        }
    }

    //a method to place or remove a flag of a cell 
    public void placeOrRemoveFlag(Location point) {

        //translate x y coordinates into row and column
        int pointRow = translateX(point);
        int pointCol = translateY(point);

        //if the cell is uncovered and the user has't won or lost the game 
        if (!grid[pointRow][pointCol].isUncovered() && !gameOver) {

            //if the cell isn't flagged, it is placed a flag
            if (!grid[pointRow][pointCol].isFlagged()) {
                grid[pointRow][pointCol].placeFlag();
            }
            //if the cell is flagged, its flag is removed
            else {
                grid[pointRow][pointCol].removeFlag();
            }
        }
    }

    //a method to update the number of flagged cells
    public int updateNumFlagged() {
        int flaggedCell = 0;
        for (int row = 0; row < grid.length; row++) {
            for (int col = 0; col < grid[row].length; col++) {
                if (grid[row][col] != null) {
                    if (grid[row][col].isFlagged()) {
                        flaggedCell++;
                    }
                }
            }
        }
        return flaggedCell;
    }

    //a method to check whether all cells have been revealed (except the cells with bomb)
    private boolean allRevealed() {
        int cellsRevealed = 0;
        for (int row = 0; row < grid.length; row++) {
            for (int col = 0; col < grid[row].length; col++) {
                if (grid[row][col] != null && grid[row][col].isUncovered()) {
                    cellsRevealed++;
                }
            }
        }
        return (cellsRevealed == (grid.length*grid[0].length - NUM_OF_MINES));
    }

    //a method to translate x coordinate into row
    private int translateX(Location point) {
        int pointRow;
        //translate x y coordinates into row and column
        if ((point.getX() - MARGIN_LEFT) < 0) {
            return pointRow = -1;
        } else {
            return pointRow = (int)((point.getX() - MARGIN_LEFT)/CELL_SIZE);
        }
    }

    //a method to translate y coordinate into column
    private int translateY(Location point) {
        int pointCol;
        if ((point.getY() - MARGIN_TOP) < 0) {
            return pointCol = -1;
        } else {
            return pointCol = (int)((point.getY() - MARGIN_TOP)/CELL_SIZE);
        }
    }

    //a method to determine when the user wins
    public void win() {
        //if the user has flagged all cells with mines and all other cells are revealed, the user wins
        if (updateNumFlagged() == getNumMines() && allRevealed()) {
            grid[0][0].win();
            gameOver = true;
        }
    }

    //a method to define what happens when the user clicks on a mine and loses
    private void lose(GridCell cell) {
        cell.explode();
        showAllMines();
        gameOver = true;
    }
}
