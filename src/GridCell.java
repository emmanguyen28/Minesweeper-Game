import objectdraw.*;
import java.awt.*;
/**
 * This is a class to manage an individual cell. 
 *
 * @HaNguyen (your name)
 * @11/28/17 (a version number or a date)
 */
public class GridCell
{
    //declare contants 
    private static final int CELL_SIZE = 28;
    private static final int MARGIN_LEFT = 60;
    private static final int MARGIN_TOP = 10;
    private static final int WHITE_SPACE = 4;
    private static final int MINE_SIZE = 20;

    //declare instance variables
    private FilledRect cell;
    private DrawingCanvas canvas;
    private boolean containsMine = false;
    private boolean uncovered = false;
    private boolean flagged = false;

    //construct a cell
    public GridCell(int row, int col, DrawingCanvas canvas) {
        int cellLeft = MARGIN_LEFT + row*CELL_SIZE;
        int cellTop = MARGIN_TOP + col*CELL_SIZE;
        cell = new FilledRect(cellLeft, cellTop, CELL_SIZE, CELL_SIZE, canvas);
        cell.setColor(Color.GRAY);
        new FramedRect(cellLeft, cellTop, CELL_SIZE, CELL_SIZE, canvas);
        this.canvas = canvas;
    }

    //a method to determine whether a cell contains a mine or not
    public boolean containsMine() {
        return containsMine;
    }

    //a method to place mine into a cell
    public void placeMine() {
        containsMine = true;
    }

    //a method to draw (show) the mine if it contains a mine 
    public void showMine(int row, int col) {
        double mineLeft = cell.getX() + WHITE_SPACE;
        double mineTop = cell.getY() + WHITE_SPACE;
        if (containsMine) {
            new FilledOval(mineLeft, mineTop, MINE_SIZE, MINE_SIZE, canvas);
        }
    }

    //a method to determine whether a cell is uncovered or not
    public boolean isUncovered() {
        return uncovered;
    }

    //a method to reveal (show) neighbors with mines
    public void revealNeighborCount(int value) {
        cell.setColor(Color.WHITE);
        double numberLeft = cell.getX() + WHITE_SPACE*2;
        double numberTop = cell.getY() + WHITE_SPACE/2;
        uncovered = true;
        if (value != 0) {
            Text neighbor = new Text(value, numberLeft, numberTop, canvas);
            neighbor.setFontSize(20);
        } 
    }

    //a method to determine whether a cell is flagged or not
    public boolean isFlagged() {
        return flagged;
    }

    //a method to place flag in a mine
    public void placeFlag() {
        flagged = true;
        cell.setColor(Color.BLUE);
    }

    //a method to remove flag from a cell
    public void removeFlag() {
        flagged = false;
        cell.setColor(Color.GRAY);
    }

    //a method to explode a cell
    public void explode() {
        cell.setColor(Color.RED);
        Text lose = new Text("You Lose!!", MARGIN_LEFT + CELL_SIZE/2, MARGIN_TOP + CELL_SIZE*10, canvas);
        lose.setFontSize(50);
        lose.setColor(Color.PINK);
    } 

    //a method to display text when the user wins the game
    public void win() {
        Text win = new Text("You Win!!", MARGIN_LEFT + CELL_SIZE/2, MARGIN_TOP + CELL_SIZE*10, canvas);
        win.setFontSize(50);
        win.setColor(Color.BLUE);
    }
}
