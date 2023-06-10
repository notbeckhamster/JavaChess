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
            for (int rank = 1; rank<9;rank++){
                for (int file = 1; file<9;file++){
                    if (file % 2 == 0){
                    PiecePanel test = new PiecePanel(rank,file,Piece.BLACK | Piece.ROOK);
                    test.setSize(new Dimension(100,100));
                    add(test);
                    } else {
                        PiecePanel test = new PiecePanel(rank,file,Piece.WHITE | Piece.KNIGHT);
                        test.setSize(new Dimension(100,100));
                        add(test);
                    } 
                  
                }
            }
        }
    
    }
    
}
