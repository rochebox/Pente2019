import java.util.ArrayList;

public class ComputerMoveGenerator {
    
    public static final int OFFENSE = 1;
    public static final int DEFENSE = -1;
    
    
    
    PenteGameBoard myGame;
    int myStone;
    
    
    ArrayList<CMObject> oMoves = new ArrayList<CMObject>();
    ArrayList<CMObject> dMoves = new ArrayList<CMObject>();
    
    //probably need arrayLists
   
    // Contstructor(s)
    public ComputerMoveGenerator(PenteGameBoard gb, int stoneColor) {
        
        myStone = stoneColor;
        myGame = gb;
        
        System.out.println("Computer is playing as player " + myStone);
    }
    
    public int[] getComputeMove() {
        int[] newMove;
        
        findDefMoves();
        findOffMoves();
        
        newMove = generateRandomMove();
        // TON OF PROGAMMING TO PLAY THE GAME
        // LOTS
        // I mean lots.....
        
        
        
        try {
            sleepForAMove();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        return newMove;
    }
    
    public void findDefMoves() {
        
        findOneDef();
        //findTwoDef();
        //findThreeDef();
        //findFourDef();
        
    } 
    
    public void findOneDef() {
        //We start here on Wed
        
    }
    
    
    public void findOffMoves() {
        
    }
    
    public int[] generateRandomMove() {
        int[] move = new int[2]; // we will have row and col
        
        boolean done = false;
        
        int newR, newC;
        do {
           newR = (int)(Math.random() * PenteGameBoard.NUM_SQUARES_SIDE) ;
           newC = (int)(Math.random() * PenteGameBoard.NUM_SQUARES_SIDE) ;
           
           if(myGame.getBoard()[newR][newC].getState() == PenteGameBoard.EMPTY) {
               done = true;
               move[0] = newR;
               move[1] = newC;
           }
        } while(!done);

        
        return move;
    }
    
    
   public void sleepForAMove() throws InterruptedException {
        
        Thread currThread = Thread.currentThread();
        
        currThread.sleep(PenteGameBoard.SLEEP_TIME);
       
        
    }
    
    
    
}
