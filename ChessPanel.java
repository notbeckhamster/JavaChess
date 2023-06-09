import javax.swing.JPanel;

public class ChessPanel extends JPanel{
    private int boardPixelSize = 800;
    public ChessPanel(){
        setPreferredSize(new java.awt.Dimension(boardPixelSize, boardPixelSize));
    }
}
