package src;
import java.awt.Color;
import java.awt.Dimension;
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

    public PiecePanel(int rank, int file, int piece){  
        this.rank = rank;
        this.file = file;
        this.piece = piece;
        ImageIcon icon = Helper.getImageIcon("src/images/Chess_bdt45.png");
        jLabelPiece = new JLabel(icon, SwingConstants.CENTER);
        setLayout(new GridBagLayout());
        add(jLabelPiece);
       setOpaque(false);
        
    }

    public JLabel getJLabelPiece(){
        return jLabelPiece;
    }
}