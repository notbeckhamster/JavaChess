package src;

public class Move {
    private int oldSqr;
    private int newSqrt;

    
    public Move(int oldSqr, int newSqrt){
        this.oldSqr = oldSqr;
        this.newSqrt = newSqrt;
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

        boolean ifEqual = this.oldSqr == m2.getOldSqr() && this.newSqrt == m2.getNewSqr();
        return ifEqual;
    }   
    
}
