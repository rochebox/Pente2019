import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

public class PenteScore extends JPanel implements ActionListener{
    
    //Data
    //Height Normally is 722
    private JLabel p1Name = new JLabel( "              ");
    private JLabel p2Name = new JLabel("              ");
    private JTextField p1Captures, p2Captures;
    private JTextField whoseTurnField;
    
    private JButton resetButton;
    
    private Color backColor;
    
    private int spWidth;
    private int spHeight;
    
    private int fontSize;
    private Font myFont;
    private Color bBlack = new Color(5, 11, 91);
    private PenteGameBoard myBoard = null;
    
    private boolean firstGame = true;  
   
    public PenteScore(int w, int h) {
        
       backColor = new Color(102, 102, 255);
       spWidth = w; 
       spHeight = h;
       fontSize = (int) (spHeight * (24.0/722.0));
       myFont = new Font("Arial", Font.PLAIN, fontSize);
       
       this.setSize(spWidth, spHeight);
       this.setBackground(backColor);
        
       this.setVisible(true);
       
       addInfoPlaces();  
    }
    
    public void addInfoPlaces() {
        int scaler = (int) (spHeight * (10.0/722.0));
        System.out.println("The Scaler is " + scaler);
       
        //player 1 info
       JPanel p1Panel = new JPanel();
       p1Panel.setLayout(new BoxLayout(p1Panel, BoxLayout.Y_AXIS));
       p1Panel.setSize(spWidth, (int)(spHeight*0.35));
       p1Panel.setBackground(new Color(137, 156, 249)); //**** new
       //p1Panel.setOpaque(false);
           
           p1Name = new JLabel("Player1 Name");
           p1Name.setAlignmentX(Component.CENTER_ALIGNMENT);  
           p1Name.setFont(myFont);
           p1Name.setForeground(bBlack);   //**** new 
           p1Name.setHorizontalAlignment(SwingConstants.CENTER); 
           
           p1Captures = new JTextField("Player1 Captures");
           p1Captures.setAlignmentX(Component.CENTER_ALIGNMENT);  
           p1Captures.setFont(myFont);
           p1Captures.setForeground(Color.WHITE);  //*** new
           p1Captures.setBackground(bBlack);  //*** new
           p1Captures.setHorizontalAlignment(SwingConstants.CENTER); 
           p1Captures.setFocusable(false);  // this stops editing.
           
           //Place and space the labels
           p1Panel.add(Box.createRigidArea(new Dimension(spWidth-40,scaler*5)));
           p1Panel.add(p1Name);
           p1Panel.add(Box.createRigidArea(new Dimension(spWidth-40,scaler*3)));
           p1Panel.add(p1Captures);
           p1Panel.add(Box.createRigidArea(new Dimension(spWidth-40,scaler*3)));
           
           Border b = BorderFactory.createLineBorder(Color.BLUE, 4, true);
           
           p1Panel.setBorder(b);

       this.add(Box.createRigidArea(new Dimension(spWidth-40,scaler*3)));
       this.add(p1Panel);
       this.add(Box.createRigidArea(new Dimension(spWidth-40,scaler)));
       
       //Add a button
       
       resetButton = new JButton("New Game");
       resetButton.setFont(myFont);
       resetButton.addActionListener(this);
       
       this.add(resetButton);
       
       
       //player 1 info
       JPanel p2Panel = new JPanel();
       p2Panel.setLayout(new BoxLayout(p2Panel, BoxLayout.Y_AXIS));
       p2Panel.setSize(spWidth, (int)(spHeight*0.35));
       p2Panel.setOpaque(false);
           
           p2Name = new JLabel("Player2 Name");
           p2Name.setAlignmentX(Component.CENTER_ALIGNMENT);  
           p2Name.setFont(myFont);
           p2Name.setForeground(Color.WHITE);
           p2Name.setHorizontalAlignment(SwingConstants.CENTER); 
           
           p2Captures = new JTextField("Player2 Captures");
           p2Captures.setAlignmentX(Component.CENTER_ALIGNMENT);  
           p2Captures.setFont(myFont);
           p2Captures.setForeground(bBlack);
           p2Captures.setHorizontalAlignment(SwingConstants.CENTER); 
           p2Captures.setFocusable(false);  // this stops editing.
           
           //Place and space the labels
           p2Panel.add(Box.createRigidArea(new Dimension(spWidth-40,scaler*4)));
           p2Panel.add(p2Name);
           p2Panel.add(Box.createRigidArea(new Dimension(spWidth-40,scaler*3)));
           p2Panel.add(p2Captures);
           p2Panel.add(Box.createRigidArea(new Dimension(spWidth-40,scaler*3)));
           
           Border b2 = BorderFactory.createLineBorder(Color.WHITE, 4, true);
           
           p2Panel.setBorder(b2);

       this.add(Box.createRigidArea(new Dimension(spWidth-40,40)));
       this.add(p2Panel);
       
     //whose turn info
       JPanel whoseTurn = new JPanel();
       whoseTurn.setLayout(new BoxLayout(whoseTurn, BoxLayout.Y_AXIS));
       whoseTurn.setSize(spWidth, (int)(spHeight*0.35));
       whoseTurn.setOpaque(false);
           
  
           
       whoseTurnField = new JTextField("Its ??? Turn Now");
       whoseTurnField.setAlignmentX(Component.CENTER_ALIGNMENT); 
       whoseTurnField.setFont(myFont);
       whoseTurnField.setForeground(bBlack);
       whoseTurnField.setHorizontalAlignment(SwingConstants.CENTER); 
       whoseTurnField.setFocusable(false);  // this stops editing.
           
           //Place and space the labels
          
           whoseTurn.add(Box.createRigidArea(new Dimension(spWidth-40,scaler*2)));
           whoseTurn.add(whoseTurnField);
           whoseTurn.add(Box.createRigidArea(new Dimension(spWidth-40,scaler*2)));
           
           Border b3 = BorderFactory.createLineBorder(Color.BLUE, 4, true);
           
           whoseTurn.setBorder(b3);
       
        this.add(Box.createRigidArea(new Dimension(spWidth-40,scaler*3)));
        this.add(whoseTurn);
    
    }
    
    public void setName(String n, int whichPlayer ) {
        
        if(whichPlayer == PenteGameBoard.BLACKSTONE) {
            p1Name.setText("Player 1: " + n);
        } else {
            p2Name.setText("Player 2: " + n);
        }
        
        repaint();
    }
    
    public void setCaptures(int c, int whichPlayer) {
        
        if(whichPlayer == PenteGameBoard.BLACKSTONE) {
           p1Captures.setText( Integer.toString(c));
           Rectangle r = p1Captures.getVisibleRect();
           p1Captures.paintImmediately(r);
             
        } else {
           p2Captures.setText( Integer.toString(c));
           Rectangle r = p2Captures.getVisibleRect();
           p2Captures.paintImmediately(r);
        }
        
        //p1Captures.repaint();
        //p2Captures.repaint();
        
    }
    
    
    public void setPlayerTurn(int whichPlayer) {
        if(whichPlayer == PenteGameBoard.BLACKSTONE) {
            whoseTurnField.setBackground(bBlack);
            whoseTurnField.setForeground(Color.WHITE);
           
            int cLoc = p1Name.getText().indexOf(":");
            String n = p1Name.getText().substring(cLoc + 2, p1Name.getText().length());
            whoseTurnField.setText("It's " + n + "'s Turn Now");
            
            
            
        } else {
            whoseTurnField.setBackground(Color.WHITE);
            whoseTurnField.setForeground(bBlack);
           
            
            int cLoc = p2Name.getText().indexOf(":");
            String n = p2Name.getText().substring(cLoc + 2, p2Name.getText().length());
            whoseTurnField.setText("It's " + n + "'s Turn Now");
            
        }
        
        if(firstGame) {
            whoseTurnField.repaint();
        } else  {
            Rectangle r = whoseTurnField.getVisibleRect();
            whoseTurnField.paintImmediately(r);
        }
        
    }
    
    public void setGameBoard(PenteGameBoard gb) {
        myBoard = gb;
    }
    

    @Override
    public void actionPerformed(ActionEvent e) {
        // TODO Auto-generated method stub
        
        //System.out.println("Hi clicked me!!!!");
        JOptionPane.showMessageDialog(null, "OK starting new game!");
        firstGame = false;
        if(myBoard != null) myBoard.startNewGame(false); //If you are clicking new game you already have your players
        
    } 
    
    public void paintASAP() {
        this.paintImmediately(0,0,this.spWidth, spHeight);
    }

}
