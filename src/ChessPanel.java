package src;

import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.SwingConstants;

public class ChessPanel extends JLayeredPane {
    protected TopPanel piecePanel;
    private Board board = new Board();
  
    public ChessPanel() {
        setPreferredSize(new java.awt.Dimension(900, 900));
        BackgroundPanel background = new BackgroundPanel();
        add(background, JLayeredPane.DEFAULT_LAYER);
        piecePanel = new TopPanel();
        add(piecePanel, JLayeredPane.PALETTE_LAYER);
        piecePanel.setLocation(0, 0);
        MyMouseAdapter myMouseAdapter = new MyMouseAdapter();
        addMouseMotionListener(myMouseAdapter);
        addMouseListener(myMouseAdapter);

    }

    public JLayeredPane getMainLayeredPane() {
        return this;
    }

    private class MyMouseAdapter extends MouseAdapter {
        JLabel selectedPiece = null;
        int oldRank = -1;
        int oldFile = -1;
        int oldPiece = Piece.NONE;
        ArrayList<PiecePanel> hightlights = new ArrayList<PiecePanel>();
        ArrayList<PiecePanel> validMoves = new ArrayList<PiecePanel>();

        public void mousePressed(MouseEvent e) {

            if (selectedPiece == null) {
                PiecePanel selectedPanel = (PiecePanel) piecePanel.getComponentAt(e.getPoint());
                oldRank = selectedPanel.getRank();
                oldFile = selectedPanel.getFile();
                oldPiece = selectedPanel.getPiece();
                // Copy the JLabelPiece to the selectedPiece
                selectedPiece = selectedPanel.getJLabelPiece();
                selectedPiece = new JLabel(selectedPiece.getIcon(), SwingConstants.CENTER);
                selectedPiece.setSize(new Dimension(100, 100));
                highlightValidMoves(selectedPanel);
                selectedPanel.setPiece(Piece.NONE);
                int centerX = e.getPoint().x - selectedPiece.getWidth() / 2;
                int centerY = e.getPoint().y - selectedPiece.getHeight() / 2;
                selectedPiece.setLocation(centerX, centerY);
                add(selectedPiece, JLayeredPane.DRAG_LAYER);
              
                
            }
            resetHighlights();
            getMainLayeredPane().revalidate();
            getMainLayeredPane().repaint();
        }

        public void mouseDragged(MouseEvent e) {
            // If there is no piece selected, select the piece

            int centerX = e.getPoint().x - selectedPiece.getWidth() / 2;
            int centerY = e.getPoint().y - selectedPiece.getHeight() / 2;
            selectedPiece.setLocation(centerX, centerY);

            getMainLayeredPane().revalidate();
            getMainLayeredPane().repaint();
        }

        public void mouseReleased(MouseEvent e) {
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
            PiecePanel selectedPanel = (PiecePanel) piecePanel.getComponentAt(e.getPoint());
            // Check if moving into square empty or occupied by enemy
            if (selectedPanel.getPiece() == Piece.NONE
                    || Piece.isColor(oldPiece, selectedPanel.getPiece() & Piece.COLOR_MASK) == false) {


                //Check if valid move
                Move move = new Move(oldRank*8 +oldFile, selectedPanel.getRank()*8 + selectedPanel.getFile());
                if (!board.validMoves(oldRank, oldFile, oldPiece).contains(move)) {
                    returnPiece();
                    
                }else {
                // Move the piece to the new square
                selectedPanel.setPiece(oldPiece);   
                
                // Make move to board 
                board.makeMove(oldRank, oldFile, selectedPanel.getRank(), selectedPanel.getFile());
                //Set highlights and remove old highlights
                
                highlightSquares(selectedPanel);
                remove(selectedPiece);
         
                }

            } else {
                // Reset the piece to the old square
                returnPiece();

            }

            // Move the piece to the new square
            selectedPiece = null;
            resetValidMoves();
            getMainLayeredPane().revalidate();
            getMainLayeredPane().repaint();
        }

        private void returnPiece() {
            piecePanel.getPiecePanels()[oldRank * 8 + oldFile].setPiece(oldPiece);
            remove(selectedPiece);
            selectedPiece = null;
        }
        private void highlightSquares(PiecePanel selectedPanel){
                
                
                //Add new highlights
                hightlights.add(selectedPanel);
                selectedPanel.setHighlight(true);
                hightlights.add(piecePanel.getPiecePanels()[oldRank * 8 + oldFile]);
                piecePanel.getPiecePanels()[oldRank * 8 + oldFile].setHighlight(true);


        }

        private void highlightValidMoves(PiecePanel selectedPanel){
             for (Move move : board.validMoves(oldRank, oldFile, selectedPanel.getPiece())){
                    validMoves.add(piecePanel.getPiecePanels()[move.getNewSqr()]);
                    piecePanel.getPiecePanels()[move.getNewSqr()].setDisplayValidMoves(true);
                }
                
           

    }
    private void resetValidMoves(){
        for (PiecePanel panel : validMoves){
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
