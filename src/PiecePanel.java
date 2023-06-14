package src;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagLayout;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class PiecePanel extends JPanel{
    private int rank;
    private int file;
    private JLabel jLabelPiece = null;
    private int piece;
    private boolean highlight = false;
    private boolean ifDisplayValidMoves = false;
   
    public PiecePanel(int rank, int file, int piece){  
        this.rank = rank;
        this.file = file;
        this.piece = piece;
        ImageIcon icon = loadIcon();
        jLabelPiece = new JLabel(icon, SwingConstants.CENTER);
        setLayout(new GridBagLayout());
        add(jLabelPiece);
       setOpaque(false);
    
        
    }
    public int getRank(){
        return rank;
    }
    public int getFile(){
        return file;
    }
 
    public void setPiece(int piece){
        this.piece = piece;
        ImageIcon icon = loadIcon();
        jLabelPiece.setIcon(icon);
    }
    public ImageIcon loadIcon(){
        if (piece == Piece.NONE){
            return null;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("src/images/Chess_");
        
        
        switch (piece & 0b00111){
            case Piece.PAWN:
                sb.append("p");
                break;
            case Piece.KNIGHT:
                sb.append("n");
                break;
            case Piece.BISHOP:
                sb.append("b");
                break;
            case Piece.ROOK:
                sb.append("r");
                break;
            case Piece.QUEEN:
                sb.append("q");
                break;
            case Piece.KING:
                sb.append("k");
                break;
        }
        
        if (Piece.isColor(piece, Piece.BLACK)){
            sb.append("d");
        }
        else {
            sb.append("l");
        }
        sb.append("t90.png");
        ImageIcon icon = Helper.getImageIcon(sb.toString());
        return icon;
    }
    public JLabel getJLabelPiece(){
        return jLabelPiece;
    }
    public int getPiece(){
        return piece;
    }
    public void setHighlight(boolean highlight){
        if (highlight){
            setBackground(Color.BLACK);
            highlight = true;
            setOpaque(true);
        }
        else{
            setBackground(null);
            highlight=false;
            setOpaque(false);
        }
    }
    public void setDisplayValidMoves(boolean ifDisplayValidMoves){
        this.ifDisplayValidMoves = ifDisplayValidMoves;
    }
    public void paintComponent(Graphics g){
        if (ifDisplayValidMoves){
        Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(10));
        g2.setColor(Color.GRAY);
        g2.drawOval(10,10,80,80);
        }
    }
    public boolean getHighlight(){
        return highlight;
    }
}