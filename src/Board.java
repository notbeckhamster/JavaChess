package src;

import java.awt.List;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class Board {
    private int[] board = new int[64];
    private HashMap<String, Integer> dirToCompassRoseDirHM = new HashMap<String, Integer>();
    private HashMap<String, int[]> dirToNumToEdgeHM = new HashMap<String, int[]>();
    private int[] northNumToEdge = new int[64];
    private int[] southNumToEdge = new int[64];
    private int[] eastNumToEdge = new int[64];
    private int[] westNumToEdge = new int[64];
    private int[] noWeNumToEdge = new int[64];
    private int[] noEaNumToEdge = new int[64];
    private int[] soWeNumToEdge = new int[64];
    private int[] soEaNumToEdge = new int[64];
    public Board(){
        initBoard();
    }
    public void initBoard(){
        //Set up board
            
        board[56]=Piece.BLACK | Piece.ROOK;
        board[57]=Piece.BLACK | Piece.KNIGHT;
        board[58]=Piece.BLACK | Piece.BISHOP;
        board[59]=Piece.BLACK | Piece.QUEEN;
        board[60]=Piece.BLACK | Piece.KING;
        board[61]=Piece.BLACK | Piece.BISHOP;
        board[62]=Piece.BLACK | Piece.KNIGHT;
        board[63]=Piece.BLACK | Piece.ROOK;
        for (int i = 48; i < 56; i++) {
            board[i]=Piece.BLACK | Piece.PAWN;
        }
        board[0]=Piece.WHITE | Piece.ROOK;
        board[1]=Piece.WHITE | Piece.KNIGHT;
        board[2]=Piece.WHITE | Piece.BISHOP;
        board[3]=Piece.WHITE | Piece.QUEEN;
        board[4]=Piece.WHITE | Piece.KING;
        board[5]=Piece.WHITE | Piece.BISHOP;
        board[6]=Piece.WHITE | Piece.KNIGHT;
        board[7]=Piece.WHITE | Piece.ROOK;
        for (int i = 8; i < 16; i++) {
            board[i]=Piece.WHITE | Piece.PAWN;
        }
       //setup compass rose
       dirToCompassRoseDirHM.put("nort", 8);
       dirToCompassRoseDirHM.put("noWe", 7);
       dirToCompassRoseDirHM.put("west", -1);
       dirToCompassRoseDirHM.put("soWe", -9);
       dirToCompassRoseDirHM.put("sout", -8);
       dirToCompassRoseDirHM.put("soEa", -7);
       dirToCompassRoseDirHM.put("east", 1);
       dirToCompassRoseDirHM.put("noEa", 9);

       //setup numToEdge Arrays
         dirToNumToEdgeHM.put("nort", northNumToEdge);
            dirToNumToEdgeHM.put("noWe", noWeNumToEdge);
            dirToNumToEdgeHM.put("west", westNumToEdge);
            dirToNumToEdgeHM.put("soWe", soWeNumToEdge);
            dirToNumToEdgeHM.put("sout", southNumToEdge);
            dirToNumToEdgeHM.put("soEa", soEaNumToEdge);
            dirToNumToEdgeHM.put("east", eastNumToEdge);
            dirToNumToEdgeHM.put("noEa", noEaNumToEdge);


          //setup toEdge Arrays
            for (int rank = 0; rank < 8; rank++) {
                for (int file = 0; file < 8; file++){
                int idx = 8*rank+file;
                northNumToEdge[idx] = 7 - rank ;
                southNumToEdge[idx] = rank;
                eastNumToEdge[idx] = 7 - idx % 8;
                westNumToEdge[idx] = idx % 8;
                noWeNumToEdge[idx] = Math.min(northNumToEdge[idx], westNumToEdge[idx]);
                noEaNumToEdge[idx] = Math.min(northNumToEdge[idx], eastNumToEdge[idx]);
                soWeNumToEdge[idx] = Math.min(southNumToEdge[idx], westNumToEdge[idx]);
                soEaNumToEdge[idx] = Math.min(southNumToEdge[idx], eastNumToEdge[idx]);
                }
            }

        }

    public void makeMove(int oldRank, int oldFile, int newRank, int newFile){
        int oldIdx = 8*oldRank+oldFile;
        int newIdx = 8*newRank+newFile;
        board[newIdx] = board[oldIdx];
        board[oldIdx] = Piece.NONE;
    
    }
    
    public Collection<Move> validMoves(int rank, int file, int piece){
        if (isSlidingPiece(piece) == true){
            return generateSlidingMoves(rank, file, piece);
        }
        ArrayList<Move> result = new ArrayList<Move>();
        for (int i = 0; i<64; i++){
            result.add(new Move(rank*8+file, i));
        }
        return result;
    }

    public boolean isSlidingPiece(int piece){
        piece = piece & Piece.PIECE_MASK;
        if (piece == Piece.ROOK || piece == Piece.BISHOP || piece == Piece.QUEEN){
            return true;
        }
        return false;
    }

    public Collection<Move> generateSlidingMoves(int rank, int file, int piece){
        ArrayList<Move> moves = new ArrayList<Move>();
        //generate directions piece can move
        ArrayList<String> directionsPieceCanMove = generateDirections(piece);
        //for each direction piece can move find the valid moves
        for (String eachDirection : dirToCompassRoseDirHM.keySet()) {
            if (directionsPieceCanMove.contains(eachDirection) == false) continue;
            int direction = dirToCompassRoseDirHM.get(eachDirection);
            int numToEdge = dirToNumToEdgeHM.get(eachDirection)[8*rank+file];
            int currIdx = 8*rank+file;
            int oldIdx = currIdx;
            for (int i = 0; i<numToEdge; i++){
                currIdx+=direction;
                boolean ifNewSqrDifColor = (board[currIdx] & Piece.COLOR_MASK) != (piece & Piece.COLOR_MASK);
                boolean ifNewSqrEmpty = board[currIdx] == Piece.NONE;
                if (ifNewSqrEmpty == true){
                      moves.add(new Move(oldIdx, currIdx));
                      
                } else if (ifNewSqrDifColor == true){
                    moves.add(new Move(oldIdx, currIdx));
                    break;
                } else {
                    break;
                }
              


            }

            
        }
        return moves;
    }

    public ArrayList<String> generateDirections(int piece){
        int pieceType = piece & Piece.PIECE_MASK;
        ArrayList<String> directions = new ArrayList<String>();
        if (pieceType == Piece.ROOK){
            directions.add("nort");
            directions.add("west");
            directions.add("sout");
            directions.add("east");
        }
        else if (pieceType == Piece.BISHOP){
            directions.add("noWe");
            directions.add("noEa");
            directions.add("soWe");
            directions.add("soEa");
        }
        else if (pieceType == Piece.QUEEN){
            directions.add("nort");
            directions.add("noWe");
            directions.add("west");
            directions.add("soWe");
            directions.add("sout");
            directions.add("soEa");
            directions.add("east");
            directions.add("noEa");
        }
    return directions;
    }


}
