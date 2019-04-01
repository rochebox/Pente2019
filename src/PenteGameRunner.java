import javax.swing.JFrame;

public class PenteGameRunner {

    public static void main(String[] args) {
        // TODO Auto-generated method stub
        
        int gWidth = 19*38;
        int gHeight = 19*38;
        
        JFrame theGame = new JFrame("Play Pente!!");
        theGame.setSize(gWidth, gHeight+20);
        theGame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        PenteGameBoard gb = new PenteGameBoard(gWidth, gHeight, theGame);
        theGame.add(gb);
      
        theGame.setVisible(true);
        gb.startNewGame();
        
        

    }

}
