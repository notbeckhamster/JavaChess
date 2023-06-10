package src;

import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JLayeredPane;
import javax.swing.JPanel;

public class ChessPanel extends JLayeredPane{
    private PiecePanel[] piecePanels = new PiecePanel[64];
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
            int piecePanelIdx=0;
            for (int rank = 1; rank<9;rank++){
                for (int file = 1; file<9;file++){
                    
                    PiecePanel test = new PiecePanel(rank,file,Piece.NONE);
                    piecePanels[piecePanelIdx++] = test;
                    test.setSize(new Dimension(100,100));
                    add(test);
                   
                  
                }
            }
            piecePanels[0].setPiece(Piece.BLACK|Piece.ROOK);
            piecePanels[1].setPiece(Piece.BLACK|Piece.KNIGHT);
            piecePanels[2].setPiece(Piece.BLACK|Piece.BISHOP);
            piecePanels[3].setPiece(Piece.BLACK|Piece.QUEEN);
            piecePanels[4].setPiece(Piece.BLACK|Piece.KING);
            piecePanels[5].setPiece(Piece.BLACK|Piece.BISHOP);
            piecePanels[6].setPiece(Piece.BLACK|Piece.KNIGHT);
            piecePanels[7].setPiece(Piece.BLACK|Piece.ROOK);
            for (int i = 8; i<16;i++){
                piecePanels[i].setPiece(Piece.BLACK|Piece.PAWN);
            }
            piecePanels[56].setPiece(Piece.WHITE|Piece.ROOK);
            piecePanels[57].setPiece(Piece.WHITE|Piece.KNIGHT);
            piecePanels[58].setPiece(Piece.WHITE|Piece.BISHOP);
            piecePanels[59].setPiece(Piece.WHITE|Piece.QUEEN);
            piecePanels[60].setPiece(Piece.WHITE|Piece.KING);
            piecePanels[61].setPiece(Piece.WHITE|Piece.BISHOP);
            piecePanels[62].setPiece(Piece.WHITE|Piece.KNIGHT);
            piecePanels[63].setPiece(Piece.WHITE|Piece.ROOK);
            for (int i = 48; i<56;i++){
                piecePanels[i].setPiece(Piece.WHITE|Piece.PAWN);
            }

        }
    
    }
    
}
