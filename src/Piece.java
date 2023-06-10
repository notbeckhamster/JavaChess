package src;

public class Piece {
    public final static int NONE = 0;
    public final static int KING = 1;
    public final static int QUEEN = 2;
    public final static int ROOK = 3;
    public final static int BISHOP = 4;
    public final static int KNIGHT = 5;
    public final static int PAWN = 6;
   
    public final static int WHITE = 8;
    public final static int BLACK = 16;
    public final static int COLOR_MASK = 0b11000;
    public static boolean isColor(int piece, int color){
        return (piece & color) == color;
    }
}
