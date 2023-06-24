package src.test;

import java.util.ArrayList;
import java.util.Collection;

import org.junit.*;

import src.Board;
import src.Move;

public class TestPositionTest {
    private Board board = new Board();
    private int depth = 0;

    @Test
    public void TestDepth0() {
        depth = 0;
        Assert.assertEquals(1, perft(0));

    }

    @Test
    public void TestDepth1() {
        depth = 1;
        Assert.assertEquals(20, perft(1));
    }

    @Test
    public void TestDepth2() {
        depth = 2;
        Assert.assertEquals(400, perft(2));

    }

    @Test
    public void TestDepth3() {
        depth = 3;
        Assert.assertEquals(8902, perft(3));
    }

    public int perft(int depth) {

        if (depth == 0)
            return 1;

        int nodes = 0;
        Collection<Move> moves = genAllPseudoLegalMoves();
        for (Move move : moves) {
            board.makeMove(move, false);
            int nodesAtMove = perft(depth - 1);
            nodes += nodesAtMove;
            board.unmakeMove(move, false);
            if (depth == this.depth)
                System.out.println(translateMoveToString(move) + ": " + nodesAtMove);

        }

        return nodes;
    }

    public Collection<Move> genAllPseudoLegalMoves(){

        Collection<Move> result = new ArrayList<Move>();
        int[] boardArr = board.getBoardArray();
        ArrayList<Integer>[] piecesArr = board.getWhiteToMove() ? board.getWhitePieceArr() : board.getBlackPieceArr();

        for (int i = 1; i<=6; i++){
            ArrayList<Integer> pieces = piecesArr[i];
            for (int j = 0; j<pieces.size(); j++){
                int position = pieces.get(j);
                result.addAll(board.generateValidMoves(position/8, position%8, boardArr[position]));
            }
      
        }
        return result;


    }

    public String translateMoveToString(Move move) {
        return translateIndexToString(move.getOldSqr()) + translateIndexToString(move.getNewSqr());

    }

    public String translateIndexToString(int idx) {
        int rank = idx / 8;
        int file = idx % 8;
        String result = "";
        switch (file) {
            case 0:
                result += "a";
                break;
            case 1:
                result += "b";
                break;
            case 2:
                result += "c";
                break;
            case 3:
                result += "d";
                break;
            case 4:
                result += "e";
                break;
            case 5:
                result += "f";
                break;
            case 6:
                result += "g";
                break;
            case 7:
                result += "h";
                break;
        }
        switch (rank) {
            case 0:
                result += "1";
                break;
            case 1:
                result += "2";
                break;
            case 2:
                result += "3";
                break;
            case 3:
                result += "4";
                break;
            case 4:
                result += "5";
                break;
            case 5:
                result += "6";
                break;
            case 6:
                result += "7";
                break;
            case 7:
                result += "8";
                break;
        }
        return result;
    }
}
