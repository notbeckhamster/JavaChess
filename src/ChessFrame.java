package src;

import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.WindowConstants;

public class ChessFrame{
    private static ChessPanel chessPanel;
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
        chessPanel = new ChessPanel();
        frame.getContentPane().add(chessPanel);
        frame.pack();
        frame.setVisible(true);
       
        JMenuBar menuBar = new JMenuBar();
         StartGameMenu startGameMenu = new StartGameMenu();
        menuBar.add(startGameMenu.createMenu());
        TestMenu testMenu = new TestMenu();
        menuBar.add(testMenu.createMenu());
        frame.setJMenuBar(menuBar);
    }

    public static ChessPanel getChessPanel() {
        return chessPanel;
    }

}