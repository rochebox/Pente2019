import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class PenteGameBoard extends JPanel implements MouseListener {
    
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
 
    //variables for playing the game
    // Its assumed that P1 would be the darkstone (moves first)
    private int playerTurn;
    private boolean player1IsComputer = false;
    private boolean player2IsComputer = false;
    private String p1Name, p2Name;
    
    
    //make "data structure" to hold board pieces
    private PenteBoardSquare[][] gameBoard;
    private PenteScore myScoreBoard;
    //NEW ON RE-VISIT DAY!
    private int p1Captures, p2Captures;
    
   
  //here are the constructor(s)  
    public PenteGameBoard(int w, int h, PenteScore sb) {
        
        //store these variables
        bWidth = w;
        bHeight = h;
        myScoreBoard = sb;
        
        p1Captures = 0;
        p2Captures = 0;
    
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
            
             }
        }
         //funky initial pente stuff
         initialPente();
         //initialDisplay();
         repaint();
         //add mouse listening capability
         addMouseListener(this);
         this.setFocusable(true);

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
        p1Captures = 0;
        p1Captures = 0;
        //resetBoard(); 
        //ImageIcon i = createImageIcon("pentePic.png");
        JOptionPane myPane = new JOptionPane();
       
        //myPane.setIcon(i);
        p1Name = myPane.showInputDialog("Name of player 1 (or type 'c' for computer");
        myPane.setVisible(true);
        myScoreBoard.setName(p1Name, BLACKSTONE);
        myScoreBoard.setCaptures(p1Captures, BLACKSTONE);
        
        //p1Name = JOptionPane.showInputDialog("Name of player 1 (or type 'c' for computer");
        if(p1Name.equals('c') || p1Name.equals("computer") || p1Name.equals("comp")) {
            player1IsComputer = true;
         }
        
        
        
        p2Name = JOptionPane.showInputDialog("Name of player 2 (or type 'c' for computer");
        if(p2Name.equals('c') || p2Name.equals("computer") || p2Name.equals("comp")) {
            player2IsComputer = true;
        }
        myScoreBoard.setName(p2Name, WHITESTONE);
        myScoreBoard.setCaptures(p2Captures, WHITESTONE);
        
        resetBoard();   // moved here from the first line
        
        playerTurn = PLAYER1_TURN;
        this.gameBoard[NUM_SQUARES_SIDE/2][NUM_SQUARES_SIDE/2].setState(BLACKSTONE);
        changePlayerTurn();
        
        
        this.repaint();
    }
    
    
    public void changePlayerTurn() {
        playerTurn *= -1;
        System.out.println("Its now the turn of: " + playerTurn);
    }
    
    
    
    
    
   //THis is extra
   /* 
    public void updateSizes() {
        /*
        
       if (myFrame.getWidth() != bWidth || myFrame.getHeight() != bHeight + 20) {
           bWidth = myFrame.getWidth();
           bHeight = myFrame.getHeight()-20;
           
           squareW = bWidth/this.NUM_SQUARES_SIDE;
           squareH = bHeight/this.NUM_SQUARES_SIDE;
           
           resetSquares(squareW, squareH);
       }
       
  
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
    */
    //This checks on the board which square you have clicked on
    public void checkClick(int clickX, int clickY) {
        
        for(int row = 0; row < NUM_SQUARES_SIDE; row++ ) { 
            for(int col = 0; col < NUM_SQUARES_SIDE; col++) {
                
                boolean squareClicked = gameBoard[row][col].isClicked(clickX, clickY);
                if(squareClicked) {
                    System.out.println("You clicked the square at [" + row + ", " + col + "]");
                    gameBoard[row][col].setState(playerTurn);
                    this.repaint();
                    this.changePlayerTurn();
                }
            }
        } 
        
        
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        
     // System.out.println("You clicked me");
     // System.out.println("You clicked at [" + e.getX() + ", " + e.getY() + "]");
        
        
      this.checkClick(e.getX(), e.getY());
      
      
      
            
    }

    @Override
    public void mousePressed(MouseEvent e) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void mouseExited(MouseEvent e) {
        // TODO Auto-generated method stub
        
    }
    
    public void initialPente() {
        
        //p
    
        
        this.gameBoard[6][0].setState(BLACKSTONE);
        this.gameBoard[7][0].setState(BLACKSTONE);
        this.gameBoard[8][0].setState(BLACKSTONE);
        this.gameBoard[9][0].setState(BLACKSTONE);
        this.gameBoard[10][0].setState(BLACKSTONE);
        this.gameBoard[11][0].setState(BLACKSTONE);
        this.gameBoard[12][0].setState(BLACKSTONE);
        
        this.gameBoard[6][1].setState(BLACKSTONE);
        this.gameBoard[6][2].setState(BLACKSTONE);
        this.gameBoard[7][2].setState(BLACKSTONE);
        this.gameBoard[8][2].setState(BLACKSTONE);
        this.gameBoard[9][2].setState(BLACKSTONE);
        this.gameBoard[9][1].setState(BLACKSTONE);
        
        //e
        this.gameBoard[6][3].setState(WHITESTONE);
        this.gameBoard[7][3].setState(WHITESTONE);
        this.gameBoard[8][3].setState(WHITESTONE);
        this.gameBoard[9][3].setState(WHITESTONE);
        this.gameBoard[10][3].setState(WHITESTONE);
        this.gameBoard[11][3].setState(WHITESTONE);
        this.gameBoard[12][3].setState(WHITESTONE);
        
        this.gameBoard[6][4].setState(WHITESTONE);
        this.gameBoard[6][5].setState(WHITESTONE);
        this.gameBoard[6][6].setState(WHITESTONE);
        this.gameBoard[9][4].setState(WHITESTONE);
        this.gameBoard[9][5].setState(WHITESTONE);
        this.gameBoard[9][6].setState(WHITESTONE);
        this.gameBoard[12][4].setState(WHITESTONE);
        this.gameBoard[12][5].setState(WHITESTONE);
        this.gameBoard[12][6].setState(WHITESTONE);
        
        //n
        this.gameBoard[6][7].setState(BLACKSTONE);
        this.gameBoard[7][7].setState(BLACKSTONE);
        this.gameBoard[8][7].setState(BLACKSTONE);
        this.gameBoard[9][7].setState(BLACKSTONE);
        this.gameBoard[10][7].setState(BLACKSTONE);
        this.gameBoard[11][7].setState(BLACKSTONE);
        this.gameBoard[12][7].setState(BLACKSTONE);
        
        this.gameBoard[8][8].setState(BLACKSTONE);
        this.gameBoard[9][9].setState(BLACKSTONE);
        this.gameBoard[10][10].setState(BLACKSTONE);
        
        this.gameBoard[6][11].setState(BLACKSTONE);
        this.gameBoard[7][11].setState(BLACKSTONE);
        this.gameBoard[8][11].setState(BLACKSTONE);
        this.gameBoard[9][11].setState(BLACKSTONE);
        this.gameBoard[10][11].setState(BLACKSTONE);
        this.gameBoard[11][11].setState(BLACKSTONE);
        this.gameBoard[12][11].setState(BLACKSTONE);
       
        //t
        this.gameBoard[6][13].setState(WHITESTONE);
        this.gameBoard[7][13].setState(WHITESTONE);
        this.gameBoard[8][13].setState(WHITESTONE);
        this.gameBoard[9][13].setState(WHITESTONE);
        this.gameBoard[10][13].setState(WHITESTONE);
        this.gameBoard[11][13].setState(WHITESTONE);
        this.gameBoard[12][13].setState(WHITESTONE);
        
        //this.gameBoard[6][11].setState(WHITESTONE);
        this.gameBoard[6][12].setState(WHITESTONE);
        this.gameBoard[6][14].setState(WHITESTONE);
        this.gameBoard[6][15].setState(WHITESTONE);
        
        //blue e
        this.gameBoard[6][16].setState(BLACKSTONE);
        this.gameBoard[7][16].setState(BLACKSTONE);
        this.gameBoard[8][16].setState(BLACKSTONE);
        this.gameBoard[9][16].setState(BLACKSTONE);
        this.gameBoard[10][16].setState(BLACKSTONE);
        this.gameBoard[11][16].setState(BLACKSTONE);
        this.gameBoard[12][16].setState(BLACKSTONE);
        
        this.gameBoard[6][17].setState(BLACKSTONE);
        this.gameBoard[6][18].setState(BLACKSTONE);
       
        this.gameBoard[9][17].setState(BLACKSTONE);
        this.gameBoard[9][18].setState(BLACKSTONE);
        
        this.gameBoard[12][17].setState(BLACKSTONE);
        this.gameBoard[12][18].setState(BLACKSTONE);
   
    }
    
    public void initialDisplay() {
        
        //p
        this.gameBoard[6][0].setState(BLACKSTONE);
        this.gameBoard[7][0].setState(BLACKSTONE);
        this.gameBoard[8][0].setState(BLACKSTONE);
        this.gameBoard[9][0].setState(BLACKSTONE);
        this.gameBoard[10][0].setState(BLACKSTONE);
        this.gameBoard[11][0].setState(BLACKSTONE);
        this.gameBoard[12][0].setState(BLACKSTONE);
       
        this.gameBoard[6][1].setState(BLACKSTONE);
        this.gameBoard[6][2].setState(BLACKSTONE);
        
        this.gameBoard[7][2].setState(BLACKSTONE);
        this.gameBoard[8][2].setState(BLACKSTONE);
        
        this.gameBoard[9][2].setState(BLACKSTONE);
        this.gameBoard[9][1].setState(BLACKSTONE);
       
    
   

    }
    
    protected ImageIcon createImageIcon(String f) {
        boolean ok = false;
        ImageIcon ii = null;
        try {
            ii = new ImageIcon(getClass().getResource(f));
         
        } catch(Exception e) {
             System.out.println("Problem in loading " 
                  + f + " in createImageIcon...");
        }
       return ii;
    }
    

}
