package src.test;

import java.util.ArrayList;
import java.util.Collection;

import org.junit.*;

import src.Board;
import src.Move;

public class TestPositionTest {
    private Board board = new Board();
    private boolean isWhiteTurn = true;
    @Test
    public void TestDepth0() {

        Assert.assertEquals(1, perft(0));
   
    }

    @Test
    public void TestDepth1() {

        Assert.assertEquals(20, perft(1));
    }

    @Test
    public void TestDepth2() {

        Assert.assertEquals(400, perft(2));
    }

    @Test
    public void TestDepth3() {

        Assert.assertEquals(8902, perft(3));
    }

    public int perft(int depth) {

        if (depth == 0)
            return 1;

        int nodes = 0;
        Collection<Move> moves = genAllPseudoLegalMoves();
        for (Move move : moves) {
            board.makeMove(move, false);
            nodes += perft(depth - 1);
            board.unmakeMove(move, false);
        }
        return nodes;
    }

    public Collection<Move> genAllPseudoLegalMoves(){

        Collection<Move> result = new ArrayList<Move>();
        int[] boardArr = board.getBoardArray();
        ArrayList<Integer>[] piecesArr = isWhiteTurn ? board.getWhitePieceArr() : board.getBlackPieceArr();
        isWhiteTurn = !isWhiteTurn;
        for (int i = 1; i<=6; i++){
            ArrayList<Integer> pieces = piecesArr[i];
            for (int j = 0; j<pieces.size(); j++){
                int position = pieces.get(j);
                result.addAll(board.generateValidMoves(position/8, position%8, boardArr[position]));
            }
      
        }
        return result;


    }
}
