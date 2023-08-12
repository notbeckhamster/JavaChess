package src;

import java.util.ArrayList;
import java.util.Collection;

public class MoveGen {
    private Board board;

    public MoveGen(Board board) {
        this.board = board;
    }

    public void playResponse() {
        Move move = getRandomMove();
        board.makeMove(move, true);

    }

    public Move getRandomMove(){
        //Generate all possible moves
        ArrayList<Move> allMoves = genAllPseudoLegalMoves();
        //Choose a random move
        int randomIndex = (int) (Math.random() * allMoves.size());
        return allMoves.get(randomIndex);


    }

    public double negaMax(int depth){
        return 0;
    }

    public double evaluate(){
        return 0;
    }

    /** <p> Generates the evalution of the mobility using Papa's/Wita entropy method </p>  
     * @param color
     * @return
     */
    public double generateMobilityEval(int color){
        int oppColor = color==Piece.WHITE ? Piece.BLACK : Piece.WHITE;
        double moveRatio = genAllPseudoLegalMoves(color).size() / genAllPseudoLegalMoves(oppColor).size();
        double squaredControlledRatio = countControlledSquares(color) / countControlledSquares(oppColor);
        return Math.log(moveRatio * squaredControlledRatio);

    }

    public double countControlledSquares(int color){
        boolean[] controlledSquares = board.getAttackedSquares(color);
        double count = 0;
        for (int i = 0; i<controlledSquares.length; i++){
            if (controlledSquares[i]){
                count++;
            }
        }
        return count;

    }
    public ArrayList<Move> genAllPseudoLegalMoves(){

        ArrayList<Move> result = new ArrayList<Move>();
        ArrayList<Integer>[] piecesArr = board.getWhiteToMove() ? board.getWhitePieceArr() : board.getBlackPieceArr();

        for (int i = 1; i<=6; i++){
            ArrayList<Integer> pieces = piecesArr[i];
            for (int j = 0; j<pieces.size(); j++){
                int position = pieces.get(j);
                result.addAll(board.validMoves(position/8, position%8, false));
            }
      
        }
        return result;


    }

     public ArrayList<Move> genAllPseudoLegalMoves(int color){

        ArrayList<Move> result = new ArrayList<Move>();
        ArrayList<Integer>[] piecesArr = color==Piece.WHITE ? board.getWhitePieceArr() : board.getBlackPieceArr();

        for (int i = 1; i<=6; i++){
            ArrayList<Integer> pieces = piecesArr[i];
            for (int j = 0; j<pieces.size(); j++){
                int position = pieces.get(j);
                result.addAll(board.validMoves(position/8, position%8, false));
            }
      
        }
        return result;


    }
}
