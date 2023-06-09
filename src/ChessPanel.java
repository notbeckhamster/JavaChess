package src;


import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collection;

import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.SwingConstants;

public class ChessPanel extends JLayeredPane {
    protected TopPanel piecePanel;
    private Board board = new Board();
    private MoveGen moveGen = new MoveGen(board);
    private boolean ifUsingMoveGen = false;
    private int colorOfPlayer = Piece.WHITE;
    ArrayList<PiecePanel> attackedHighlights = new ArrayList<PiecePanel>();
    public ChessPanel() {
        setPreferredSize(new java.awt.Dimension(900, 900));
        BackgroundPanel background = new BackgroundPanel();
        add(background, JLayeredPane.DEFAULT_LAYER);
        piecePanel = new TopPanel(this);
        board.setPiecePanel(piecePanel);
        add(piecePanel, JLayeredPane.PALETTE_LAYER);
        piecePanel.setLocation(0, 0);
        MyMouseAdapter myMouseAdapter = new MyMouseAdapter();
        addMouseMotionListener(myMouseAdapter);
        addMouseListener(myMouseAdapter);

    }
    public void startMoveGenGame(int color){
        ifUsingMoveGen = true;
        colorOfPlayer = color;
    }
    public JLayeredPane getMainLayeredPane() {
        return this;
    }
    public TopPanel getTopPanel(){
        return piecePanel;
    }
    public Board getBoard(){
        return board;
    }
     public void highlightAttackedSquares(int color){
            boolean[] attackedSquares = board.getAttackedSquares(color);
             for (int i = 0 ; i< attackedSquares.length; i++){
                if (attackedSquares[i] == true){  
                    PiecePanel panel = piecePanel.getPiecePanels()[i];
                    attackedHighlights.add(panel);
                    panel.setDisplayAttackedSquares(true);
                }
            }
        }
     public void resetAttackHighlights(){
            for (PiecePanel highlightedPanel : attackedHighlights){
                highlightedPanel.setDisplayAttackedSquares(false);
            }

    }
    private class MyMouseAdapter extends MouseAdapter {
        JLabel selectedPiece = null;
        int oldRank = -1;
        int oldFile = -1;
        int oldPiece = Piece.NONE;
        ArrayList<PiecePanel> hightlights = new ArrayList<PiecePanel>();
        ArrayList<PiecePanel> validMovesPanel = new ArrayList<PiecePanel>();
        Collection<Move> validMoves=null;
        
        public void mousePressed(MouseEvent e) {

            if (selectedPiece == null) {
                PiecePanel selectedPanel = (PiecePanel) piecePanel.getComponentAt(e.getPoint());
                if (selectedPanel == null) {
                    return;
                }
                oldRank = selectedPanel.getRank();
                oldFile = selectedPanel.getFile();
                oldPiece = selectedPanel.getPiece();
                int turn = board.ifWhiteIsToMove() == true ? Piece.WHITE : Piece.BLACK;
                int colorSelected = (oldPiece & Piece.COLOR_MASK);
                if (turn != colorSelected) {
                    return;
                }
                if (ifUsingMoveGen == true){
                    if (colorOfPlayer != colorSelected){
                        return;
                    }
                }
                // Copy the JLabelPiece to the selectedPiece
                selectedPiece = selectedPanel.getJLabelPiece();
                selectedPiece = new JLabel(selectedPiece.getIcon(), SwingConstants.CENTER);
                selectedPiece.setSize(new Dimension(100, 100));
                //set valid moves
                validMoves = board.validMoves(oldRank, oldFile, true);
                highlightValidMoves(selectedPanel);
                
                selectedPanel.setPiece(Piece.NONE);
                int centerX = e.getPoint().x - selectedPiece.getWidth() / 2;
                int centerY = e.getPoint().y - selectedPiece.getHeight() / 2;
                selectedPiece.setLocation(centerX, centerY);
                add(selectedPiece, JLayeredPane.DRAG_LAYER);
              resetHighlights();
                
            }
            
            getMainLayeredPane().revalidate();
            getMainLayeredPane().repaint();
        }

        public void mouseDragged(MouseEvent e) {
      
               int turn = board.ifWhiteIsToMove() == true ? Piece.WHITE : Piece.BLACK;
                int colorSelected = (oldPiece & Piece.COLOR_MASK);
                if (turn != colorSelected) {
                    return;
                }
                 if (ifUsingMoveGen == true){
                    if (colorOfPlayer != colorSelected){
                        return;
                    }
                }
            int centerX = e.getPoint().x - selectedPiece.getWidth() / 2;
            int centerY = e.getPoint().y - selectedPiece.getHeight() / 2;
            selectedPiece.setLocation(centerX, centerY);

            getMainLayeredPane().revalidate();
            getMainLayeredPane().repaint();
        }

        public void mouseReleased(MouseEvent e) {
      
               resetValidMoves();
            // If out of bounds, return the piece to the original square
            if (e.getPoint().x > 800 || e.getPoint().y > 800 || e.getPoint().x < 0 || e.getPoint().y < 0) {
                returnPiece();
                getMainLayeredPane().revalidate();
                getMainLayeredPane().repaint();
                return;
            }
            //If no piece selected return dont do anything
            if ((oldPiece & Piece.PIECE_MASK) == Piece.NONE) {
                selectedPiece = null;
                return;
            }
             int turn = board.ifWhiteIsToMove() == true ? Piece.WHITE : Piece.BLACK;
                int colorSelected = (oldPiece & Piece.COLOR_MASK);
                if (turn != colorSelected) {
                    return;
                }
                if (ifUsingMoveGen == true){
                    if (colorOfPlayer != colorSelected){
                        return;
                    }
                }
            PiecePanel selectedPanel = (PiecePanel) piecePanel.getComponentAt(e.getPoint());
            // Check if moving into square empty or occupied by enemy
            if (selectedPanel.getPiece() == Piece.NONE
                    || Piece.isColor(oldPiece, selectedPanel.getPiece() & Piece.COLOR_MASK) == false) {


                //Check if valid move
                Move move = ReturnMoveIfValidMove(selectedPanel.getRank(), selectedPanel.getFile());
                if (move.getFlags() == Move.NONE) {
                    returnPiece();
                    
                }else {
                
                // Make move to board 
                board.makeMove(move, true);
                //Set highlights and remove old highlights    
                highlightSquares(selectedPanel);
                remove(selectedPiece);
        
                }

            } else {
                // Reset the piece to the old square
                returnPiece();

            }

         
            selectedPiece = null;
            
            getMainLayeredPane().revalidate();
            getMainLayeredPane().repaint();

            if (ifUsingMoveGen == true){
                     turn = board.ifWhiteIsToMove() == true ? Piece.WHITE : Piece.BLACK;
                     if (turn != colorOfPlayer){
                         moveGen.playResponse();
                     }
                }
        }

        private void returnPiece() {
            int currIdx = oldRank * 8 + oldFile;
            if (currIdx <0 || currIdx > 63){
                return;
            }
            piecePanel.getPiecePanels()[currIdx].setPiece(oldPiece);
            remove(selectedPiece);
            selectedPiece = null;
        }
        private Move ReturnMoveIfValidMove(int newRank, int newFile){
            for (Move move : validMoves){
                if (move.getNewSqr() == newRank*8 + newFile){
                    return move;
                }
            }
            return new Move(-1,-1,-1,-1,-1);
        }
        private void highlightSquares(PiecePanel selectedPanel){
                
                
                //Add new highlights
                hightlights.add(selectedPanel);
                selectedPanel.setHighlight(true);
                hightlights.add(piecePanel.getPiecePanels()[oldRank * 8 + oldFile]);
                piecePanel.getPiecePanels()[oldRank * 8 + oldFile].setHighlight(true);


        }

        private void highlightValidMoves(PiecePanel selectedPanel){
             for (Move move : validMoves){
                    validMovesPanel.add(piecePanel.getPiecePanels()[move.getNewSqr()]);
                    piecePanel.getPiecePanels()[move.getNewSqr()].setDisplayValidMoves(true);
                }
                
           

    }

     
           

    
    private void resetValidMoves(){
        for (PiecePanel panel : validMovesPanel){
            panel.setDisplayValidMoves(false);
        }
        
   

}
        private void resetHighlights(){
            for (PiecePanel highlightedPanel : hightlights){
                highlightedPanel.setHighlight(false);
            }

    }
   

    }

   

}
