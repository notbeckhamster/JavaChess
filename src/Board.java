package src;

public class Board {
    private int[] board = new int[64];

    public Board(){
        for (int i = 0; i<64; i++){
            board[i] = Piece.NONE;
        }
        board[0] = Piece.WHITE | Piece.ROOK;
        board[1] = Piece.WHITE | Piece.KNIGHT;


    }
}
