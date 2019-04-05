import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;

public class PenteBoardSquare {
    //data
    private int xLoc, yLoc;
    private int sWidth, sHeight;
    
    private int sState; // Open, Player1, Player2
    
    private Color sColor; //color of the square sColor
    private Color lColor; //color of the cross line lColor
    private Color bColor; //color of boarder aroundS bColor
    private Color innerC; // for the inner 5 x 5 squares
    
    //private Color darkStoneColor = new Color(4, 9, 84);
    private Color darkStoneColor = new Color(4, 9, 64);
    private Color darkStoneTop = new Color(63, 69, 158);
    private Color darkStoneHighLight = new Color(188, 199, 255);
    
    private Color shadowGrey = new Color(169, 173, 142);
    private Color shadowGrey2 = new Color(158, 143, 99);
    private Color shadowGrey3 = new Color(150, 126, 57);
    
    private Color lightStoneColor = new Color(224, 222, 204);
    private Color lightStoneTop = new Color(250, 250, 250);
    
    private Color lightStoneColor2 = new Color(242, 240, 222);
    //need a white stone color
    boolean isInner = false;
    
    //constructor
    public PenteBoardSquare(int x, int y, int w, int h) {
        
        xLoc = x;
        yLoc = y;
        sWidth = w;
        sHeight = h;
        
        //the old original colors
        Color sColor2 = new Color(249, 218, 124);
        Color sColor3 = new Color(239, 205, 103);
        Color bColor2 = new Color(240, 240, 190);
        Color innerC2 = new Color (255, 238, 134);
        
        sColor = Color.ORANGE;
        lColor = new Color(83, 85, 89);
        bColor = new Color(244, 218, 44);   
        innerC = new Color(244, 229, 17);
        
        //bColor = Color.YELLOW;
        sState = PenteGameBoard.EMPTY;
        
        
    }
    
    public void setInner() {
        isInner = true;
    }
    
    public void drawMe(Graphics g) {
        
        // step 1 draw basic board color
        if(isInner) {
            g.setColor(innerC);
        } else {
            g.setColor(sColor);
        }
        g.fillRect(xLoc, yLoc, sWidth, sHeight);
        
        
        
        // step 2 --border color...
        g.setColor(bColor);
        g.drawRect(xLoc, yLoc, sWidth, sHeight);
        
        
        // NEW STEP 3  SHADOW
        if(sState != PenteGameBoard.EMPTY) {
            g.setColor(shadowGrey);
            g.fillOval(xLoc, yLoc + 6, sWidth-8, sHeight-8);
        }
        
        
        //Finish board....lines
        g.setColor(lColor);
        //Horizontal Line
        g.drawLine(xLoc, yLoc+ sHeight/2, xLoc + sWidth,  yLoc+ sHeight/2);
        //Vertical Line
        g.drawLine(xLoc + sWidth/2, yLoc, xLoc + sWidth/2, yLoc+sHeight);  
        
        
        //
        if(sState == PenteGameBoard.BLACKSTONE) {
            
            g.setColor(darkStoneColor);
            g.fillOval(xLoc + 4, yLoc + 4, sWidth-8, sHeight-8);
            
            //this is the top
            g.setColor(darkStoneTop);
            g.fillOval(xLoc + 8, yLoc + 6, sWidth-12, sHeight-10);
            
            //this is the reflection (shine)
            
            Graphics2D g2 = (Graphics2D) g;
            g2.setStroke(new BasicStroke(3));
           
            g2.setColor(darkStoneHighLight);
            
            //ADD THIS ONE LINE THE CHANGE STROKE BACK TO 1
            g2.setStroke(new BasicStroke(1));
            
            /*
            g.fillOval(
                    xLoc +(int)(sWidth*0.55),   //xLoc
                    yLoc + 10,                  //yLoc
                    (int)(sWidth *0.1),        //height
                    (int)(sHeight *0.15)        //width
                    );
            */
            
            g2.drawArc(xLoc +(int)(sWidth*0.45), 
                    yLoc + 10,
                    (int)(sWidth *0.30), 
             
                    (int)(sHeight *0.35), 
                    0, 
                    90
                    );
           
        }
        
        if(sState == PenteGameBoard.WHITESTONE) {
            g.setColor(lightStoneColor);
            g.fillOval(xLoc + 4, yLoc + 4, sWidth-8, sHeight-8);
            
            //this is the top
            g.setColor(lightStoneTop);
            g.fillOval(xLoc + 8, yLoc + 6, sWidth-12, sHeight-10);
        }
        
    }
    
    
    //methods
    // there can only be 3 states:
    /*  EMPTY = 0;
        BLACKSTONE = 1;
        WHITESTONE = -1;
    */
    public void setState(int newState) {
        if(newState < -1 || newState > 1) {
            System.out.println(newState + "is an illegal. State must be between -1 and 1");
        } else {
            sState = newState;
        }
    
    }
    
    
    public void setWidth(int newW) {
        sWidth = newW;
    }
    
    public void setHeight(int newH) {
        sHeight = newH;
    }
    
    public void setXLoc(int newX) {
        xLoc = newX;
    }
    
    public void setYLoc(int newY) {
        yLoc = newY;
    }
    
    //this is used with clickCheck in GameBoard to 
    //see if the x,y from a click is in the square.
    public boolean isClicked(int clickX, int clickY) {
        boolean didYouClickMe = false;
        
        if(  xLoc < clickX && clickX < xLoc + sWidth) {
            if(  yLoc < clickY && clickY < yLoc + sHeight) {
                didYouClickMe = true;
                
            }
            
        } 
       return didYouClickMe; 
    }
    
    
    
    

}
