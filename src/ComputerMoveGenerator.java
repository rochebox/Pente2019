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
    public static final int TWO_IN_ROW_CAP = 28;  // We will adjust
    
    public static final int TWO_2_THREE_OFF_WALL_LOW_P = 20; //two in row + wall low priority
    public static final int TWO_2_OPEN_THREE_GOOD = 32;
    public static final int TWO_2_THREE_TO_BLOCK_WIN_BY_CAPTURE = 40;
    public static final int TWO_2_THEEE_TO_BLOCK_GROWING_OPP_CAPTURE = 27;
    public static final int TWO_2_THEEE_TO_BLOCK_CAPTURE_NORMAL = 26;
    public static final int TWO_2_THREE_SETTING_FOR_CAPTURE = 28;
    public static final int TWO_2_THREE_2_SET_UNGUARDED_THREE_STEALTH = 32;
    
    public static final int THREE_IN_ROW_ENCLOSE = 30;
    public static final int THREE_1_1_1_EXTREME_DEF = 31;
    public static final int THREE_IN_ROW_OPEN_DEF = 33;
    public static final int THREE_1_2_EXTREME_DEF = 33;
    public static final int THREE_2_1_EXTREME_DEF = 34;
    
    public static final int THREE_IN_ROW_OFF_WALL_OSIDE = 30;
    public static final int THREE_IN_ROW_OPEN_OFFENSE = 39;
    public static final int THREE_IN_ROW_OFF_OPPONENT_OSIDE = 30;
   
    
    public static final int FOUR_IN_ROW_OFF_WIN = 42;
    public static final int FOUR_IN_ROW_ENCLOSE_DEF = 41;
    public static final int FOUR_IN_ROW_HOPELESS_DEF = 39; //not so great...
    
    
    PenteGameBoard myGame;
    int myStone;
    
    int moveCount = 0;
    
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
        moveCount++;
        System.out.println("We have generated move number: " + moveCount);
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
                    
                    findFourDefNormal(row, col);
                    findThreeDef(row, col);  //this calls the 3 combos
                    findTwoDef(row, col);
                    findOneDef(row, col);
               
                    // findFourDef(row, col);
                } else if(myGame.getBoard()[row][col].getState() == myStone) {
                    
                     //one moving to two
                     findOneOff(row, col);
                     //one two moving to three
                     findTwoOffNormal(row, col);
                     //three moving to four
                     findThreeOffNormal(row, col);
                     //four moving to 5
                     for(int el = 1; el <=4; el++) {
                         findFourOffAll(row, col, el);
                     }
                     
                     //findFourOffNormal(row, col);
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
        
        //System.out.println(" in set  move the dark stone move is " + myGame.getDarkStoneMove2Taken());
        
        if(myStone == PenteGameBoard.BLACKSTONE && myGame.getDarkStoneMove2Taken() == false) {
            
            if(myGame.darkSquareProblemComputerMoveList(r, c) == false) {
                CMObject newMove = new CMObject();
                newMove.setRow(r);
                newMove.setCol(c);
                newMove.setPriority(p);
                newMove.setMoveType(type);
                allMoves.add(newMove);
            }  else {
                //System.out.println("There is a dark square problem: ");
                //System.out.println("\tmove at [" + r +", " + c + "] is being thrown out");
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
    
    
    public void findFourDefNormal(int r, int c) {
        
          //This runs in the 8 directions (9)
          for(int rL = -1; rL <= 1; rL++) {
              for(int uD = -1; uD <= 1; uD++) {
                  try {
                      
                      if(myGame.getBoard()[r + rL][c + uD].getState() == myStone * -1 ) {
                          if(myGame.getBoard()[r + (rL *2)][c + (uD*2)].getState() == myStone * -1 ) {
                              if(myGame.getBoard()[r + (rL *3)][c + (uD*3)].getState() == myStone * -1 ) {
                                  if(myGame.getBoard()[r + (rL *4)][c + (uD*4)].getState() == PenteGameBoard.EMPTY ) {
                          
                                  //if r-rl is the wall 
                                  if(isOnBoard(r-rL, c-uD) == false) {
                                      setMove(
                                              r + (rL* 4), 
                                              c + (uD * 4),
                                              FOUR_IN_ROW_ENCLOSE_DEF, DEFENSE );
                                     
                                  } else if (
                                          myGame.getBoard()[r - rL][c -uD].getState() == PenteGameBoard.EMPTY  
                                          ) {
                                      setMove(
                                              r + (rL* 4), 
                                              c + (uD * 4),
                                              this.FOUR_IN_ROW_HOPELESS_DEF, DEFENSE);
                                      
                                  } else if (
                                          myGame.getBoard()[r - rL][c -uD].getState() == myStone 
                                          ){
                                      setMove(
                                              r + (rL* 4), 
                                              c + (uD * 4),
                                              FOUR_IN_ROW_ENCLOSE_DEF, DEFENSE);
                                      
                                  }
                              }
                          }
                      }
                  }
                  }            
                                
                   catch (ArrayIndexOutOfBoundsException e) {
                      //System.out.println("Off the board in findOneDef at [" + r + "," + c + "]");
                  }
                  
              }
          }   
      }
    
    public void findThreeDef(int r, int c) {
         findThreeDefNormal( r,  c );
         findThreeDefExtreme1_2( r,  c );
         findThreeDefExtreme1_1_1(r, c);
        
        
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
                    //System.out.println("Off the board in findOneDef at [" + r + "," + c + "]");
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
                     // System.out.println("Off the board in findOneDef at [" + r + "," + c + "]");
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
                      //System.out.println("Off the board in findOneDef at [" + r + "," + c + "]");
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
                  //System.out.println("Off the board in findOneDef at [" + r + "," + c + "]");
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
                                

                            
                            }
                        }
                        
                    } catch (ArrayIndexOutOfBoundsException e) {
                        //System.out.println("Off the board in findOneDef at [" + r + "," + c + "]");
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
   /* Look around your one position move see if you can make combinations */
   public int oneOffHasNeighbor(int r, int c) {
       
       int highestP = 0;
       int priorityUp = 0;
       for(int rL = -1; rL <= 1; rL++) {
           for(int uD = -1; uD <= 1; uD++) {
               priorityUp = 0;
               try {
                   if(myGame.getBoard()[r + rL][c + uD].getState() == myStone ) {
                       priorityUp +=1;
                   }
                   if(myGame.getBoard()[r + (rL*2)][c + (uD*2)].getState() == myStone ) {
                       priorityUp +=2;
                   }
                   if(myGame.getBoard()[r + (rL*3)][c + (uD*3)].getState() == myStone ) {
                       priorityUp +=3;
                   }
               } catch(ArrayIndexOutOfBoundsException  e) {
                 //System.out.println("In PriorityUp1 and out of bounds exception [" + r + "," + c + "]");
               } 
               if(priorityUp > highestP) {
                   highestP = priorityUp;
               }
           }
       }
       
       
       
       return priorityUp;
   }
   public void findOneOff(int r, int c) {
       //We start here on Wed
       //This runs in the 8 directions (9)
       for(int rL = -1; rL <= 1; rL++) {
           for(int uD = -1; uD <= 1; uD++) {
               try {
                   
                   int priorityUpgrade = 0;
                   
                   if(myGame.getBoard()[r + rL][c + uD].getState() == PenteGameBoard.EMPTY ) {
                       
                      priorityUpgrade =  oneOffHasNeighbor(r + rL, c + uD );
                       
                       this.setMove(r + rL, c + uD, ONE_IN_ROW_OFF+priorityUpgrade, OFFENSE );  

                   }
                   
               } catch (ArrayIndexOutOfBoundsException e) {
                   //System.out.println("Off the board in findOneDef at [" + r + "," + c + "]");
               }
               
           }
       }
       }
   
   
   public void findThreeOffNormal(int r, int c) {
       
       //We start here on Wed
         //This runs in the 8 directions (9)
         for(int rL = -1; rL <= 1; rL++) {
             for(int uD = -1; uD <= 1; uD++) {
                 try {
                     
                     if(myGame.getBoard()[r + rL][c + uD].getState() == myStone  ) {
                         if(myGame.getBoard()[r + (rL *2)][c + (uD*2)].getState() == myStone  ) {
                             if(myGame.getBoard()[r + (rL *3)][c + (uD*3)].getState() == PenteGameBoard.EMPTY ) {
                         
                                 //if r-rl is the wall 
                                 if(isOnBoard(r-rL, c-uD) == false) {
                                     setMove(
                                             r + (rL* 3), 
                                             c + (uD * 3),
                                             THREE_IN_ROW_OFF_WALL_OSIDE, OFFENSE );
                                    
                                 } else if (
                                         myGame.getBoard()[r - rL][c -uD].getState() == PenteGameBoard.EMPTY  
                                         ) {
                                     setMove(
                                             r + (rL* 3), 
                                             c + (uD * 3),
                                             this.THREE_IN_ROW_OPEN_OFFENSE, OFFENSE);
                                     
                                 } else if (
                                         myGame.getBoard()[r - rL][c -uD].getState() == (myStone  * -1)
                                          ){
                                     setMove(
                                             r + (rL* 3), 
                                             c + (uD * 3),
                                             THREE_IN_ROW_OFF_OPPONENT_OSIDE, OFFENSE);
                                     
                                 }
                             }
                         }
                     }
                 }
                             
                               
                  catch (ArrayIndexOutOfBoundsException e) {
                     //System.out.println("Off the board in findOneDef at [" + r + "," + c + "]");
                 }
                 
             }
         }   
     }
   
   
   
public void findFourOffNormal(int r, int c) {
       
       //We start here on Wed
         //This runs in the 8 directions (9)
         for(int rL = -1; rL <= 1; rL++) {
             for(int uD = -1; uD <= 1; uD++) {
                 try {
                     
                     if(myGame.getBoard()[r + rL][c + uD].getState() == myStone  ) {
                         if(myGame.getBoard()[r + (rL *2)][c + (uD*2)].getState() == myStone  ) {
                             if(myGame.getBoard()[r + (rL *3)][c + (uD*3)].getState() == myStone ) {
                                 if(myGame.getBoard()[r + (rL *4)][c + (uD*4)].getState() == PenteGameBoard.EMPTY ) {
                                 
                                     setMove(
                                             r + (rL* 4), 
                                             c + (uD * 4),
                                             FOUR_IN_ROW_OFF_WIN, OFFENSE );
                                    
                                 }
                             }
                         }
                     }
                 }
                             
                               
                  catch (ArrayIndexOutOfBoundsException e) {
                    // System.out.println("Off the board in findOneDef at [" + r + "," + c + "]");
                 }
                 
             }
         }   
     }

public void findFourOffExtreme1(int r, int c) {
    
    //We start here on Wed
      //This runs in the 8 directions (9)
      for(int rL = -1; rL <= 1; rL++) {
          for(int uD = -1; uD <= 1; uD++) {
              try {
                  
                  if(myGame.getBoard()[r + rL][c + uD].getState() == PenteGameBoard.EMPTY  ) {
                      if(myGame.getBoard()[r + (rL *2)][c + (uD*2)].getState() == myStone  ) {
                          if(myGame.getBoard()[r + (rL *3)][c + (uD*3)].getState() == myStone ) {
                              if(myGame.getBoard()[r + (rL *4)][c + (uD*4)].getState() == myStone ) {
                              
                                  setMove(
                                          r + (rL* 4), 
                                          c + (uD * 4),
                                          FOUR_IN_ROW_OFF_WIN, OFFENSE );
                                 
                              }
                          }
                      }
                  }
              }
                          
                            
               catch (ArrayIndexOutOfBoundsException e) {
                  //System.out.println("Off the board in findOneDef at [" + r + "," + c + "]");
              }
              
          }
      }   
  }


public void findFourOffAll(int r, int c, int emptyLocation) {
    
    //We start here on Wed
      //This runs in the 8 directions (9)
      for(int rL = -1; rL <= 1; rL++) {
          for(int uD = -1; uD <= 1; uD++) {
              try {
                  
                  if(
                          (emptyLocation == 1 && myGame.getBoard()[r + rL][c + uD].getState() == PenteGameBoard.EMPTY) ||
                          (emptyLocation != 1 && myGame.getBoard()[r + rL][c + uD].getState() == myStone)
                          ) {
                      if(
                             (emptyLocation == 2 && myGame.getBoard()[r + (rL*2)][c + (uD*2)].getState() == PenteGameBoard.EMPTY) ||
                             (emptyLocation != 2 && myGame.getBoard()[r + (rL*2)][c + (uD*2)].getState() == myStone) 
                              ) {
                          if(
                                  (emptyLocation == 3 && myGame.getBoard()[r + (rL*3)][c + (uD*3)].getState() == PenteGameBoard.EMPTY) ||
                                  (emptyLocation != 3 && myGame.getBoard()[r + (rL*3)][c + (uD*3)].getState() == myStone) 
                                  ) {
                              if(
                                      (emptyLocation == 4 && myGame.getBoard()[r + (rL*4)][c + (uD*4)].getState() == PenteGameBoard.EMPTY) ||
                                      (emptyLocation != 4 && myGame.getBoard()[r + (rL*4)][c + (uD*4)].getState() == myStone) 
                                      ) {
                              
                                  setMove(
                                          r + (rL* emptyLocation), 
                                          c + (uD * emptyLocation),
                                          FOUR_IN_ROW_OFF_WIN, OFFENSE );
                                 
                              }
                          }
                      }
                  }
              }
                          
                            
               catch (ArrayIndexOutOfBoundsException e) {
                  //System.out.println("Off the board in findOneDef at [" + r + "," + c + "]");
              }
              
          }
      }   
  }


public void findTwoOffNormal(int r, int c) {
    
    for(int rL = -1; rL <= 1; rL++) {
        for(int uD = -1; uD <= 1; uD++) {
            try {
                
                if(myGame.getBoard()[r + rL][c + uD].getState() == myStone ) {
                    if(myGame.getBoard()[r + (rL *2)][c + (uD*2)].getState() == PenteGameBoard.EMPTY ) {
                
                        
                        //if r-rl is the wall 
                        if(isOnBoard(r-rL, c-uD) == false) {
                            setMove(
                                    r + (rL* 2), 
                                    c + (uD * 2),
                                    TWO_2_THREE_OFF_WALL_LOW_P, OFFENSE );
                            
                        //if r-rl is myStone then this is really a 3 so skip it (other code picks this up)   
                        } else if (
                                myGame.getBoard()[r - rL][c -uD].getState() == myStone  
                                ) {
                            
                                   //MAKING NO MOVE BECAUSE THIS WILL BE PICKED UP BY 3                
                                   
                        //if r-rl is opponent then this is a blocking capture situation and there are different concerns  
                        } else if (
                                myGame.getBoard()[r - rL][c -uD].getState() == (myStone* -1)  
                                ) {
                            
                                //Here we need to look at captures
                                if(myGame.getPlayerCaptures(myStone *-1) > 3) {
                                    setMove(
                                          r + (rL* 2), 
                                          c + (uD * 2),
                                          this.TWO_2_THREE_TO_BLOCK_WIN_BY_CAPTURE, OFFENSE );
                                    
                                } else if (myGame.getPlayerCaptures(myStone * -1) > 2) {
                                    setMove(
                                            r + (rL* 2), 
                                            c + (uD * 2),
                                            this.TWO_2_THEEE_TO_BLOCK_GROWING_OPP_CAPTURE, OFFENSE );
                                } else {
                                    setMove(
                                            r + (rL* 2), 
                                            c + (uD * 2),
                                            this.TWO_2_THEEE_TO_BLOCK_CAPTURE_NORMAL, OFFENSE );
                                }
                                    
                         } else {
                             
                           //HERE you have two in a row and EMPTY on either side
                           //This could be an unguarded three, now see if its any good
                             //Case 1 -- The fifth stone moving positively is a wall....
                             if(!this.isOnBoard( r+ (rL* 3), c + (uD*3) ) ) {
                                 //Then look one more in the opposite directions and deal
                                 if(!this.isOnBoard( r+ (rL* -2), c + (uD*-2) )) {
                                     //This is an irrational case, so don't do anything
                                 } else {
                                     //OK SO r + 2rL and c + 2uD is on the board
                                     if(
                                             myGame.getBoard()[r - (rL*-2)][c - (uD*-2)].getState() == myStone  
                                             ) {
                                              // ****This is really a three which will be picked up by other software
                                         
                                     } else if (
                                             myGame.getBoard()[r - (rL*-2)][c - (uD*-2)].getState() == (myStone *-1)  
                                             ) {
                                             //here this is setting up for a potential capture next two an opponent
                                         setMove(
                                                 r + (rL* -1), 
                                                 c + (uD * -1),
                                                 this.TWO_2_THREE_SETTING_FOR_CAPTURE, OFFENSE );
                                         
                                     } else {
                                         //THis is the final case where we are setting up a three but stealthy
                                         setMove(
                                                 r + (rL* -2), 
                                                 c + (uD * -2),
                                                 this.TWO_2_THREE_2_SET_UNGUARDED_THREE_STEALTH, OFFENSE );
                                         
                                     }
                                 }
                                 
                                
                             } else {
                                 
                                 if(myGame.getBoard()[r + (rL *3)][c + (uD*3)].getState() == PenteGameBoard.EMPTY ) {
                                     setMove(
                                             r + (rL* 2), 
                                             c + (uD * 2),
                                             this.TWO_2_OPEN_THREE_GOOD, OFFENSE );
                                 } 
                                 
                             }
                                    
                        }
                         
                    
                    }
                }
                
            } catch (ArrayIndexOutOfBoundsException e) {
                //System.out.println("Off the board in findOneDef at [" + r + "," + c + "]");
            }
            
        }
    }



}

     
    
}
