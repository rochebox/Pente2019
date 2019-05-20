import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

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
    public static final int MAX_CAPTURES = 5;
    public static final int SLEEP_TIME = 750;
    
    private int bWidth, bHeight;
    
    private PenteBoardSquare testSquare;
    private int squareW, squareH;
 
    //variables for playing the game
    // Its assumed that P1 would be the darkstone (moves first)
    private int playerTurn;
    private boolean player1IsComputer = false;
    private boolean player2IsComputer = false;
    private String p1Name, p2Name;
    private boolean darkStoneMove2Taken = false;
    
    //we start the game like this...
    private boolean gameOver = false;  
    
    //Variables for Computer Game Players
    private ComputerMoveGenerator p1ComputerPlayer = null;
    private ComputerMoveGenerator p2ComputerPlayer = null;
    
    
    
    
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
                gameBoard[row][col].setWinningSquare(false);
            }
        } 
        //
        //this.paintImmediately(0, 0,  bWidth, bHeight);   // we want this
        repaint();
        
    }
    
    
    //first game will create dialog boxes to get player information
    //After that it skips the info
    public void startNewGame(boolean firstGame) {
        
        //No matter what, reset captures
        p1Captures = 0;
        p2Captures = 0;
        gameOver = false;
        darkStoneMove2Taken = false;
        
 
        if(firstGame) {
             p1Name = JOptionPane.showInputDialog("Name of player 1 (or type 'c' for computer");
             if(p1Name != null && (p1Name.toLowerCase().equals("c") || p1Name.toLowerCase().equals("computer") || 
                     p1Name.toLowerCase().equals("comp"))) {
                player1IsComputer = true;
                System.out.println("PLAYER 1 is a Computer");
                p1ComputerPlayer = new ComputerMoveGenerator(this, BLACKSTONE);
             }
        }
        
        myScoreBoard.setName(p1Name, BLACKSTONE);
        myScoreBoard.setCaptures(p1Captures, BLACKSTONE);
        
        
      if(firstGame) {
            p2Name = JOptionPane.showInputDialog("Name of player 2 (or type 'c' for computer");
            if(p2Name != null && (p2Name.toLowerCase().equals("c") || p2Name.toLowerCase().equals("computer") || 
                    p2Name.toLowerCase().equals("comp"))) {
                player2IsComputer = true;
                System.out.println("PLAYER 2 is a computer");
                p2ComputerPlayer = new ComputerMoveGenerator(this, WHITESTONE);
            }
      }
            myScoreBoard.setName(p2Name, WHITESTONE);
            myScoreBoard.setCaptures(p2Captures, WHITESTONE);
            
            resetBoard();   // moved here from the first line
        
            //We place the first dark stone here.
            playerTurn = PLAYER1_TURN;
            //This next line sets the center square as a dark square
            this.gameBoard[NUM_SQUARES_SIDE/2][NUM_SQUARES_SIDE/2].setState(BLACKSTONE);
            darkStoneMove2Taken = false;
            changePlayerTurn();
           
            checkForComputerMove(playerTurn);
       
            this.repaint();
            
     
       
    }
    
    
    public void changePlayerTurn() {
        playerTurn *= -1;
        //System.out.println("Its now the turn of: " + playerTurn);
        myScoreBoard.setPlayerTurn(playerTurn);
       
    }
    
    public boolean fiveInARow(int whichPlayer) {
        boolean isFive = false;
        //System.out.println("In top of fiveInARow and isFive is " + isFive);
        
        //we will write this ...now before lunch
        //FOR EVERY SQUARE ON THE BOARD....
        for(int row = 0; row < NUM_SQUARES_SIDE; row++ ) { 
            for(int col = 0; col < NUM_SQUARES_SIDE; col++) {
                //System.out.println("In fiveInRow, looking at [" + row + ", " + col + "]");
                for(int rL = -1; rL <= 1; rL++) { 
                    for(int uD = -1; uD <= 1; uD++) {
                        if(fiveCheck( row,  col, whichPlayer,   rL /* row */,  uD /*col */)) {
                            //System.out.println("In fiveInRow, found a 5 at [" + row + ", " + col + "]");
                            //System.out.println("FiveCheck is returning  true");
                            isFive = true;
                        }
                    }
                }
            }
        }   
        //System.out.println("In bottom of fiveInARow and isFive is " + isFive);
        return isFive;
    }
    
     
    public boolean fiveCheck( int r, int c, int pt, int upDown, int rightLeft ) {
        
       
   
        
        try {
            boolean found5 = false;  //like cap has to be a different variable
            //THIS CODE IS WRONG BUT YOU CAN CORRECT IT
            if(!(upDown == 0 && rightLeft == 0)) 
            {
                if( gameBoard[r][c].getState() == pt) 
                {                //1st
                    if(gameBoard[r+upDown][c+rightLeft].getState() == pt) //2nd
                    {             
                        if(gameBoard[r + (upDown*2)][c+(rightLeft*2)].getState() == pt)
                        {  
                            if(gameBoard[r + (upDown*3)][c+ (rightLeft*3)].getState() == pt) 
                            {
                                if(gameBoard[r + (upDown*4)][c+ (rightLeft*4)].getState() == pt) 
                                {
                                    System.out.println("IT'S a WIN" );
                                    
                                    found5 = true;
                                    gameBoard[r][c].setWinningSquare(true);
                                    gameBoard[r+upDown][c+rightLeft].setWinningSquare(true);
                                    gameBoard[r + (upDown*2)][c+(rightLeft*2)].setWinningSquare(true);
                                    gameBoard[r + (upDown*3)][c+ (rightLeft*3)].setWinningSquare(true);
                                    gameBoard[r + (upDown*4)][c+ (rightLeft*4)].setWinningSquare(true);
                                }
                            } 
                        }
                    }
                }
            }
            
            return found5; 
            
        }  catch (ArrayIndexOutOfBoundsException e) {
            //System.out.println("You have an error in five check" +   e.toString());
            return false;
        }   
    }
    
    
    public void checkForWin(int whichPlayer) {
        //System.out.println("At top of check for win for " + whichPlayer);
        //for player 1
        if(whichPlayer == this.PLAYER1_TURN) {
            if(this.p1Captures >= MAX_CAPTURES) {
                //we win!!
                JOptionPane.showMessageDialog(null, "Congratulations: " + p1Name +  " Wins!!" +
                        "\n with " + p1Captures + " captures");
                gameOver = true;
                
            } else {
                if(fiveInARow(whichPlayer)) {
                    //System.out.println("Back from  fiveInARow(); for P1 and its true");
                    JOptionPane.showMessageDialog(null, "Congratulations: " 
                            + p1Name + " Wins!! with 5 in a row");
                    gameOver = true;
                } 
            }
        } else {  //for player 2
            if(this.p2Captures >= MAX_CAPTURES) {
                //we win!!
                JOptionPane.showMessageDialog(null, "Congratulations: " + p2Name +  " Wins!!" +
                        "\n with " + p2Captures + " captures");
                gameOver = true;  
            } else {
                if(fiveInARow(whichPlayer)) {
                    //System.out.println("Back from  fiveInARow(); for P2 and its true");
                    JOptionPane.showMessageDialog(null, "Congratulations: " 
                            + p2Name + " Wins!! with 5 in a row");
                    gameOver = true;
                }
            } 
        }
    }
     
   
    //This checks on the board which square you have clicked on
    public void checkClick(int clickX, int clickY) {
        
        if(!gameOver) {
            for(int row = 0; row < NUM_SQUARES_SIDE; row++ ) { 
                for(int col = 0; col < NUM_SQUARES_SIDE; col++) {
                    
                    boolean squareClicked = gameBoard[row][col].isClicked(clickX, clickY);
                    if(squareClicked) {
                        //System.out.println("You clicked the square at [" + row + ", " + col + "]");    
                        if(gameBoard[row][col].getState() == EMPTY) {
                            //one more check to see about the second dark move
                            if(!darkSquareProblem(row, col)) {
                                gameBoard[row][col].setState(playerTurn);
                                this.paintImmediately(0,0, bWidth, bHeight);
                                checkForAllCaptures(row, col, playerTurn);
                                this.repaint();
                                /*  stops the wait on the human move */
                                //this.paintImmediately(0,0, bWidth, bHeight);
                                checkForWin(playerTurn);
                                this.changePlayerTurn();
                                checkForComputerMove(playerTurn);
                            } else {
                               JOptionPane.showMessageDialog(null, "Second dark stone move has to be outside of the light square");    
                            }
                        } else {
                            JOptionPane.showMessageDialog(null, "This square is taken, click on another");
                        }
                        
                    }
                }
            }   
        }
    }
    
    public void checkForComputerMove(int whichPlayer) {
        
        if(!gameOver) {
            if(whichPlayer == this.PLAYER1_TURN && this.player1IsComputer) {
                //System.out.println("PLAYER 1 is TAKING ITS TURN.....");
                int[] nextMove = this.p1ComputerPlayer.getComputeMove();
                int newR = nextMove[0];
                int newC = nextMove[1];
                gameBoard[newR][newC].setState(playerTurn);
                this.paintImmediately(0, 0, bWidth,  bHeight);  //added *****
                //this.repaint();
                checkForAllCaptures(newR, newC, playerTurn);
                this.repaint();
                checkForWin(playerTurn);
                if(!gameOver) {
                    this.changePlayerTurn();
                    checkForComputerMove(playerTurn);
                }
                
            } else if (whichPlayer == this.PLAYER2_TURN && this.player2IsComputer) {
                //System.out.println("PLAYER 2 is TAKING ITS TURN.....");
                int[] nextMove = this.p2ComputerPlayer.getComputeMove();
                int newR = nextMove[0];
                int newC = nextMove[1];
                gameBoard[newR][newC].setState(playerTurn);
                this.paintImmediately(0, 0, bWidth,  bHeight);  //added *****
                //this.repaint();
                checkForAllCaptures(newR, newC, playerTurn);
                //this.repaint();
                checkForWin(playerTurn);
                if(!gameOver) {
                    this.changePlayerTurn();
                    checkForComputerMove(playerTurn);
                }
            }
        }
        this.repaint();
        
      
    }
    
   /*  This method checks for the dark stone 2nd move issue
    *  WHAT IS A DARK STONE PROBLEM ******
    *  You have a dark stone move issue, if:
    *  
    *  1) darkStoneMove2Taken = false;  and
    *  2) playerTurn == the dark stone player and
    *  3) he or she tries to move into the inner circle
    *  
    */
    public boolean darkSquareProblem(int r, int c) {
        
        boolean dsp = false;
        
        if((!darkStoneMove2Taken) && (playerTurn == BLACKSTONE)) 
        {
           if( (r >= INNER_START && r <= INNER_END) && (c >= INNER_START && c <= INNER_END))
           {
                        dsp = true;     
           } else {
//               System.out.println(
//                       "in darkSquareProblem [" + r + ", " + c + "]" +
//                       " and about to set darkStoneMove2Taken to TRUE");
               darkStoneMove2Taken = true;
           }
        }        

        return dsp;
    }
    
    
 public boolean darkSquareProblemComputerMoveList(int r, int c) {
        
        boolean dsp = false;
        
        if((!darkStoneMove2Taken) && (playerTurn == BLACKSTONE)) 
        {
           if( (r >= INNER_START && r <= INNER_END) && (c >= INNER_START && c <= INNER_END))
           {
                        dsp = true;     
           } else {
               
              // darkStoneMove2Taken = true;
           }
        }        

        return dsp;
    }
    
    

    
    //This is a big routine to check for captures
    public void checkForAllCaptures(int r, int c, int pt) {
        
       boolean didCapture;
       //Horizontal Checks
       for(int rL = -1; rL <= 1; rL++) {
           for(int uD = -1; uD <= 1; uD++) {
               didCapture = checkForCaptures( r,  c,  pt,   rL /* row */,  uD /*col */); 
           }
       }
    }
    
    
    public boolean checkForCaptures(int r, int c, int pt, int upDown, int rightLeft) {
        
        try {
            boolean cap = false;
            
            if(gameBoard[r+upDown][c+rightLeft].getState() == pt*-1) {
                if(gameBoard[r + (upDown*2)][c+(rightLeft*2)].getState() == pt*-1) {
                    if(gameBoard[r + (upDown*3)][c+ (rightLeft*3)].getState() == pt) {
                        //System.out.println("IT'S A horizontal CAPTURE!!!" + rightLeft);
                        //Now let's take them off the board
                        gameBoard[r + upDown][c+rightLeft].setState(EMPTY);
                        gameBoard[r + (upDown*2)][c+(rightLeft*2)].setState(EMPTY);
                        cap = true;
                        if(pt == this.PLAYER1_TURN) {
                            p1Captures++;
                            myScoreBoard.setCaptures(p1Captures, playerTurn);
       
                        } else {
                            p2Captures++;
                            myScoreBoard.setCaptures(p2Captures, playerTurn);
                        }
                    } 
                }
            }
        
            return cap; 
        } catch(ArrayIndexOutOfBoundsException e) {
            //System.out.println("You have an error " +   e.toString());
            return false;
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
    
    //exposing or allowing access to the Gameboard
    public PenteBoardSquare[][] getBoard() {
        return gameBoard;
    }
    
    //getMethod for the darkStone variable getter method
    //accessor method
    public boolean getDarkStoneMove2Taken() {
        return darkStoneMove2Taken;
    }
    
    
    public boolean getGameOver() {
        return gameOver;
    }
    
    public int getPlayerCaptures(int whichPlayer) {
        if(whichPlayer == this.BLACKSTONE) {
            return p1Captures;
        } else {
            return p2Captures;
        }
        
    }

}
