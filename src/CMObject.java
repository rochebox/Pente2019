
public class CMObject {
        
    //Data
    private int priority = 0;
    private int row = -1, col = -1;
    private int moveType = 0; //Offense or Defense
    
    
    //I HAVE NO CONSTRUCTOR SO I CAN USE THE DEFAULT ONE
    
    //I Set Methods....Mutators... Setters
    public void setPriority(int newP) {
        priority = newP;
    }
    
    public void setRow (int newR) {
        row = newR;
    }
    
    public void setCol(int newC) {
        col = newC;  
    }
    
    public void setMoveType(int newT) {
        moveType = newT;
    }
    
    
    
    
    //Set Accessor Methods Getters
    public int getPriority() {
        return priority;
    }
    
    public int getRow() {
        return row;
    }
    
    public int getCol() {
        return col;
    }
    
    public int getType() {
        return moveType;
    }
  
}
