import objectdraw.*;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.event.*;
/**
 * This is a class to implement the Minesweeper game. It is responsible for the intial display and defining the event handlers 
 *
 * @HaNguyen (your name)
 * @11/28/2017 (a version number or a date)
 */
public class Minesweeper extends WindowController implements ActionListener, MouseListener
{
    //declare instance variables
    private JButton newGame;
    private JLabel minesFound;
    private JLabel numFoundLabel;
    private Grid grid;

    //create the initial display
    public void begin()
    {
        canvas.addMouseListener(this);

        //set up a new grid
        grid = new Grid(canvas);

        //create a JPanel to hold the game button + score
        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.Y_AXIS));

        //create the New Game button, add an action listener to it, and add it to the control panel
        newGame = new JButton("New game");
        newGame.setAlignmentX(Component.CENTER_ALIGNMENT);
        newGame.addActionListener(this);
        controlPanel.add(newGame);

        //create a panel to hold the minefound score
        JPanel minePanel = new JPanel();
        minePanel.add(new JLabel("Mines found:    "));

        //create a number label
        numFoundLabel = new JLabel("0");
        minePanel.add(numFoundLabel);

        //create total number label and add it to the mine panel
        JLabel numMinesLabel = new JLabel("/" + grid.getNumMines());
        minePanel.add(numMinesLabel);

        //add the mine panel to the control panel
        controlPanel.add(minePanel);

        //add the control panel to JFrame
        add(controlPanel, BorderLayout.SOUTH);

        //grid.showAllMines();
        //grid.displayAllNeighbors();
    }

    //what happens when user presses the mouse 
    public void mousePressed(MouseEvent event)
    { 
        //determine the location at which the mouse clicks 
        Location point = new Location (event.getX(), event.getY());

        /*as the user right clicks on a cell, it is either placed or removed a flag. At the same time, the mines found label
         * is updated
         */      
        if (event.getButton() == MouseEvent.BUTTON3 || (event.getModifiersEx() & MouseEvent.CTRL_DOWN_MASK) == MouseEvent.CTRL_DOWN_MASK) 
        {
            grid.placeOrRemoveFlag(point);
            numFoundLabel.setText("" + grid.updateNumFlagged());
        }

        // as the user left clicks on a cell, it is uncovered and certain actions are taken based on the state of the cell
        else
        {  
            grid.uncoverCell(point);
        }

        //the user wins on any click if all the conditions have been met (all cells are revealed and all mines are flagged) 
        grid.win();
    }

    //as the user clicks on the New Game button, a new game is started without having to restart the program
    public void actionPerformed(ActionEvent event) {
        if (event.getSource() == newGame) {
            if (grid != null) {
                canvas.clear();
            }
            grid = new Grid(canvas);
            numFoundLabel.setText("0");
        }
    }

    //mouse clicked stub
    public void mouseClicked(MouseEvent arg0) 
    {

    }

    //mouse released stub
    public void mouseReleased(MouseEvent arg0) 
    {

    }

    //mouse entered stub
    public void mouseEntered(MouseEvent arg0) 
    {

    }

    //mouse exited stub
    public void mouseExited(MouseEvent arg0) 
    {

    }

}
