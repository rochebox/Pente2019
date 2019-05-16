import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class ComputerMoveGenerator {
    
    public static final int OFFENSE = 1;
    public static final int DEFENSE = -1;
    public static final int NOT_ON_BOARD = -100;
    
    public static final int ONE_IN_ROW_DEF = 10;
    public static final int ONE_IN_ROW_OFF = 11;
    
    
    public static final int TWO_IN_ROW_DEF = 20;
    public static final int TWO_IN_ROW_OPEN = 21;
    public static final int TWO_IN_ROW_CAP = 22;  // We will adjust
    
    public static final int THREE_IN_ROW_ENCLOSE = 30;
    public static final int THREE_1_1_1_EXTREME_DEF = 31;
    public static final int THREE_IN_ROW_OPEN_DEF = 32;
    public static final int THREE_1_2_EXTREME_DEF = 32;
    public static final int THREE_2_1_EXTREME_DEF = 33;
    
    
    PenteGameBoard myGame;
    int myStone;
    
  
    ArrayList<CMObject> allMoves = new ArrayList<CMObject>();
    
    //probably need arrayLists
   
    // Contstructor(s)
    public ComputerMoveGenerator(PenteGameBoard gb, int stoneColor) {
        
        myStone = stoneColor;
        myGame = gb;
        
        System.out.println("Computer is playing as player " + myStone);
    }
    
    public void sortPriorities() {
        //Here we are going to sort the priorities..
        //Comparator<CMObject>  compareByPriority = (CMObject o1, CMObject o2) ->
        //o1.getPriorityInt().compareTo( o2.getPriorityInt() );   
        
        Collections.sort(allMoves);


    }
    
    public int[] getComputeMove() {
        //Initializing stuff...
        int[] newMove = new int[2];
        newMove[0] = -1;
        newMove[1] = -1;
        
        allMoves.clear();
        
        
        //Find all your moves...
        findMoves();  //dMoves will be filled
        sortPriorities();
        
        printPriorities();
        System.out.println();
        
        
        if(allMoves.size() > 0) {
            //Testing
            //int whichOne = (int)(Math.random() * dMoves.size());
            CMObject ourMove; 
            
            if(allMoves.get(0).getPriority() <= this.ONE_IN_ROW_DEF ) {
                ourMove = allMoves.get((int)(Math.random()*allMoves.size()));
            } else {
                ourMove = allMoves.get(0);
            }
            newMove[0] = ourMove.getRow();
            newMove[1] = ourMove.getCol();
            
            if(myGame.darkSquareProblem(newMove[0], newMove[1]) == true) {
                System.out.println("CRUD YOU REALLY HAVE PROBLEM, BECAUSE YOUR FINAL MOVE IS A DARK SQUARE PROBLEM");
            }
            
        } else {
            //Special Situation for rule against inner move on first move
            if(myStone == PenteGameBoard.BLACKSTONE && myGame.getDarkStoneMove2Taken() == false) {       
                //System.out.println("In getComputerMove(), myStone is DARK and there is DSProblem"); 
                        
                       int newBStoneProbRow = -1;
                       int newBStoneProbCol = -1;             
                       int innerSafeSquareSideLen = PenteGameBoard.INNER_END - PenteGameBoard.INNER_START + 1;         
                       //System.out.println("InnerSafeSquareSideLen = " + innerSafeSquareSideLen);
                       //System.out.println("InnerSafeSquareSideLen + 1 = " + (innerSafeSquareSideLen + 1) );
                       //System.out.println("InnerSafeSquareSideLen + 2 = " + (innerSafeSquareSideLen + 2) );
                       while(myGame.getDarkStoneMove2Taken() == false) {
                       
          
                           newBStoneProbRow = (int) (Math.random() * (innerSafeSquareSideLen + 2)  )
                                           + (innerSafeSquareSideLen + 1);
                           
                           newBStoneProbCol = (int) (Math.random() * (innerSafeSquareSideLen + 2)  )
                                   + (innerSafeSquareSideLen + 1);
                           
                           //System.out.println("To Solve Problem, trying: [" + 
                           //       newBStoneProbRow + ", " + newBStoneProbCol + "]"
                           //               );
                           myGame.darkSquareProblem(newBStoneProbRow, newBStoneProbCol);
                       }
                       newMove[0] = newBStoneProbRow;
                       newMove[1] = newBStoneProbCol;     
            } else {              
                System.out.println("HI I'm just generating a random move");
                newMove = generateRandomMove();
            }
        }
        
    
        
        
        
        try {
            sleepForAMove();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        return newMove;
    }
    
    public void printPriorities() {

        for(CMObject m: allMoves){    //loop for iterables
             System.out.println(m);
        }
    }
    
    public void findMoves() {
        
        for(int row = 0; row < PenteGameBoard.NUM_SQUARES_SIDE; row++ ) { 
            for(int col = 0; col < PenteGameBoard.NUM_SQUARES_SIDE; col++) {
                if(myGame.getBoard()[row][col].getState() == myStone * -1) {
                    
                    findThreeDefExtreme1_2(row, col);
                    findThreeDefExtreme1_1_1(row, col);
                    findThreeDef(row, col);
                    findTwoDef(row, col);
                    findOneDef(row, col);
               
                    // findFourDef(row, col);
                }
                
                if(myGame.getBoard()[row][col].getState() == myStone) {
                    
                     findOneOff(row, col);
                    // findFourDef(row, col);
                }  
            }
        }    
    } 
    

    //this finds all instances of possible moves to block one stone
    public void findOneDef(int r, int c) {
        //We start here on Wed
        //This runs in the 8 directions (9)
        for(int rL = -1; rL <= 1; rL++) {
            for(int uD = -1; uD <= 1; uD++) {
                try {
                    
                    if(myGame.getBoard()[r + rL][c + uD].getState() == PenteGameBoard.EMPTY ) {
                        this.setMove(r + rL, c + uD, this.ONE_IN_ROW_DEF, DEFENSE);            
                    }
                    
                } catch (ArrayIndexOutOfBoundsException e) {
                    System.out.println("Off the board in findOneDef at [" + r + "," + c + "]");
                }
                
            }
        }
    }
    
    public boolean isOnBoard(int r, int c) {     
        boolean isOn = false;     
        if(r >= 0 && r < PenteGameBoard.NUM_SQUARES_SIDE) {
            if(c >= 0 && c < PenteGameBoard.NUM_SQUARES_SIDE) {
                isOn = true;
            }
        }
        return isOn;
    }
    
    //formerly setDefMove...
    public void setMove(int r, int c, int p, int type) {
        
        System.out.println(" in set  move the dark stone move is " + myGame.getDarkStoneMove2Taken());
        
        if(myStone == PenteGameBoard.BLACKSTONE && myGame.getDarkStoneMove2Taken() == false) {
            
            if(myGame.darkSquareProblemComputerMoveList(r, c) == false) {
                CMObject newMove = new CMObject();
                newMove.setRow(r);
                newMove.setCol(c);
                newMove.setPriority(p);
                newMove.setMoveType(type);
                allMoves.add(newMove);
            }  else {
                System.out.println("There is a dark square problem: ");
                System.out.println("\tmove at [" + r +", " + c + "] is being thrown out");
            }
            
        } else {
            
            //the whole game moves like it used to except for one move....
            CMObject newMove = new CMObject();
            newMove.setRow(r);
            newMove.setCol(c);
            newMove.setPriority(p);
            newMove.setMoveType(type);
            allMoves.add(newMove);
        }
        
    }
    
    public void findThreeDef(int r, int c) {
         findThreeDefNormal( r,  c );
         findThreeDefExtreme1_2( r,  c );
        
    }
    
    
    public void findThreeDefNormal(int r, int c) {
        
      //We start here on Wed
        //This runs in the 8 directions (9)
        for(int rL = -1; rL <= 1; rL++) {
            for(int uD = -1; uD <= 1; uD++) {
                try {
                    
                    if(myGame.getBoard()[r + rL][c + uD].getState() == myStone * -1 ) {
                        if(myGame.getBoard()[r + (rL *2)][c + (uD*2)].getState() == myStone * -1 ) {
                            if(myGame.getBoard()[r + (rL *3)][c + (uD*3)].getState() == PenteGameBoard.EMPTY ) {
                        
                                //if r-rl is the wall 
                                if(isOnBoard(r-rL, c-uD) == false) {
                                    setMove(
                                            r + (rL* 3), 
                                            c + (uD * 3),
                                            THREE_IN_ROW_ENCLOSE, DEFENSE );
                                   
                                } else if (
                                        myGame.getBoard()[r - rL][c -uD].getState() == PenteGameBoard.EMPTY  
                                        ) {
                                    setMove(
                                            r + (rL* 3), 
                                            c + (uD * 3),
                                            this.THREE_IN_ROW_OPEN_DEF, DEFENSE);
                                    
                                } else if (
                                        myGame.getBoard()[r - rL][c -uD].getState() == myStone 
                                        ){
                                    setMove(
                                            r + (rL* 3), 
                                            c + (uD * 3),
                                            this.THREE_IN_ROW_ENCLOSE, DEFENSE);
                                    
                                }
                            }
                        }
                    }
                }
                            
                              
                 catch (ArrayIndexOutOfBoundsException e) {
                    System.out.println("Off the board in findOneDef at [" + r + "," + c + "]");
                }
                
            }
        }   
    }
    
    
    
    public void findThreeDefExtreme1_2(int r, int c) {
        
        //We start here on Wed
          //This runs in the 8 directions (9)
          for(int rL = -1; rL <= 1; rL++) {
              for(int uD = -1; uD <= 1; uD++) {
                  try {
                      
                      if(myGame.getBoard()[r + rL][c + uD].getState() == PenteGameBoard.EMPTY ) {
                          if(myGame.getBoard()[r + (rL *2)][c + (uD*2)].getState() == myStone * -1 ) {
                              if(myGame.getBoard()[r + (rL *3)][c + (uD*3)].getState() == myStone * -1 ) {
                          
                                  setMove(
                                          r + (rL), 
                                          c + (uD),
                                          this.THREE_1_2_EXTREME_DEF, DEFENSE);
                                  
                              }
                          }
                      }
                  }
                              
                                
                   catch (ArrayIndexOutOfBoundsException e) {
                      System.out.println("Off the board in findOneDef at [" + r + "," + c + "]");
                  }
                  
              }
          }    
      }
    
    
public void findThreeDefExtreme2_1(int r, int c) {
        
        //We start here on Wed
          //This runs in the 8 directions (9)
          for(int rL = -1; rL <= 1; rL++) {
              for(int uD = -1; uD <= 1; uD++) {
                  try {
                      
                      if(myGame.getBoard()[r + rL][c + uD].getState() == myStone * -1 ) {
                          if(myGame.getBoard()[r + (rL *2)][c + (uD*2)].getState() == PenteGameBoard.EMPTY  ) {
                              if(myGame.getBoard()[r + (rL *3)][c + (uD*3)].getState() == myStone * -1 ) {
                          
                                  setMove(
                                          r + (rL * 2), 
                                          c + (uD * 2),
                                          this.THREE_2_1_EXTREME_DEF, DEFENSE);
                              }
                          }
                      }
                  }
                              
                                
                   catch (ArrayIndexOutOfBoundsException e) {
                      System.out.println("Off the board in findOneDef at [" + r + "," + c + "]");
                  }
                  
              }
          }    
      }


public void findThreeDefExtreme1_1_1(int r, int c) {
    
    //We start here on Wed
      //This runs in the 8 directions (9)
      for(int rL = -1; rL <= 1; rL++) {
          for(int uD = -1; uD <= 1; uD++) {
              try {
                  
                  if(myGame.getBoard()[r + rL][c + uD].getState() == PenteGameBoard.EMPTY ) {
                      if(myGame.getBoard()[r + (rL *2)][c + (uD*2)].getState() == myStone * -1 ) {
                          if(myGame.getBoard()[r + (rL*3)][c + (uD*3)].getState() == PenteGameBoard.EMPTY ) {
                              if(myGame.getBoard()[r + (rL *4)][c + (uD*4)].getState() == myStone * -1 ) {
                      
                                if((int)(Math.random()*100) < 50) {
                                    setMove(
                                            r + (rL), 
                                            c + (uD),
                                            this.THREE_1_1_1_EXTREME_DEF, DEFENSE);
                                } else {
                                    setMove(
                                            r + (rL*3), 
                                            c + (uD*3),
                                            this.THREE_1_1_1_EXTREME_DEF, DEFENSE);
                                    
                                }
                              
                              
                              }
                          }
                      }
                  }
              }
                          
                            
               catch (ArrayIndexOutOfBoundsException e) {
                  System.out.println("Off the board in findOneDef at [" + r + "," + c + "]");
              }
              
          }
      }    
  }
    
        public void findTwoDef(int r, int c) {
            //We start here on Wed
            //This runs in the 8 directions (9)
            for(int rL = -1; rL <= 1; rL++) {
                for(int uD = -1; uD <= 1; uD++) {
                    try {
                        
                        if(myGame.getBoard()[r + rL][c + uD].getState() == myStone * -1 ) {
                            if(myGame.getBoard()[r + (rL *2)][c + (uD*2)].getState() == PenteGameBoard.EMPTY ) {
                        
                                //if r-rl is the wall 
                                if(isOnBoard(r-rL, c-uD) == false) {
                                    setMove(
                                            r + (rL* 2), 
                                            c + (uD * 2),
                                            TWO_IN_ROW_DEF, DEFENSE );
                                   
                                } else if (
                                        myGame.getBoard()[r - rL][c -uD].getState() == PenteGameBoard.EMPTY  
                                        ) {
                                    setMove(
                                            r + (rL* 2), 
                                            c + (uD * 2),
                                            this.TWO_IN_ROW_OPEN, DEFENSE );
                                    
                                } else if (
                                        myGame.getBoard()[r - rL][c -uD].getState() == myStone 
                                        ){
                                    setMove(
                                            r + (rL* 2), 
                                            c + (uD * 2),
                                            this.TWO_IN_ROW_CAP, DEFENSE);
                                    
                                }
                                
                                
                                
                               //if r-rl, c-uD is open 
                                
                                
                               //if r-rl, c-uD is us..
                                
                                
                             
                                
                                //if r-rl, c-uD is opponent (we don't care)
                                
                                
                                
                                
//                                CMObject newMove = new CMObject();
//                                newMove.setRow(r + (rL* 2));
//                                newMove.setCol(c + (uD * 2));
//                                newMove.setPriority(ONE_IN_ROW_DEF);
//                                newMove.setMoveType(DEFENSE);
//                                dMoves.add(newMove);
                            
                            }
                        }
                        
                    } catch (ArrayIndexOutOfBoundsException e) {
                        System.out.println("Off the board in findOneDef at [" + r + "," + c + "]");
                    }
                    
                }
            }
        
        
        
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
    
  
   public void goOutSevenAndSee(int r, int c, int rL, int uD) {
       int[] goOut = new int[7];
       
       for(int i = 0; i < 7; i++) {
           if( !this.isOnBoard(r + (rL * i), c + (uD * i)) ) {
               goOut[i] = this.NOT_ON_BOARD;
           } else {
               PenteBoardSquare[][] b = myGame.getBoard();
               goOut[i] = b[r + (rL * i)][c + (uD * i)].getState();
           }
       }
          
   }
   
   //OFFENSE BABY!!!
   //this finds all instances of possible moves to block one stone
   public void findOneOff(int r, int c) {
       //We start here on Wed
       //This runs in the 8 directions (9)
       for(int rL = -1; rL <= 1; rL++) {
           for(int uD = -1; uD <= 1; uD++) {
               try {
                   
                   if(myGame.getBoard()[r + rL][c + uD].getState() == PenteGameBoard.EMPTY ) {
                       this.setMove(r + rL, c + uD, this.ONE_IN_ROW_OFF, OFFENSE);   
                       
                       int addPriorityForOneOff
                   }
                   
               } catch (ArrayIndexOutOfBoundsException e) {
                   System.out.println("Off the board in findOneDef at [" + r + "," + c + "]");
               }
               
           }
       }
       }
    
}
