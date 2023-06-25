package src;


public class Move {
    private int oldSqr;
    private int newSqrt;
    private int pieceMoved;
    private int pieceAtNewSquare;
    private int flags;
    public final static int NONE = -1;
    public final static int NORMAL = 0;
    public final static int CASTLE = 1;
    public final static int ENPASSANT = 2;
    public final static int PROMOTION = 3;
  

    
    public Move(int oldSqr, int newSqrt, int pieceMoved, int pieceAtNewSquare, int flags){
        this.oldSqr = oldSqr;
        this.newSqrt = newSqrt;
        this.pieceMoved = pieceMoved;
        this.pieceAtNewSquare = pieceAtNewSquare;
        this.flags = flags;
    }


    public int getPieceMoved(){
        return pieceMoved;
    }

    public int getPiecesAtNewSquare(){
        return pieceAtNewSquare;
    }

    public int getFlags(){
        return flags;
    }

    public int getOldSqr(){
        return oldSqr;
    }

    public int getNewSqr(){
        return newSqrt;
    }
    @Override
    public boolean equals(Object obj){
        Move m2 = (Move) obj;

        boolean ifEqual = this.oldSqr == m2.getOldSqr() && this.newSqrt == m2.getNewSqr() && this.pieceMoved == m2.getPieceMoved();
        return ifEqual;
    }   
    
}
