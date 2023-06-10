package src;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Rectangle;

public class Helper {
    /** <p>
 * Draw a String centered in the middle of a Rectangle.
 * </p>
 *  <p>
     * This code is taken from StackOverflow:
     * <a href=
     * "https://stackoverflow.com/a/27740330">https://stackoverflow.com/a/27740330</a>
     * in response to
     * <a href=
     * "https://stackoverflow.com/questions/27706197/how-can-i-center-graphics-drawstring-in-java">https://stackoverflow.com/questions/27706197/how-can-i-center-graphics-drawstring-in-java</a>.
     * Code by Daniel Kvist used under the <a href="https://creativecommons.org/licenses/by-sa/3.0/">(CC BY-SA 3.0)</a> license.
     * </p>
 * @param g The Graphics instance.
 * @param text The String to draw.
 * @param rect The Rectangle to center the text in.
 */
public static void drawCenteredString(Graphics g, String text, Rectangle rect, Font font) {
    // Get the FontMetrics
    FontMetrics metrics = g.getFontMetrics(font);
    // Determine the X coordinate for the text
    int x = rect.x + (rect.width - metrics.stringWidth(text)) / 2;
    // Determine the Y coordinate for the text (note we add the ascent, as in java 2d 0 is top of the screen)
    int y = rect.y + ((rect.height - metrics.getHeight()) / 2) + metrics.getAscent();
    // Set the font
    g.setFont(font);
    // Draw the String
    g.drawString(text, x, y);
}
}
