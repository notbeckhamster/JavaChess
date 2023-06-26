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
        ArrayList<Integer>[] pieceList = board.getBlackPieceArr();
        //Generate all possible moves
        ArrayList<Move> allMoves = genAllPseudoLegalMoves();
        //Choose a random move
        int randomIndex = (int) (Math.random() * allMoves.size());
        return allMoves.get(randomIndex);


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
}
