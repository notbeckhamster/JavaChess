package src;

import java.awt.Dimension;
import java.awt.GridLayout;
import javax.swing.JPanel;

public class TopPanel extends JPanel {
      private PiecePanel[] piecePanels = new PiecePanel[64];


    public TopPanel() {
        setLayout(new GridLayout(8, 8));
        setSize(new java.awt.Dimension(800, 800));
        setOpaque(false);
        initPanel();
   

    }
    public void initPanel(){
        PiecePanel[][] tempPiecePanels = new PiecePanel[8][8];
        // Add the panels to the board
        for (int rank = 7; rank >= 0; rank--) {
            for (int file = 0; file < 8; file++) {

                PiecePanel test = new PiecePanel(rank, file, Piece.NONE);
                tempPiecePanels[rank][file] = test;
                test.setSize(new Dimension(100, 100));
                add(test);

            }
        }
        // Add the panels to the PiecePanel array
        int count = 0;
     
        for (int rank = 0; rank < 8; rank++) {
            for (int file = 0; file < 8; file++) {
                piecePanels[count] = tempPiecePanels[rank][file];
             count++;
            }
        }
        
        piecePanels[56].setPiece(Piece.BLACK | Piece.ROOK);
        piecePanels[57].setPiece(Piece.BLACK | Piece.KNIGHT);
        piecePanels[58].setPiece(Piece.BLACK | Piece.BISHOP);
        piecePanels[59].setPiece(Piece.BLACK | Piece.QUEEN);
        piecePanels[60].setPiece(Piece.BLACK | Piece.KING);
        piecePanels[61].setPiece(Piece.BLACK | Piece.BISHOP);
        piecePanels[62].setPiece(Piece.BLACK | Piece.KNIGHT);
        piecePanels[63].setPiece(Piece.BLACK | Piece.ROOK);
        for (int i = 48; i < 56; i++) {
            piecePanels[i].setPiece(Piece.BLACK | Piece.PAWN);
        }
        piecePanels[0].setPiece(Piece.WHITE | Piece.ROOK);
        piecePanels[1].setPiece(Piece.WHITE | Piece.KNIGHT);
        piecePanels[2].setPiece(Piece.WHITE | Piece.BISHOP);
        piecePanels[3].setPiece(Piece.WHITE | Piece.QUEEN);
        piecePanels[4].setPiece(Piece.WHITE | Piece.KING);
        piecePanels[5].setPiece(Piece.WHITE | Piece.BISHOP);
        piecePanels[6].setPiece(Piece.WHITE | Piece.KNIGHT);
        piecePanels[7].setPiece(Piece.WHITE | Piece.ROOK);
        for (int i = 8; i < 16; i++) {
            piecePanels[i].setPiece(Piece.WHITE | Piece.PAWN);
        }

        
                
    }
    public PiecePanel[] getPiecePanels(){
        return piecePanels;
    }

 
}
