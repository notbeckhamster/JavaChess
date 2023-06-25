package src;


import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JLayeredPane;
import javax.swing.JPanel;

public class TopPanel extends JPanel {
      private PiecePanel[] piecePanels = new PiecePanel[64];
    private ChessPanel chessPanel;

    public TopPanel(ChessPanel chessPanel) {
        this.chessPanel = chessPanel;
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
        
       //Set the gui board
        int[] board = chessPanel.getBoard().getBoardArray();
        for (int i = 0; i < 64; i++){
            piecePanels[i].setPiece(board[i]);
        }

        
                
    }
    public PiecePanel[] getPiecePanels(){
        return piecePanels;
    }
    public void updateForTesting(){

        int[] boardArray = ChessFrame.getChessPanel().getBoard().getBoardArray();
        for (int i = 0; i < 64; i++){
            piecePanels[i].setPiece(boardArray[i]);
        }

    }
 
}
