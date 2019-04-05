import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

public class PenteScore extends JPanel{
    
    //Datas
    private JLabel p1Name, p2Name;
    private JTextField p1Captures, p2Captures;
    private JTextField whoseTurnField;
    
    private JButton resetButton;
    
    private Color backColor;
    
    private int spWidth;
    private int spHeight;
    
    private Font myFont = new Font("Arial", Font.PLAIN, 24);
   
    public PenteScore(int w, int h) {
        
       backColor = new Color(102, 102, 255);
       spWidth = w; 
       spHeight = h;
       
       this.setSize(spWidth, spHeight);
       this.setBackground(backColor);
        
       this.setVisible(true);
       
       addInfoPlaces();  
    }
    
    public void addInfoPlaces() {
       
        //player 1 info
       JPanel p1Panel = new JPanel();
       p1Panel.setLayout(new BoxLayout(p1Panel, BoxLayout.Y_AXIS));
       p1Panel.setSize(spWidth, (int)(spHeight*0.45));
       p1Panel.setOpaque(false);
           
           p1Name = new JLabel("Player1 Name");
           p1Name.setAlignmentX(Component.CENTER_ALIGNMENT);  //New***
           p1Name.setFont(myFont);
           p1Name.setForeground(Color.WHITE);
           p1Name.setHorizontalAlignment(SwingConstants.CENTER); //New **
           
           p1Captures = new JTextField("Player1 Captures");
           p1Captures.setAlignmentX(Component.CENTER_ALIGNMENT);  //New **
           p1Captures.setFont(myFont);
           p1Captures.setForeground(Color.BLACK);
           p1Captures.setHorizontalAlignment(SwingConstants.CENTER);  //New**
           
           //Place and space the labels
           p1Panel.add(Box.createRigidArea(new Dimension(spWidth-40,50)));
           p1Panel.add(p1Name);
           p1Panel.add(Box.createRigidArea(new Dimension(spWidth-40,30)));
           p1Panel.add(p1Captures);
           p1Panel.add(Box.createRigidArea(new Dimension(spWidth-40,30)));
           
           Border b = BorderFactory.createLineBorder(Color.BLUE, 4, true);
           
           p1Panel.setBorder(b);

       this.add(Box.createRigidArea(new Dimension(spWidth-40,30)));
       this.add(p1Panel);
       this.add(Box.createRigidArea(new Dimension(spWidth-40,10)));
       
       //Add a button
       
       resetButton = new JButton("New Game");
       resetButton.setFont(myFont);
       this.add(resetButton);
       
       
       //player 1 info
       JPanel p2Panel = new JPanel();
       p2Panel.setLayout(new BoxLayout(p2Panel, BoxLayout.Y_AXIS));
       p2Panel.setSize(spWidth, (int)(spHeight*0.45));
       p2Panel.setOpaque(false);
           
           p2Name = new JLabel("Player2 Name");
           p2Name.setAlignmentX(Component.CENTER_ALIGNMENT);  //New***
           p2Name.setFont(myFont);
           p2Name.setForeground(Color.WHITE);
           p2Name.setHorizontalAlignment(SwingConstants.CENTER); //New **
           
           p2Captures = new JTextField("Player2 Captures");
           p2Captures.setAlignmentX(Component.CENTER_ALIGNMENT);  //New **
           p2Captures.setFont(myFont);
           p2Captures.setForeground(Color.BLACK);
           p2Captures.setHorizontalAlignment(SwingConstants.CENTER);  //New**
           
           //Place and space the labels
           p2Panel.add(Box.createRigidArea(new Dimension(spWidth-40,40)));
           p2Panel.add(p2Name);
           p2Panel.add(Box.createRigidArea(new Dimension(spWidth-40,30)));
           p2Panel.add(p2Captures);
           p2Panel.add(Box.createRigidArea(new Dimension(spWidth-40,30)));
           
           Border b2 = BorderFactory.createLineBorder(Color.WHITE, 4, true);
           
           p2Panel.setBorder(b2);

       this.add(Box.createRigidArea(new Dimension(spWidth-40,40)));
       this.add(p2Panel);
       
     //whose turn info
       JPanel whoseTurn = new JPanel();
       whoseTurn.setLayout(new BoxLayout(whoseTurn, BoxLayout.Y_AXIS));
       whoseTurn.setSize(spWidth, (int)(spHeight*0.45));
       whoseTurn.setOpaque(false);
           
  
           
       whoseTurnField = new JTextField("Its ??? Turn Now");
       whoseTurnField.setAlignmentX(Component.CENTER_ALIGNMENT);  //New **
       whoseTurnField.setFont(myFont);
       whoseTurnField.setForeground(Color.BLACK);
       whoseTurnField.setHorizontalAlignment(SwingConstants.CENTER);  //New**
           
           //Place and space the labels
          
           whoseTurn.add(Box.createRigidArea(new Dimension(spWidth-40,20)));
           whoseTurn.add(whoseTurnField);
           whoseTurn.add(Box.createRigidArea(new Dimension(spWidth-40,20)));
           
           Border b3 = BorderFactory.createLineBorder(Color.BLUE, 4, true);
           
           whoseTurn.setBorder(b3);
       
        this.add(Box.createRigidArea(new Dimension(spWidth-40,30)));
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
        } else {
           p2Captures.setText( Integer.toString(c));
        }
        
        repaint();
    }
       

}
