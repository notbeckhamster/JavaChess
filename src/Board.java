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
    private ArrayList<Move> registeredMoves = new ArrayList<Move>();
    private TopPanel piecePanel;

    public Board() {

        initBoard();
    }

    public void initBoard() {
        // Set up board

        board[56] = Piece.BLACK | Piece.ROOK;
        board[57] = Piece.BLACK | Piece.KNIGHT;
        board[58] = Piece.BLACK | Piece.BISHOP;
        board[59] = Piece.BLACK | Piece.QUEEN;
        board[60] = Piece.BLACK | Piece.KING;
        board[61] = Piece.BLACK | Piece.BISHOP;
        board[62] = Piece.BLACK | Piece.KNIGHT;
        board[63] = Piece.BLACK | Piece.ROOK;
        for (int i = 48; i < 56; i++) {
            board[i] = Piece.BLACK | Piece.PAWN;
        }
        board[0] = Piece.WHITE | Piece.ROOK;
        board[1] = Piece.WHITE | Piece.KNIGHT;
        board[2] = Piece.WHITE | Piece.BISHOP;
        board[3] = Piece.WHITE | Piece.QUEEN;
        board[4] = Piece.WHITE | Piece.KING;
        board[5] = Piece.WHITE | Piece.BISHOP;
        board[6] = Piece.WHITE | Piece.KNIGHT;
        board[7] = Piece.WHITE | Piece.ROOK;
        for (int i = 8; i < 16; i++) {
            board[i] = Piece.WHITE | Piece.PAWN;
        }
        // setup compass rose
        dirToCompassRoseDirHM.put("nort", 8);
        dirToCompassRoseDirHM.put("noWe", 7);
        dirToCompassRoseDirHM.put("west", -1);
        dirToCompassRoseDirHM.put("soWe", -9);
        dirToCompassRoseDirHM.put("sout", -8);
        dirToCompassRoseDirHM.put("soEa", -7);
        dirToCompassRoseDirHM.put("east", 1);
        dirToCompassRoseDirHM.put("noEa", 9);

        // setup numToEdge Arrays
        dirToNumToEdgeHM.put("nort", northNumToEdge);
        dirToNumToEdgeHM.put("noWe", noWeNumToEdge);
        dirToNumToEdgeHM.put("west", westNumToEdge);
        dirToNumToEdgeHM.put("soWe", soWeNumToEdge);
        dirToNumToEdgeHM.put("sout", southNumToEdge);
        dirToNumToEdgeHM.put("soEa", soEaNumToEdge);
        dirToNumToEdgeHM.put("east", eastNumToEdge);
        dirToNumToEdgeHM.put("noEa", noEaNumToEdge);

        // setup toEdge Arrays
        for (int rank = 0; rank < 8; rank++) {
            for (int file = 0; file < 8; file++) {
                int idx = 8 * rank + file;
                northNumToEdge[idx] = 7 - rank;
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

    public void makeMove(Move move) {
        int oldIdx = move.getOldSqr();
        int newIdx = move.getNewSqr();
        int capturedPieceIfAny = board[newIdx];
        board[newIdx] = board[oldIdx];
        board[oldIdx] = Piece.NONE;
        if ((move.getPieceMoved() & Piece.PIECE_MASK) == Piece.PAWN) checkPawnPromotionAndEnPassant(move, capturedPieceIfAny);
        registeredMoves.add(move);
    }

    public void setPiecePanel(TopPanel piecePanel) {
        this.piecePanel = piecePanel;
    }

    public void checkPawnPromotionAndEnPassant(Move move, int capturedPieceIfAny) {
        // check if pawn is on last rank
        int newIdx = move.getNewSqr();
        int piece = board[newIdx];
        int rank = newIdx / 8;
        if (rank == 0 || rank == 7) {
            // pawn is on last rank
            // promote pawn to queen
            board[newIdx] = piece | Piece.QUEEN;
            piecePanel.getPiecePanels()[newIdx].setPiece(Piece.QUEEN | (piece & Piece.COLOR_MASK));
        } else {
            if (registeredMoves.size() < 2) return;
            // Grab info about last move
            Move lastMove = registeredMoves.get(registeredMoves.size() - 1);
            int lastPiece = lastMove.getPieceMoved();
            int lastOldIdx = lastMove.getOldSqr();
            int lastNewIdx = lastMove.getNewSqr();
            // Check if current pawn move was a diagonal which captured nothing
            ArrayList<Integer> fourDiagonalDirections = new ArrayList<Integer>();
            fourDiagonalDirections.add(dirToCompassRoseDirHM.get("noWe"));
            fourDiagonalDirections.add(dirToCompassRoseDirHM.get("soWe"));
            fourDiagonalDirections.add(dirToCompassRoseDirHM.get("soEa"));
            fourDiagonalDirections.add(dirToCompassRoseDirHM.get("noEa"));
            int currentMoveDirection = move.getNewSqr() - move.getOldSqr();
            boolean ifCurrentMoveWasDiagonal = fourDiagonalDirections.contains(currentMoveDirection);
            //Check if last move was a pawn move forward two and digaonal capture is empty
            boolean ifLastMoveForwardTwo = Math.abs(lastNewIdx-lastOldIdx) == 16;
            boolean ifSquareCapureIsEmpty = capturedPieceIfAny == Piece.NONE;
            if (ifCurrentMoveWasDiagonal && ifLastMoveForwardTwo && ifSquareCapureIsEmpty ) {
                // en passant
                // remove pawn
                //Captured pawn idx
                int capturedPawnIdx = registeredMoves.get(registeredMoves.size()-1).getNewSqr();
                board[capturedPawnIdx] = Piece.NONE;
                piecePanel.getPiecePanels()[capturedPawnIdx].setPiece(Piece.NONE);
            

            }
        }

    }

    public Collection<Move> validMoves(int rank, int file, int piece) {
        if (isSlidingPiece(piece) == true) {
            return generateSlidingMoves(rank, file, piece);
        } else if ((piece & Piece.PIECE_MASK) == Piece.KNIGHT) {
            return generateKnightMoves(rank, file, piece);
        } else if ((piece & Piece.PIECE_MASK) == Piece.PAWN) {
            return generatePawnMoves(rank, file, piece);
        } else if ((piece & Piece.PIECE_MASK) == Piece.KING) {
            return generateKingMoves(rank, file, piece);
        }
        // incase you ask for valid moves of a empty piece...
        ArrayList<Move> result = new ArrayList<Move>();
        return result;
    }

    public boolean isSlidingPiece(int piece) {
        piece = piece & Piece.PIECE_MASK;
        if (piece == Piece.ROOK || piece == Piece.BISHOP || piece == Piece.QUEEN) {
            return true;
        }
        return false;
    }

    public boolean isOnBoard(int idx) {
        if (idx >= 0 && idx < 64) {
            return true;
        }
        return false;
    }

    public Collection<Move> generatePawnMoves(int rank, int file, int piece) {
        ArrayList<Move> moves = new ArrayList<Move>();
        int currIdx = 8 * rank + file;
        int direction = 0;
        // Set the direction based on color
        if (Piece.isColor(piece, Piece.WHITE)) {
            direction = dirToCompassRoseDirHM.get("nort");
        } else {
            direction = dirToCompassRoseDirHM.get("sout");
        }

        // Add the forward move if empty
        int idxOneMoveForward = currIdx + direction;
        if (isOnBoard(idxOneMoveForward) == true && board[idxOneMoveForward] == Piece.NONE) {
            moves.add(new Move(currIdx, idxOneMoveForward, piece));
        }

        // Add the starting two move forward if on starting rank
        boolean ifOnStartingRank = (rank == 1 && Piece.isColor(piece, Piece.WHITE))
                || (rank == 6 && Piece.isColor(piece, Piece.BLACK));
        if (ifOnStartingRank == true) {
            int idxTwoMoveForward = currIdx + 2 * direction;
            if (board[idxTwoMoveForward] == Piece.NONE && board[idxOneMoveForward] == Piece.NONE) {
                moves.add(new Move(currIdx, idxTwoMoveForward, piece));
            }
        }

        // Check if enpassant is valid move
        // Grab info about last move
        if (registeredMoves.size() != 0) {
            Move lastMove = registeredMoves.get(registeredMoves.size() - 1);
            int lastPiece = lastMove.getPieceMoved();
            int lastOldIdx = lastMove.getOldSqr();
            int lastNewIdx = lastMove.getNewSqr();
            // Check if last piece is a pawn which moves two squares
            if ((lastPiece & Piece.PIECE_MASK) == Piece.PAWN && Math.abs(lastOldIdx - lastNewIdx) == 16) {
                // Check if the last move was next to the current pawn
                int lastDirection = 0;
                boolean ifBeside = lastNewIdx == currIdx + dirToCompassRoseDirHM.get("east")
                        || lastNewIdx == currIdx + dirToCompassRoseDirHM.get("west");
                if (ifBeside == true){
                    if (Piece.isColor(piece, Piece.WHITE)) {
                        lastDirection = dirToCompassRoseDirHM.get("nort");
                    } else {
                        lastDirection = dirToCompassRoseDirHM.get("sout");
                    }
                    moves.add(new Move(currIdx,  lastNewIdx+ lastDirection, piece));
                }
            }
        }

        // Add the diagonal moves if there is an enemy piece
        String leftDirectionKey = Piece.isColor(piece, Piece.WHITE) ? "noWe" : "soWe";
        String rightDirectionKey = Piece.isColor(piece, Piece.WHITE) ? "noEa" : "soEa";

        int leftCaptureSqr = currIdx + dirToCompassRoseDirHM.get(leftDirectionKey);
        int rightCaptureSqr = currIdx + dirToCompassRoseDirHM.get(rightDirectionKey);
        // Check captures are moves on the board
        int numToEdgeLeft = dirToNumToEdgeHM.get(leftDirectionKey)[8 * rank + file];
        int numToEdgeRight = dirToNumToEdgeHM.get(rightDirectionKey)[8 * rank + file];
        if (isOnBoard(leftCaptureSqr) && numToEdgeLeft > 0 && board[leftCaptureSqr] != Piece.NONE
                && Piece.isColor(piece, board[leftCaptureSqr] & Piece.COLOR_MASK) == false) {
            moves.add(new Move(currIdx, leftCaptureSqr, piece));
        }
        if (isOnBoard(rightCaptureSqr) && numToEdgeRight > 0 && board[rightCaptureSqr] != Piece.NONE
                && Piece.isColor(piece, board[rightCaptureSqr] & Piece.COLOR_MASK) == false) {
            moves.add(new Move(currIdx, rightCaptureSqr, piece));
        }
        return moves;

    }

    public Collection<Move> generateSlidingMoves(int rank, int file, int piece) {
        ArrayList<Move> moves = new ArrayList<Move>();
        // generate directions piece can move
        ArrayList<String> directionsPieceCanMove = generateDirections(piece);
        // for each direction piece can move find the valid moves
        for (String eachDirection : dirToCompassRoseDirHM.keySet()) {
            if (directionsPieceCanMove.contains(eachDirection) == false)
                continue;
            int direction = dirToCompassRoseDirHM.get(eachDirection);
            int numToEdge = dirToNumToEdgeHM.get(eachDirection)[8 * rank + file];
            int currIdx = 8 * rank + file;
            int oldIdx = currIdx;
            for (int i = 0; i < numToEdge; i++) {
                currIdx += direction;
                boolean ifNewSqrDifColor = (board[currIdx] & Piece.COLOR_MASK) != (piece & Piece.COLOR_MASK);
                boolean ifNewSqrEmpty = board[currIdx] == Piece.NONE;
                if (ifNewSqrEmpty == true) {
                    moves.add(new Move(oldIdx, currIdx, piece));

                } else if (ifNewSqrDifColor == true) {
                    moves.add(new Move(oldIdx, currIdx, piece));
                    break;
                } else {
                    break;
                }

            }

        }
        return moves;
    }

    public Collection<Move> generateKnightMoves(int rank, int file, int piece) {
        ArrayList<Move> moves = new ArrayList<Move>();
        // generate directions piece can move
        ArrayList<Integer> directionsPieceCanMove = new ArrayList<Integer>();
        int noEa = dirToCompassRoseDirHM.get("noEa");
        int noWe = dirToCompassRoseDirHM.get("noWe");
        int soEa = dirToCompassRoseDirHM.get("soEa");
        int soWe = dirToCompassRoseDirHM.get("soWe");
        directionsPieceCanMove.add(noEa + dirToCompassRoseDirHM.get("nort"));
        directionsPieceCanMove.add(noEa + dirToCompassRoseDirHM.get("east"));
        directionsPieceCanMove.add(noWe + dirToCompassRoseDirHM.get("nort"));
        directionsPieceCanMove.add(noWe + dirToCompassRoseDirHM.get("west"));
        directionsPieceCanMove.add(soEa + dirToCompassRoseDirHM.get("sout"));
        directionsPieceCanMove.add(soEa + dirToCompassRoseDirHM.get("east"));
        directionsPieceCanMove.add(soWe + dirToCompassRoseDirHM.get("sout"));
        directionsPieceCanMove.add(soWe + dirToCompassRoseDirHM.get("west"));

        // for each direction piece can move find the valid moves
        for (int direction : directionsPieceCanMove) {
            int currIdx = 8 * rank + file;
            int oldIdx = currIdx;

            currIdx += direction;
            // Decomp the currIdx into new Rank and new File
            int newRank = currIdx / 8;
            int newFile = currIdx % 8;
            // If knight on the right side of the board and the new move
            // is on the left break out
            if (file >= 6 && newFile <= 1) {
                continue;
                // if knight on the left side of the board and the new move is
                // on the right break out
            } else if (file <= 1 && newFile >= 6) {
                continue;
            }
            // If the move is not dont consider it
            if (isOnBoard(currIdx) == false)
                continue;

            boolean ifNewSqrDifColor = (board[currIdx] & Piece.COLOR_MASK) != (piece & Piece.COLOR_MASK);
            boolean ifNewSqrEmpty = board[currIdx] == Piece.NONE;
            if (ifNewSqrEmpty == true) {
                moves.add(new Move(oldIdx, currIdx, piece));

            } else if (ifNewSqrDifColor == true) {
                moves.add(new Move(oldIdx, currIdx, piece));
            }

        }
        return moves;
    }

    public Collection<Move> generateKingMoves(int rank, int file, int piece) {
        ArrayList<Move> moves = new ArrayList<Move>();

        // for each direction piece can move find the valid moves
        for (String eachDirection : dirToCompassRoseDirHM.keySet()) {

            int direction = dirToCompassRoseDirHM.get(eachDirection);
            int currIdx = 8 * rank + file;
            int oldIdx = currIdx;

            currIdx += direction;
            // If the move is not dont consider it
            if (isOnBoard(currIdx) == false)
                continue;

            boolean ifNewSqrDifColor = (board[currIdx] & Piece.COLOR_MASK) != (piece & Piece.COLOR_MASK);
            boolean ifNewSqrEmpty = board[currIdx] == Piece.NONE;
            if (ifNewSqrEmpty == true) {
                moves.add(new Move(oldIdx, currIdx, piece));

            } else if (ifNewSqrDifColor == true) {
                moves.add(new Move(oldIdx, currIdx, piece));
            }

        }

        return moves;
    }

    public ArrayList<String> generateDirections(int piece) {
        int pieceType = piece & Piece.PIECE_MASK;
        ArrayList<String> directions = new ArrayList<String>();
        if (pieceType == Piece.ROOK) {
            directions.add("nort");
            directions.add("west");
            directions.add("sout");
            directions.add("east");
        } else if (pieceType == Piece.BISHOP) {
            directions.add("noWe");
            directions.add("noEa");
            directions.add("soWe");
            directions.add("soEa");
        } else if (pieceType == Piece.QUEEN) {
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
