package src;

import java.awt.List;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

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
    private ArrayList<Integer> whitePawnSquares = new ArrayList<Integer>();
    private ArrayList<Integer> whiteRookSquares = new ArrayList<Integer>();
    private ArrayList<Integer> whiteKnightSquares = new ArrayList<Integer>();
    private ArrayList<Integer> whiteBishopSquares = new ArrayList<Integer>();
    private ArrayList<Integer> whiteQueenSquares = new ArrayList<Integer>();
    private ArrayList<Integer> whiteKingSquares = new ArrayList<Integer>();
    private ArrayList<Integer>[] whitePieceArr= new ArrayList[7];
    private ArrayList<Integer> blackPawnSquares = new ArrayList<Integer>();
    private ArrayList<Integer> blackRookSquares = new ArrayList<Integer>();
    private ArrayList<Integer> blackKnightSquares = new ArrayList<Integer>();
    private ArrayList<Integer> blackBishopSquares = new ArrayList<Integer>();
    private ArrayList<Integer> blackQueenSquares = new ArrayList<Integer>();
    private ArrayList<Integer> blackKingSquares = new ArrayList<Integer>();
    private ArrayList<Integer>[] blackPieceArr = new ArrayList[7];
    private boolean[] whiteAttackingSquares = new boolean[64];
    private boolean[] blackAttackingSquares = new boolean[64];
    private ArrayList<Move> registeredMoves = new ArrayList<Move>();
    private TopPanel piecePanel;
    private boolean whiteToMove = true;

    public Board() {

        initBoard();
    }
    public boolean ifWhiteIsToMove(){
        return whiteToMove;
    }
    public void initBoard() {
        // Set up piece lists
        whitePieceArr[Piece.PAWN] = whitePawnSquares;
        whitePieceArr[Piece.ROOK] = whiteRookSquares;
        whitePieceArr[Piece.KNIGHT] = whiteKnightSquares;
        whitePieceArr[Piece.BISHOP] = whiteBishopSquares;
        whitePieceArr[Piece.QUEEN] = whiteQueenSquares;
        whitePieceArr[Piece.KING] = whiteKingSquares;
        blackPieceArr[Piece.PAWN] = blackPawnSquares;
        blackPieceArr[Piece.ROOK] = blackRookSquares;
        blackPieceArr[Piece.KNIGHT] = blackKnightSquares;
        blackPieceArr[Piece.BISHOP] = blackBishopSquares;
        blackPieceArr[Piece.QUEEN] = blackQueenSquares;
        blackPieceArr[Piece.KING] = blackKingSquares;

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

         setPieceLists();
    }

    public ArrayList<Move> getRegisteredMoves() {
        return registeredMoves;
    }
    public void setPieceLists(){
        for (int i = 0; i < 64; i++) {
            int piece = board[i];
            if (piece != Piece.NONE) {
                if ((piece & Piece.WHITE) == Piece.WHITE) {
                    whitePieceArr[piece & Piece.PIECE_MASK].add(i);
                } else {
                    blackPieceArr[piece & Piece.PIECE_MASK].add(i);
                }
            }
        }
        setAttackedSquares(Piece.WHITE);
        setAttackedSquares(Piece.BLACK);
    }

    public void updatePieceList(){
        for (int i = 1; i < blackPieceArr.length; i++) {
            blackPieceArr[i].clear();
            whitePieceArr[i].clear();
        }
        for (int i = 0; i < 64; i++) {
            int piece = board[i];
            if ((piece & Piece.PIECE_MASK) != Piece.NONE){
                if ((piece & Piece.COLOR_MASK) == Piece.WHITE) {
                    whitePieceArr[piece & Piece.PIECE_MASK].add(i);
                } else {
                    blackPieceArr[piece & Piece.PIECE_MASK].add(i);
                }
        }
        }
        setAttackedSquares(Piece.WHITE);
        setAttackedSquares(Piece.BLACK);

    }
    public void makeMove(Move move) {

        int oldIdx = move.getOldSqr();
        int newIdx = move.getNewSqr();
        int capturedPieceIfAny = board[newIdx];
        board[newIdx] = board[oldIdx];
        Piece.setMoved(board[newIdx], true);
        board[oldIdx] = Piece.NONE;
    
        if ((move.getPieceMoved() & Piece.PIECE_MASK) == Piece.PAWN)
            checkPawnPromotionAndEnPassant(move, capturedPieceIfAny);
        registeredMoves.add(move);
        updatePieceList();
        //Update white to move
        whiteToMove = !whiteToMove;
    }
     
    public void unmakeMove(Move move) {
        //First reverse the piece at newSqr to oldSqr, bring back the piece at new Sqr if any
        int oldIdx = move.getOldSqr();
        int newIdx = move.getNewSqr();
        board[oldIdx] = move.getPieceMoved();
        board[newIdx] = move.getPiecesAtNewSquare();
    
    
      //  if ((move.getPieceMoved() & Piece.PIECE_MASK) == Piece.PAWN)
            //unmakePawnPromotionAndEnPassant(move);
        registeredMoves.remove(move);
         updatePieceList();
         whiteToMove = !whiteToMove;
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
            board[newIdx] = (piece & Piece.COLOR_MASK) | Piece.QUEEN;
            piecePanel.getPiecePanels()[newIdx].setPiece(Piece.QUEEN | (piece & Piece.COLOR_MASK));
        } else {
            if (registeredMoves.size() < 2)
                return;
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
            // Check if last move was a pawn move forward two and digaonal capture is empty
            boolean ifLastMoveForwardTwo = Math.abs(lastNewIdx - lastOldIdx) == 16;
            boolean ifSquareCapureIsEmpty = capturedPieceIfAny == Piece.NONE;
            if (ifCurrentMoveWasDiagonal && ifLastMoveForwardTwo && ifSquareCapureIsEmpty) {
                // en passant
                // remove pawn
                // Captured pawn idx
                int capturedPawnIdx = registeredMoves.get(registeredMoves.size() - 1).getNewSqr();
                board[capturedPawnIdx] = Piece.NONE;
                piecePanel.getPiecePanels()[capturedPawnIdx].setPiece(Piece.NONE);

            }
        }

    }

    public Collection<Move> validMoves(int rank, int file, int piece) {
        //Generate board showing what is under attack
        setAttackedSquares(piece);
        //Generate moves
        Collection<Move> moves = generateValidMoves(rank, file, piece);
        //Filter out moves that leave king in check
        Collection<Move> movesCopy = new ArrayList<Move>(moves);
        for (Move eachMove : movesCopy) {
            makeMove(eachMove);
            if (isKingInCheck(piece & Piece.COLOR_MASK) == true) {
                moves.remove(eachMove);
            }
            unmakeMove(eachMove);
        }
        return moves;        
    }

    public boolean isKingInCheck(int color) {
        //Pick attackedSquaresArr based on opposite color
        boolean[] attackedSquares = (color == Piece.WHITE) ? blackAttackingSquares : whiteAttackingSquares;
        //Find king on color
        int kingIdx = (color == Piece.WHITE) ? whitePieceArr[Piece.KING].get(0) : blackPieceArr[Piece.KING].get(0);
        //Check if king is under attack
        return attackedSquares[kingIdx];
    }

    public Collection<Move> generateValidMoves(int rank, int file, int piece){
        if (isSlidingPiece(piece) == true) {
            return generateSlidingMoves(rank, file, piece);
        } else if ((piece & Piece.PIECE_MASK) == Piece.KNIGHT) {
            return generateKnightMoves(rank, file);
        } else if ((piece & Piece.PIECE_MASK) == Piece.PAWN) {
            return generatePawnMoves(rank, file);
        } else if ((piece & Piece.PIECE_MASK) == Piece.KING) {
            return generateKingMoves(rank, file);
        } else {
             ArrayList<Move> result = new ArrayList<Move>();
            return result;
        }
    }
   
    public void setAttackedSquares(int color){
        //Pick attackedSquaresArr based on color
        boolean[] attackedSquares = (color == Piece.WHITE) ? whiteAttackingSquares : blackAttackingSquares;
        //Clear attacked squares
        for(int i = 0; i < 64; i++){
            attackedSquares[i] = false;
        }
        //Find opposite color piece list
        ArrayList<Integer>[] selectedColorPiecesList = (color == Piece.WHITE) ? whitePieceArr : blackPieceArr;
        //Set attacked squares for king
        if (selectedColorPiecesList[Piece.KING].size() != 0) {
          Collection<Move> kingMoves = generateKingMoves(selectedColorPiecesList[Piece.KING].get(0)/8, selectedColorPiecesList[Piece.KING].get(0)%8);
        for (Move move : kingMoves) {
            attackedSquares[move.getNewSqr()] = true;
        }
        }
        
        //set attacked squares for queen 
        for (int i = 0; i < selectedColorPiecesList[Piece.QUEEN].size(); i++) {
            Collection<Move> queenMoves = generateSlidingMoves(selectedColorPiecesList[Piece.QUEEN].get(i)/8, selectedColorPiecesList[Piece.QUEEN].get(i)%8, Piece.QUEEN);
            for (Move move : queenMoves) {
                int newSqr = move.getNewSqr();
                attackedSquares[newSqr] = true;
            }
        }
        //set attacked squares for rook
        for (int i = 0; i < selectedColorPiecesList[Piece.ROOK].size(); i++) {
            Collection<Move> rookMoves = generateSlidingMoves(selectedColorPiecesList[Piece.ROOK].get(i)/8, selectedColorPiecesList[Piece.ROOK].get(i)%8, Piece.ROOK);
            for (Move move : rookMoves) {
                attackedSquares[move.getNewSqr()] = true;
            }
        }
        //set attacked squares for bishop
        for (int i = 0; i < selectedColorPiecesList[Piece.BISHOP].size(); i++) {
            Collection<Move> bishopMoves = generateSlidingMoves(selectedColorPiecesList[Piece.BISHOP].get(i)/8, selectedColorPiecesList[Piece.BISHOP].get(i)%8, Piece.BISHOP);
            for (Move move : bishopMoves) {
                attackedSquares[move.getNewSqr()] = true;
            }
        }
        //set attacked squares for knight
        for (int i = 0; i < selectedColorPiecesList[Piece.KNIGHT].size(); i++) {
            Collection<Move> knightMoves = generateKnightMoves(selectedColorPiecesList[Piece.KNIGHT].get(i)/8, selectedColorPiecesList[Piece.KNIGHT].get(i)%8);
            for (Move move : knightMoves) {
                attackedSquares[move.getNewSqr()] = true;
            }
        }
        //set attacked squares for pawn
        for (int i = 0; i < selectedColorPiecesList[Piece.PAWN].size(); i++) {
            Collection<Move> pawnMoves = generatePawnAttacks(selectedColorPiecesList[Piece.PAWN].get(i)/8, selectedColorPiecesList[Piece.PAWN].get(i)%8);
            for (Move move : pawnMoves) {
                attackedSquares[move.getNewSqr()] = true;
            }
        }
    

        

    }

    public boolean[] getAttackedSquares(int color) {
        boolean[] attackedSquares = (color == Piece.WHITE) ? whiteAttackingSquares : blackAttackingSquares;
        return attackedSquares;
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
    
    public Collection<Move> generatePawnAttacks(int rank, int file) {
        ArrayList<Move> moves = new ArrayList<Move>();
        int currIdx = 8 * rank + file;
        int direction = 0;
        int piece = board[currIdx];
        // Set the direction based on color
        if (Piece.isColor(piece, Piece.WHITE)) {
            direction = dirToCompassRoseDirHM.get("nort");
        } else {
            direction = dirToCompassRoseDirHM.get("sout");
        }

        // Add the diagonal moves if there is an enemy piece
        String leftDirectionKey = Piece.isColor(piece, Piece.WHITE) ? "noWe" : "soWe";
        String rightDirectionKey = Piece.isColor(piece, Piece.WHITE) ? "noEa" : "soEa";

        int leftCaptureSqr = currIdx + dirToCompassRoseDirHM.get(leftDirectionKey);
        int rightCaptureSqr = currIdx + dirToCompassRoseDirHM.get(rightDirectionKey);
        // Check captures are moves on the board
        int numToEdgeLeft = dirToNumToEdgeHM.get(leftDirectionKey)[currIdx];
        int numToEdgeRight = dirToNumToEdgeHM.get(rightDirectionKey)[currIdx];
        if (numToEdgeLeft >= 1) moves.add(new Move(currIdx, leftCaptureSqr, piece, board[leftCaptureSqr], Move.NORMAL));
       
        if (numToEdgeRight >= 1) moves.add(new Move(currIdx, rightCaptureSqr, piece, board[rightCaptureSqr], Move.NORMAL));
        
        return moves;

    }
   
    public Collection<Move> generatePawnMoves(int rank, int file) {
        ArrayList<Move> moves = new ArrayList<Move>();
        int currIdx = 8 * rank + file;
        int direction = 0;
        int piece = board[currIdx];
        // Set the direction based on color
        if (Piece.isColor(piece, Piece.WHITE)) {
            direction = dirToCompassRoseDirHM.get("nort");
        } else {
            direction = dirToCompassRoseDirHM.get("sout");
        }

        // Add the forward move if empty
        int idxOneMoveForward = currIdx + direction;
        if (isOnBoard(idxOneMoveForward) == true && board[idxOneMoveForward] == Piece.NONE) {
            moves.add(new Move(currIdx, idxOneMoveForward, piece, Piece.NONE, Move.NORMAL));
        }

        // Add the starting two move forward if on starting rank
        boolean ifOnStartingRank = (rank == 1 && Piece.isColor(piece, Piece.WHITE))
                || (rank == 6 && Piece.isColor(piece, Piece.BLACK));
        if (ifOnStartingRank == true) {
            int idxTwoMoveForward = currIdx + 2 * direction;
            if (board[idxTwoMoveForward] == Piece.NONE && board[idxOneMoveForward] == Piece.NONE) {
                moves.add(new Move(currIdx, idxTwoMoveForward, piece, Piece.NONE, Move.NORMAL));
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
                if (ifBeside == true) {
                    if (Piece.isColor(piece, Piece.WHITE)) {
                        lastDirection = dirToCompassRoseDirHM.get("nort");
                    } else {
                        lastDirection = dirToCompassRoseDirHM.get("sout");
                    }
                    moves.add(new Move(currIdx, lastNewIdx + lastDirection, piece, Piece.NONE, Move.ENPASSANT));
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
            moves.add(new Move(currIdx, leftCaptureSqr, piece, board[leftCaptureSqr], Move.NORMAL));
        }
        if (isOnBoard(rightCaptureSqr) && numToEdgeRight > 0 && board[rightCaptureSqr] != Piece.NONE
                && Piece.isColor(piece, board[rightCaptureSqr] & Piece.COLOR_MASK) == false) {
            moves.add(new Move(currIdx, rightCaptureSqr, piece , board[rightCaptureSqr], Move.NORMAL));
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
                boolean ifNewSqrDifColor = (board[currIdx] & Piece.COLOR_MASK) != (board[oldIdx] & Piece.COLOR_MASK);
                boolean ifNewSqrEmpty = board[currIdx] == Piece.NONE;
                if (ifNewSqrEmpty == true) {
                    moves.add(new Move(oldIdx, currIdx, board[oldIdx], Piece.NONE, Move.NORMAL));

                } else if (ifNewSqrDifColor == true) {
                    moves.add(new Move(oldIdx, currIdx, board[oldIdx], board[currIdx], Move.NORMAL));
                    break;
                } else {
                    break;
                }

            }

        }
        return moves;
    }

    public Collection<Move> generateKnightMoves(int rank, int file) {
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
        int currIdx = 8 * rank + file;
        int piece = board[currIdx];
        // for each direction piece can move find the valid moves
        for (int direction : directionsPieceCanMove) {
            

            int newIdx = currIdx + direction;
            // Decomp the currIdx into new Rank and new File

            int newFile = newIdx % 8;
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
            if (isOnBoard(newIdx) == false)
                continue;

            boolean ifNewSqrDifColor = (board[newIdx] & Piece.COLOR_MASK) != (piece & Piece.COLOR_MASK);
            boolean ifNewSqrEmpty = board[newIdx] == Piece.NONE;
            if (ifNewSqrEmpty == true) {
                moves.add(new Move(currIdx, newIdx, piece, Piece.NONE, Move.NORMAL));

            } else if (ifNewSqrDifColor == true) {
                moves.add(new Move(currIdx, newIdx, piece, board[newIdx], Move.NORMAL));
            }

        }
        return moves;
    }

    public Collection<Move> generateKingMoves(int rank, int file) {
        ArrayList<Move> moves = new ArrayList<Move>();
        int currIdx = 8 * rank + file;
        int piece = board[currIdx];
        // for each direction piece can move find the valid moves
        for (String eachDirection : dirToCompassRoseDirHM.keySet()) {

            int direction = dirToCompassRoseDirHM.get(eachDirection);
            int newIdx = currIdx+direction;
            // If the move is not dont consider it
            if (isOnBoard(newIdx) == false)
                continue;

            boolean ifNewSqrDifColor = (board[newIdx] & Piece.COLOR_MASK) != (piece & Piece.COLOR_MASK);
            boolean ifNewSqrEmpty = board[newIdx] == Piece.NONE;
            if (ifNewSqrEmpty == true) {
                moves.add(new Move(currIdx, newIdx, piece, Piece.NONE, Move.NORMAL));

            } else if (ifNewSqrDifColor == true) {
                moves.add(new Move(currIdx, newIdx, piece, board[newIdx], Move.NORMAL));
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

    public int[] getBoardArray(){
        return board;
    }
}
