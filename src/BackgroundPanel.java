package src;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
public class BackgroundPanel extends JPanel{
    private int boardPixelSize = 900;
    private int squarePixelSize = 100;
    private JLayeredPane layeredPane;
    public BackgroundPanel(){
        
        setSize(new java.awt.Dimension(boardPixelSize, boardPixelSize));
    }

    /** <p> Draws ChessPanel. </p>
     *  @param g Graphics object to draw on.
     */
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        drawBoard(g2);
      
}

    /** <p> Draws Chess Board Background </p>
     * @param g2 Graphics2D object to draw on.
     */
    public void drawBoard(Graphics2D g){
        //Draw the Board Background (checkered squares)
        for (int rank = 0; rank<8; rank++){
            for (int file = 0; file<8; file++){
                if ((rank+file)%2==0){
                    g.setColor(java.awt.Color.WHITE);
                }
                else{
                    g.setColor(java.awt.Color.BLACK);
                }
                g.fillRect(rank*squarePixelSize, file*squarePixelSize, squarePixelSize, squarePixelSize);
            }
        }
        //Draw the Rank and File Labels
        g.setColor(java.awt.Color.BLACK);
        //Draw the Files Labels
        for (int file = 0; file<8; file++){
            Rectangle rect = new Rectangle(file*squarePixelSize, 8*squarePixelSize, squarePixelSize, squarePixelSize);
            Helper.drawCenteredString(g, Character.toString((char)('a'+file)), rect, new Font("TimesRoman", Font.PLAIN, 50));
        }
        //Draw the rank labels
        for (int rank = 0; rank<8; rank++){
            Rectangle rect = new Rectangle(8*squarePixelSize, rank*squarePixelSize, squarePixelSize, squarePixelSize);
            Helper.drawCenteredString(g, Character.toString((char)('8'-rank)), rect, new Font("TimesRoman", Font.PLAIN, 50));
        }
    }
}