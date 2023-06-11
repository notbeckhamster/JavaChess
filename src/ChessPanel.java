package src;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class ChessPanel extends JLayeredPane {
    protected TopPanel piecePanel;
    private PiecePanel[] piecePanels = new PiecePanel[64];

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
        PiecePanel newMoveHighLight = null;
        PiecePanel oldMoveHighLight = null;

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
                selectedPanel.setPiece(Piece.NONE);
                int centerX = e.getPoint().x - selectedPiece.getWidth() / 2;
                int centerY = e.getPoint().y - selectedPiece.getHeight() / 2;
                selectedPiece.setLocation(centerX, centerY);
                add(selectedPiece, JLayeredPane.DRAG_LAYER);
            }
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
            if (e.getPoint().x > 800 || e.getPoint().y > 800 || e.getPoint().x < 0 || e.getPoint().y < 0) {
                returnPiece();
                getMainLayeredPane().revalidate();
                getMainLayeredPane().repaint();
                return;
            }
            PiecePanel selectedPanel = (PiecePanel) piecePanel.getComponentAt(e.getPoint());
            // Check if moving into square empty or occupied by enemy
            if (selectedPanel.getPiece() == Piece.NONE
                    || Piece.isColor(oldPiece, selectedPanel.getPiece() & Piece.COLOR_MASK) == false) {
                // Move the piece to the new square
                selectedPanel.setPiece(oldPiece);
                //Set highlights and remove old highlights
                highlightSquares(selectedPanel);
                remove(selectedPiece);
                selectedPiece = null;

            } else {
                // Reset the piece to the old square
                returnPiece();

            }

            // Move the piece to the new square
            selectedPiece = null;
            getMainLayeredPane().revalidate();
            getMainLayeredPane().repaint();
        }

        private void returnPiece() {
            piecePanels[oldRank * 8 + oldFile].setPiece(oldPiece);
            remove(selectedPiece);
            selectedPiece = null;
        }
        private void highlightSquares(PiecePanel selectedPanel){
                
                //Remove old highlights
                if (newMoveHighLight != null) {
                    newMoveHighLight.setHighlight(false);
                    
                    oldMoveHighLight.setHighlight(false);
                }
                //Add new highlights
                selectedPanel.setHighlight(true);
                piecePanels[oldRank * 8 + oldFile].setHighlight(true);
                //Save the highlights
                newMoveHighLight = selectedPanel;
                oldMoveHighLight = piecePanels[oldRank * 8 + oldFile];

        }

    }

    private class TopPanel extends JPanel {
        public TopPanel() {
            setLayout(new GridLayout(8, 8));
            setSize(new java.awt.Dimension(800, 800));
            setOpaque(false);
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

    }

}
