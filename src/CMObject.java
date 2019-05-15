
public class CMObject implements Comparable<Object> {
        
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
    
    public Integer getPriorityInt() {
        return new Integer(priority);
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
    
    public String toString() {
        
        return "Move at: [" + row + ", " + col + "] priority: " + priority ;
    }

    @Override
    public int compareTo(Object o) {
      
        int comparePriority=((CMObject)o).getPriority();
        /* For Descending order do like this */
        return comparePriority-this.priority;
        
    }



  
}
