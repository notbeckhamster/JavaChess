package src;

import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JLayeredPane;
import javax.swing.JPanel;

public class ChessPanel extends JLayeredPane{
    public ChessPanel(){
        setPreferredSize(new java.awt.Dimension(900, 900));
        BackgroundPanel background = new BackgroundPanel();
        add(background, JLayeredPane.DEFAULT_LAYER);
        TopPanel piece = new TopPanel();
        add(piece, JLayeredPane.PALETTE_LAYER);
        piece.setLocation(0,0);
        
    }
    private class TopPanel extends JPanel{
        public TopPanel(){
            setLayout(new GridLayout(8,8));
            setSize(new java.awt.Dimension(800, 800));
            setOpaque(false);
            for (int j = 0; j<8;j++){
                for (int i = 0; i<8;i++){
                    PiecePanel test = new PiecePanel(0,0,Piece.NONE);
                test.setSize(new Dimension(100,100));
                add(test);
                }
            }
        }
    
    }
    
}
