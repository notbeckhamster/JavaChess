package src;

public class Move {
    private int oldSqr;
    private int newSqrt;
    private int pieceMoved;

    
    public Move(int oldSqr, int newSqrt, int pieceMoved ){
        this.oldSqr = oldSqr;
        this.newSqrt = newSqrt;
        this.pieceMoved = pieceMoved;
    }


    public int getPieceMoved(){
        return pieceMoved;
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
