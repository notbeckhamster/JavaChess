package src;
import javax.swing.JFrame;
import javax.swing.WindowConstants;

public class ChessFrame{

    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
        


    }

    private static void createAndShowGUI() {
        JFrame frame = new JFrame("Chess");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.getContentPane().add(new ChessPanel());
        frame.pack();
        frame.setVisible(true);
    }

}