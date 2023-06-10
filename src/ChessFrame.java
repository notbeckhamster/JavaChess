package src;
import javax.swing.JFrame;
import javax.swing.WindowConstants;

public class ChessFrame{

    public static void main(String[] args) {

        JFrame frame = new JFrame("Chess by Beckhamster");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.getContentPane().add(new ChessPanel());
        frame.setVisible(true);
        frame.pack();

    }

}