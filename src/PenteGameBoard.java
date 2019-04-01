import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class PenteGameBoard extends JPanel {
    
    public static final int EMPTY = 0;
    public static final int BLACKSTONE = 1;
    public static final int WHITESTONE = -1;
    public static final int NUM_SQUARES_SIDE = 19;
    public static final int INNER_START = 7;
    public static final int INNER_END = 11;
    public static final int PLAYER1_TURN = 1;
    public static final int PLAYER2_TURN = -1;
    
    
    private int bWidth, bHeight;
    
    private PenteBoardSquare testSquare;
    private int squareW, squareH;
    private JFrame myFrame;
    
    //variables for playing the game
    // Its assumed that P1 would be the darkstone (moves first)
    int playerTurn;
    boolean player1IsComputer = false;
    boolean player2IsComputer = false;
    String p1Name, p2Name;
    
    
    //make "data structure" to hold board pieces
    private PenteBoardSquare[][] gameBoard;
   
  //here are the constructor(s)  
    public PenteGameBoard(int w, int h, JFrame f) {
        
        //store these variables
        bWidth = w;
        bHeight = h;
        myFrame = f;
    
        this.setSize(w,h);
        this.setBackground(Color.CYAN);
        
        squareW = bWidth/this.NUM_SQUARES_SIDE;
        squareH = bHeight/this.NUM_SQUARES_SIDE;
        
       // testSquare = new PenteBoardSquare(0,0,squareW, squareH);
         gameBoard = new PenteBoardSquare[NUM_SQUARES_SIDE][NUM_SQUARES_SIDE];
         
         for(int row = 0; row < NUM_SQUARES_SIDE; row++ ) {
             for(int col = 0; col < NUM_SQUARES_SIDE; col++) {
                 
                 gameBoard[row][col] = new PenteBoardSquare(col*squareW ,row*squareH,squareW, squareH);
                 if(col >= INNER_START && col <= INNER_END) {
                     if(row >= INNER_START && row <= INNER_END) {
                         gameBoard[row][col].setInner();
                     }
                 }
                 //To test I am going to make them all darkstones
                 /*
                 if( (row+col)  % 2 == 0) {
                     gameBoard[row][col].setState(BLACKSTONE);
                 } else {
                     gameBoard[row][col].setState(WHITESTONE);
                 }
                 */
             }
        }

    }
    
    //method to do drawing.....
    //we do this by overriding.
    public void paintComponent(Graphics g) {
        //updateSizes();
        
        g.setColor(Color.CYAN);
        g.fillRect(0, 0, bWidth, bHeight);
        
        //do this 19 x 19 times
       // testSquare.drawMe(g);
        for(int row = 0; row < NUM_SQUARES_SIDE; row++ ) { 
            for(int col = 0; col < NUM_SQUARES_SIDE; col++) {
                gameBoard[row][col].drawMe(g);
            }
        }   
    }
    
    
    public void resetBoard() {
        for(int row = 0; row < NUM_SQUARES_SIDE; row++ ) { 
            for(int col = 0; col < NUM_SQUARES_SIDE; col++) {
                gameBoard[row][col].setState(EMPTY);
            }
        }  
        
    }
    
    
    
    public void startNewGame() {
        resetBoard();
        
        p1Name = JOptionPane.showInputDialog("Name of player 1 (or type 'c' for computer");
        if(p1Name.equals('c') || p1Name.equals("computer") || p1Name.equals("comp")) {
            player1IsComputer = true;
        }
        
        p2Name = JOptionPane.showInputDialog("Name of player 2 (or type 'c' for computer");
        if(p2Name.equals('c') || p2Name.equals("computer") || p2Name.equals("comp")) {
            player2IsComputer = true;
        }
        
        playerTurn = PLAYER1_TURN;
        this.gameBoard[NUM_SQUARES_SIDE/2][NUM_SQUARES_SIDE/2].setState(BLACKSTONE);
        changePlayerTurn();
        
        this.repaint();
    }
    
    
    public void changePlayerTurn() {
        playerTurn *= -1;
    }
    
    
    
    
    
   //THis is extra
    
    public void updateSizes() {
        /*
        
       if (myFrame.getWidth() != bWidth || myFrame.getHeight() != bHeight + 20) {
           bWidth = myFrame.getWidth();
           bHeight = myFrame.getHeight()-20;
           
           squareW = bWidth/this.NUM_SQUARES_SIDE;
           squareH = bHeight/this.NUM_SQUARES_SIDE;
           
           resetSquares(squareW, squareH);
       }
       */
  
    }
    
    public void resetSquares(int w, int h) {
        
        for(int row = 0; row < NUM_SQUARES_SIDE; row++ ) { 
            for(int col = 0; col < NUM_SQUARES_SIDE; col++) {
                
                gameBoard[row][col].setXLoc(col * w);
                gameBoard[row][col].setYLoc(row * h);
                gameBoard[row][col].setWidth(w);
                gameBoard[row][col].setHeight(h);
            }
        } 
        
    }
    
    
    
    

}
