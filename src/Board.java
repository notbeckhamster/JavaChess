package src;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Stack;

public class Board {
    private int[] board = new int[64];
    private HashMap<String, Integer> dirToCompassRoseDirHM = new HashMap<String, Integer>();
    private HashMap<String, int[]> dirStrToEdgeHM = new HashMap<String, int[]>();
    private HashMap<Integer, int[]> dirIntToEdgeHM = new HashMap<Integer, int[]>();
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
    @SuppressWarnings("unchecked")
    private ArrayList<Integer>[] whitePieceArr = new ArrayList[7];
    private ArrayList<Integer> blackPawnSquares = new ArrayList<Integer>();
    private ArrayList<Integer> blackRookSquares = new ArrayList<Integer>();
    private ArrayList<Integer> blackKnightSquares = new ArrayList<Integer>();
    private ArrayList<Integer> blackBishopSquares = new ArrayList<Integer>();
    private ArrayList<Integer> blackQueenSquares = new ArrayList<Integer>();
    private ArrayList<Integer> blackKingSquares = new ArrayList<Integer>();
    @SuppressWarnings("unchecked")
    private ArrayList<Integer>[] blackPieceArr = new ArrayList[7];
    private boolean[] whiteAttackingSquares = new boolean[64];
    private boolean[] blackAttackingSquares = new boolean[64];
    private Stack<Move> registeredMoves = new Stack<Move>();
    private TopPanel piecePanel;
    private boolean whiteToMove = true;
    private boolean ifBlackKingSideCastle = true;
    private boolean ifBlackQueenSideCastle = true;
    private boolean ifWhiteKingSideCastle = true;
    private boolean ifWhiteQueenSideCastle = true;

    public Board() {

        initBoard();
        // set up board
        String defaultPos = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";
        setBoardFromFEN(defaultPos);
    }

    public Board(String fen) {
        initBoard();
        setBoardFromFEN(fen);
    }

    public ArrayList<Integer>[] getWhitePieceArr() {
        return whitePieceArr;
    }

    public ArrayList<Integer>[] getBlackPieceArr() {
        return blackPieceArr;
    }

    public void setIfWhiteIsToMove(boolean whiteToMove) {
        this.whiteToMove = whiteToMove;
    }

    public boolean ifWhiteIsToMove() {
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

        // setup compass rose
        dirToCompassRoseDirHM.put("nort", 8);
        dirToCompassRoseDirHM.put("noWe", 7);
        dirToCompassRoseDirHM.put("west", -1);
        dirToCompassRoseDirHM.put("soWe", -9);
        dirToCompassRoseDirHM.put("sout", -8);
        dirToCompassRoseDirHM.put("soEa", -7);
        dirToCompassRoseDirHM.put("east", 1);
        dirToCompassRoseDirHM.put("noEa", 9);

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

        // setup numToEdge Arrays
        dirStrToEdgeHM.put("nort", northNumToEdge);
        dirStrToEdgeHM.put("noWe", noWeNumToEdge);
        dirStrToEdgeHM.put("west", westNumToEdge);
        dirStrToEdgeHM.put("soWe", soWeNumToEdge);
        dirStrToEdgeHM.put("sout", southNumToEdge);
        dirStrToEdgeHM.put("soEa", soEaNumToEdge);
        dirStrToEdgeHM.put("east", eastNumToEdge);
        dirStrToEdgeHM.put("noEa", noEaNumToEdge);

        dirIntToEdgeHM.put(dirToCompassRoseDirHM.get("nort"), northNumToEdge);
        dirIntToEdgeHM.put(dirToCompassRoseDirHM.get("noWe"), noWeNumToEdge);
        dirIntToEdgeHM.put(dirToCompassRoseDirHM.get("west"), westNumToEdge);
        dirIntToEdgeHM.put(dirToCompassRoseDirHM.get("soWe"), soWeNumToEdge);
        dirIntToEdgeHM.put(dirToCompassRoseDirHM.get("sout"), southNumToEdge);
        dirIntToEdgeHM.put(dirToCompassRoseDirHM.get("soEa"), soEaNumToEdge);
        dirIntToEdgeHM.put(dirToCompassRoseDirHM.get("east"), eastNumToEdge);
        dirIntToEdgeHM.put(dirToCompassRoseDirHM.get("noEa"), noEaNumToEdge);
    }

    public Stack<Move> getRegisteredMoves() {
        return registeredMoves;
    }

    public void setPieceLists() {
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
        setAttackedSquares();
    }

    public void updatePieceList(Move move) {

          for (int i = 1; i < blackPieceArr.length; i++) {
            blackPieceArr[i].clear();
            whitePieceArr[i].clear();
        }
        for (int i = 0; i < 64; i++) {
            int piece = board[i];
            int idx = piece & Piece.PIECE_MASK;
            if (idx != Piece.NONE) {
                if ((piece & Piece.COLOR_MASK) == Piece.WHITE) {
                    whitePieceArr[idx].add(i);
                } else {
                    blackPieceArr[idx].add(i);
                }
            }
        }
        setAttackedSquares();

    }

    /** repeat of update piece list swapped add and remove */
    public void updatePieceListUnmake(Move move) {
        for (int i = 1; i < blackPieceArr.length; i++) {
            blackPieceArr[i].clear();
            whitePieceArr[i].clear();
        }
        for (int i = 0; i < 64; i++) {
            int piece = board[i];
            int idx = piece & Piece.PIECE_MASK;
            if (idx != Piece.NONE) {
                if ((piece & Piece.COLOR_MASK) == Piece.WHITE) {
                    whitePieceArr[idx].add(i);
                } else {
                    blackPieceArr[idx].add(i);
                }
            }
        }
        setAttackedSquares();

    }

    public void makeMove(Move move, boolean ifUpdateGUI) {
        int oldIdx = move.getOldSqr();
        int newIdx = move.getNewSqr();

        // Update Board
        board[newIdx] = move.getPieceMoved();
        board[newIdx] = board[newIdx] | Piece.MOVED;
        board[oldIdx] = Piece.NONE;

        registeredMoves.push(move);

        if (ifUpdateGUI)
            piecePanel.getPiecePanels()[oldIdx].setPiece(Piece.NONE);

        if (move.getFlags() == Move.NORMAL) {

            if (ifUpdateGUI)
                piecePanel.getPiecePanels()[newIdx].setPiece(move.getPieceMoved());

        } else if (move.getFlags() == Move.PROMOTION) {
            board[newIdx] = move.getPromoPiece();
            if (ifUpdateGUI)
                piecePanel.getPiecePanels()[newIdx].setPiece(board[newIdx]);

        } else if (move.getFlags() == Move.ENPASSANT) {

            if (ifUpdateGUI) {

                piecePanel.getPiecePanels()[newIdx].setPiece(move.getPieceMoved());
                int capturedPawnIdx = registeredMoves.get(registeredMoves.size() - 2).getNewSqr();
                piecePanel.getPiecePanels()[capturedPawnIdx].setPiece(Piece.NONE);

            }

            // en passant
            // remove pawn
            // Captured pawn idx
            int capturedPawnIdx = registeredMoves.get(registeredMoves.size() - 2).getNewSqr();
            board[capturedPawnIdx] = Piece.NONE;

        } else if (move.getFlags() == Move.CASTLE) {
            if (ifUpdateGUI)
                piecePanel.getPiecePanels()[newIdx].setPiece(move.getPieceMoved());
            if (move.getNewSqr() == 2) {

                board[3] = board[0];
                board[0] = Piece.NONE;
                if (ifUpdateGUI) {
                    piecePanel.getPiecePanels()[3].setPiece(board[3]);
                    piecePanel.getPiecePanels()[0].setPiece(Piece.NONE);
                }

            } else if (move.getNewSqr() == 6) {

                board[5] = board[7];
                board[7] = Piece.NONE;
                if (ifUpdateGUI) {
                    piecePanel.getPiecePanels()[5].setPiece(board[5]);
                    piecePanel.getPiecePanels()[7].setPiece(Piece.NONE);
                }

            } else if (move.getNewSqr() == 58) {

                board[59] = board[56];
                board[56] = Piece.NONE;
                if (ifUpdateGUI) {
                    piecePanel.getPiecePanels()[59].setPiece(board[59]);
                    piecePanel.getPiecePanels()[56].setPiece(Piece.NONE);
                }

            } else if (move.getNewSqr() == 62) {

                board[61] = board[63];
                board[63] = Piece.NONE;
                if (ifUpdateGUI) {
                    piecePanel.getPiecePanels()[61].setPiece(board[61]);
                    piecePanel.getPiecePanels()[63].setPiece(Piece.NONE);
                }

            }
        }

        // Update Piece Lists
        updatePieceList(move);
        // Update white to move
        whiteToMove = !whiteToMove;
    }

    public void unmakeMove(Move move, boolean ifUpdateGUI) {
        // First reverse the piece at newSqr to oldSqr, bring back the piece at new Sqr
        // if any
        int oldIdx = move.getOldSqr();
        int newIdx = move.getNewSqr();
        board[oldIdx] = move.getPieceMoved();
        board[newIdx] = move.getPiecesAtNewSquare();
        board[newIdx] = board[newIdx] & ~Piece.MOVED_MASK;

        if (move.getFlags() == Move.ENPASSANT) {
            int capturedPawnIdx = registeredMoves.get(registeredMoves.size() - 2).getNewSqr();
            int colorOfCapturedPawn = ((move.getPieceMoved() & Piece.COLOR_MASK) == Piece.WHITE) ? Piece.BLACK
                    : Piece.WHITE;
            board[capturedPawnIdx] = Piece.PAWN | colorOfCapturedPawn;
            if (ifUpdateGUI)
                piecePanel.getPiecePanels()[capturedPawnIdx].setPiece(board[capturedPawnIdx]);

        } else if (move.getFlags() == Move.CASTLE) {
            if (move.getNewSqr() == 2) {
                board[0] = board[3];
                board[3] = Piece.NONE;
                if (ifUpdateGUI) {
                    piecePanel.getPiecePanels()[0].setPiece(board[0]);
                    piecePanel.getPiecePanels()[3].setPiece(Piece.NONE);
                }
            } else if (move.getNewSqr() == 6) {
                board[7] = board[5];
                board[5] = Piece.NONE;
                if (ifUpdateGUI) {
                    piecePanel.getPiecePanels()[7].setPiece(board[7]);
                    piecePanel.getPiecePanels()[5].setPiece(Piece.NONE);
                }
            } else if (move.getNewSqr() == 58) {
                board[56] = board[59];
                board[59] = Piece.NONE;
                if (ifUpdateGUI) {
                    piecePanel.getPiecePanels()[56].setPiece(board[56]);
                    piecePanel.getPiecePanels()[59].setPiece(Piece.NONE);
                }
            } else if (move.getNewSqr() == 62) {
                board[63] = board[61];
                board[61] = Piece.NONE;
                if (ifUpdateGUI) {
                    piecePanel.getPiecePanels()[63].setPiece(board[63]);
                    piecePanel.getPiecePanels()[61].setPiece(Piece.NONE);
                }
            }
        }

        updatePieceListUnmake(move);
        registeredMoves.pop();
        whiteToMove = !whiteToMove;
    }

    public void setPiecePanel(TopPanel piecePanel) {
        this.piecePanel = piecePanel;
    }

    public Collection<Move> validMoves(int rank, int file, boolean ifForGUI) {
        int currIdx = rank * 8 + file;
        int piece = board[currIdx];
        // Generate moves
        Collection<Move> moves = generateValidMoves(currIdx, piece, ifForGUI);

        // Filter out moves that leave king in check
        Collection<Move> movesCopy = new ArrayList<Move>(moves);
        for (Move eachMove : movesCopy) {
            makeMove(eachMove, false);
            if (isKingInCheck(piece & Piece.COLOR_MASK) == true) {
                moves.remove(eachMove);
            }
            unmakeMove(eachMove, false);
        }
        return moves;

    }

    public boolean isKingInCheck(int color) {
        // Pick attackedSquaresArr based on opposite color
        boolean[] attackedSquares = (color == Piece.WHITE) ? blackAttackingSquares : whiteAttackingSquares;
        // Find king on color
        if (color == Piece.WHITE){
            if (whitePieceArr[Piece.KING].isEmpty()) return true;
        } else {
 if (blackPieceArr[Piece.KING].isEmpty()) return true;
        }
        int kingIdx = (color == Piece.WHITE) ? whitePieceArr[Piece.KING].get(0) : blackPieceArr[Piece.KING].get(0);
        // Check if king is under attack
        return attackedSquares[kingIdx];
    }

    public boolean isIndexInCheck(int idx, int color) {
        // Pick attackedSquaresArr based on opposite color
        boolean[] attackedSquares = (color == Piece.WHITE) ? blackAttackingSquares : whiteAttackingSquares;

        // Check if piece is under attack
        return attackedSquares[idx];
    }

    public Collection<Move> generateValidMoves(int currIdx, int piece, boolean ifForGUI) {
        if (isSlidingPiece(piece) == true) {
            return generateSlidingMoves(currIdx, piece);
        } else if ((piece & Piece.PIECE_MASK) == Piece.KNIGHT) {
            return generateKnightMoves(currIdx);
        } else if ((piece & Piece.PIECE_MASK) == Piece.PAWN) {
            return generatePawnMoves(currIdx, ifForGUI);
        } else if ((piece & Piece.PIECE_MASK) == Piece.KING) {
            return generateKingMoves(currIdx);
        } else {
            ArrayList<Move> result = new ArrayList<Move>();
            return result;
        }
    }

    public void setAttackedSquares() {
        // clear the exisitng arrays... try new chekc profiler
            Arrays.fill(whiteAttackingSquares,false);
            Arrays.fill(blackAttackingSquares,false);
     


        // Set attacked squares for king
        if (whitePieceArr[Piece.KING].size() != 0) {
            Collection<Move> kingMoves = generateKingMoves(whitePieceArr[Piece.KING].get(0));
            for (Move move : kingMoves) {
                whiteAttackingSquares[move.getNewSqr()] = true;
            }
        }

        // set attacked squares for queen
        for (int i = 0; i < whitePieceArr[Piece.QUEEN].size(); i++) {
            Collection<Move> queenMoves = generateSlidingMoves(whitePieceArr[Piece.QUEEN].get(i),
                    Piece.QUEEN);
            for (Move move : queenMoves) {
                whiteAttackingSquares[move.getNewSqr()] = true;
            }
        }
        // set attacked squares for rook
        for (int i = 0; i < whitePieceArr[Piece.ROOK].size(); i++) {
            Collection<Move> rookMoves = generateSlidingMoves(whitePieceArr[Piece.ROOK].get(i), Piece.ROOK);
            for (Move move : rookMoves) {
                whiteAttackingSquares[move.getNewSqr()] = true;
            }
        }
        // set attacked squares for bishop
        for (int i = 0; i < whitePieceArr[Piece.BISHOP].size(); i++) {
            Collection<Move> bishopMoves = generateSlidingMoves(whitePieceArr[Piece.BISHOP].get(i),
                    Piece.BISHOP);
            for (Move move : bishopMoves) {
                whiteAttackingSquares[move.getNewSqr()] = true;
            }
        }
        // set attacked squares for knight
        for (int i = 0; i < whitePieceArr[Piece.KNIGHT].size(); i++) {
            Collection<Move> knightMoves = generateKnightMoves(whitePieceArr[Piece.KNIGHT].get(i));
            for (Move move : knightMoves) {
                whiteAttackingSquares[move.getNewSqr()] = true;
            }
        }
        // set attacked squares for pawn
        for (int i = 0; i < whitePieceArr[Piece.PAWN].size(); i++) {
            Collection<Move> pawnMoves = generatePawnAttacks(whitePieceArr[Piece.PAWN].get(i));
            for (Move move : pawnMoves) {
                whiteAttackingSquares[move.getNewSqr()] = true;
            }
        }
            // Set attacked squares for king
    if (blackPieceArr[Piece.KING].size() != 0) {
        Collection<Move> kingMoves = generateKingMoves(blackPieceArr[Piece.KING].get(0));
        for (Move move : kingMoves) {
            blackAttackingSquares[move.getNewSqr()] = true;
        }
    }

    // set attacked squares for queen
    for (int i = 0; i < blackPieceArr[Piece.QUEEN].size(); i++) {
        Collection<Move> queenMoves = generateSlidingMoves(blackPieceArr[Piece.QUEEN].get(i),
                Piece.QUEEN);
        for (Move move : queenMoves) {
            blackAttackingSquares[move.getNewSqr()] = true;
        }
    }
    // set attacked squares for rook
    for (int i = 0; i < blackPieceArr[Piece.ROOK].size(); i++) {
        Collection<Move> rookMoves = generateSlidingMoves(blackPieceArr[Piece.ROOK].get(i), Piece.ROOK);
        for (Move move : rookMoves) {
            blackAttackingSquares[move.getNewSqr()] = true;
        }
    }
    // set attacked squares for bishop
    for (int i = 0; i < blackPieceArr[Piece.BISHOP].size(); i++) {
        Collection<Move> bishopMoves = generateSlidingMoves(blackPieceArr[Piece.BISHOP].get(i),
                Piece.BISHOP);
        for (Move move : bishopMoves) {
            blackAttackingSquares[move.getNewSqr()] = true;
        }
    }
    // set attacked squares for knight
    for (int i = 0; i < blackPieceArr[Piece.KNIGHT].size(); i++) {
        Collection<Move> knightMoves = generateKnightMoves(blackPieceArr[Piece.KNIGHT].get(i));
        for (Move move : knightMoves) {
            blackAttackingSquares[move.getNewSqr()] = true;
        }
    }
    // set attacked squares for pawn
    for (int i = 0; i < blackPieceArr[Piece.PAWN].size(); i++) {
        Collection<Move> pawnMoves = generatePawnAttacks(blackPieceArr[Piece.PAWN].get(i));
        for (Move move : pawnMoves) {
            blackAttackingSquares[move.getNewSqr()] = true;
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

    public Collection<Move> generatePawnAttacks(int currIdx) {
        ArrayList<Move> moves = new ArrayList<Move>();

        int piece = board[currIdx];

        // Add the diagonal moves if there is an enemy piece
        String leftDirectionKey = Piece.isColor(piece, Piece.WHITE) ? "noWe" : "soWe";
        String rightDirectionKey = Piece.isColor(piece, Piece.WHITE) ? "noEa" : "soEa";

        int leftCaptureSqr = currIdx + dirToCompassRoseDirHM.get(leftDirectionKey);
        int rightCaptureSqr = currIdx + dirToCompassRoseDirHM.get(rightDirectionKey);
        // Check captures are moves on the board
        int numToEdgeLeft = dirStrToEdgeHM.get(leftDirectionKey)[currIdx];
        int numToEdgeRight = dirStrToEdgeHM.get(rightDirectionKey)[currIdx];
        if (numToEdgeLeft >= 1)
            moves.add(new Move(currIdx, leftCaptureSqr, piece, board[leftCaptureSqr], Move.NORMAL));

        if (numToEdgeRight >= 1)
            moves.add(new Move(currIdx, rightCaptureSqr, piece, board[rightCaptureSqr], Move.NORMAL));

        return moves;

    }

    public Collection<Move> generatePawnMoves(int currIdx, boolean ifForGUI) {
        ArrayList<Move> moves = new ArrayList<Move>();
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

            int moveType = (idxOneMoveForward / 8 == 0 || idxOneMoveForward / 8 == 7) ? Move.PROMOTION : Move.NORMAL;
            if (moveType == Move.NORMAL) {
                moves.add(new Move(currIdx, idxOneMoveForward, piece, board[idxOneMoveForward], moveType));
            } else {
                Move queenPromoMove = new Move(currIdx, idxOneMoveForward, piece, board[idxOneMoveForward], moveType);
                queenPromoMove.setPromoPiece(Piece.QUEEN | (piece & Piece.COLOR_MASK));
                moves.add(queenPromoMove);
                if (ifForGUI == false) {
                    Move knightPromoMove = new Move(currIdx, idxOneMoveForward, piece, board[idxOneMoveForward],
                            moveType);
                    knightPromoMove.setPromoPiece(Piece.KNIGHT | (piece & Piece.COLOR_MASK));
                    moves.add(knightPromoMove);
                    Move rookPromoMove = new Move(currIdx, idxOneMoveForward, piece, board[idxOneMoveForward],
                            moveType);
                    rookPromoMove.setPromoPiece(Piece.ROOK | (piece & Piece.COLOR_MASK));
                    moves.add(rookPromoMove);
                    Move bishopPromoMove = new Move(currIdx, idxOneMoveForward, piece, board[idxOneMoveForward],
                            moveType);
                    bishopPromoMove.setPromoPiece(Piece.BISHOP | (piece & Piece.COLOR_MASK));
                    moves.add(bishopPromoMove);

                }
            }
        }

        int rank = currIdx/8;
        // Add the starting two move forward if on starting rank
        boolean ifOnStartingRank = (rank == 1 && Piece.isColor(piece, Piece.WHITE))
                || (rank == 6 && Piece.isColor(piece, Piece.BLACK));
        if (ifOnStartingRank == true) {
            int idxTwoMoveForward = currIdx + 2 * direction;
            if (board[idxTwoMoveForward] == Piece.NONE && board[idxOneMoveForward] == Piece.NONE) {
                moves.add(new Move(currIdx, idxTwoMoveForward, piece, Piece.NONE, Move.NORMAL));
            }
        }

        // Add the diagonal moves if there is an enemy piece
        String leftDirectionKey = Piece.isColor(piece, Piece.WHITE) ? "noWe" : "soWe";
        String rightDirectionKey = Piece.isColor(piece, Piece.WHITE) ? "noEa" : "soEa";

        int leftCaptureSqr = currIdx + dirToCompassRoseDirHM.get(leftDirectionKey);
        int rightCaptureSqr = currIdx + dirToCompassRoseDirHM.get(rightDirectionKey);

        // Check captures are moves on the board
        int numToEdgeLeft = dirStrToEdgeHM.get(leftDirectionKey)[currIdx];
        int numToEdgeRight = dirStrToEdgeHM.get(rightDirectionKey)[currIdx];
        if (isOnBoard(leftCaptureSqr) && numToEdgeLeft > 0 && board[leftCaptureSqr] != Piece.NONE
                && Piece.isColor(piece, board[leftCaptureSqr] & Piece.COLOR_MASK) == false) {
            int moveType = (leftCaptureSqr / 8 == 0 || leftCaptureSqr / 8 == 7) ? Move.PROMOTION : Move.NORMAL;
            if (moveType == Move.NORMAL) {
                moves.add(new Move(currIdx, leftCaptureSqr, piece, board[leftCaptureSqr], moveType));
            } else {
                Move queenPromoMove = new Move(currIdx, leftCaptureSqr, piece, board[leftCaptureSqr], moveType);
                queenPromoMove.setPromoPiece(Piece.QUEEN | (piece & Piece.COLOR_MASK));
                moves.add(queenPromoMove);
                if (ifForGUI == false) {
                    Move knightPromoMove = new Move(currIdx, leftCaptureSqr, piece, board[leftCaptureSqr], moveType);
                    knightPromoMove.setPromoPiece(Piece.KNIGHT | (piece & Piece.COLOR_MASK));
                    moves.add(knightPromoMove);
                    Move rookPromoMove = new Move(currIdx, leftCaptureSqr, piece, board[leftCaptureSqr], moveType);
                    rookPromoMove.setPromoPiece(Piece.ROOK | (piece & Piece.COLOR_MASK));
                    moves.add(rookPromoMove);
                    Move bishopPromoMove = new Move(currIdx, leftCaptureSqr, piece, board[leftCaptureSqr], moveType);
                    bishopPromoMove.setPromoPiece(Piece.BISHOP | (piece & Piece.COLOR_MASK));
                    moves.add(bishopPromoMove);

                }
            }

        }
        if (isOnBoard(rightCaptureSqr) && numToEdgeRight > 0 && board[rightCaptureSqr] != Piece.NONE
                && Piece.isColor(piece, board[rightCaptureSqr] & Piece.COLOR_MASK) == false) {
            int moveType = (rightCaptureSqr / 8 == 0 || rightCaptureSqr / 8 == 7) ? Move.PROMOTION : Move.NORMAL;
            if (moveType == Move.NORMAL) {
                moves.add(new Move(currIdx, rightCaptureSqr, piece, board[rightCaptureSqr], moveType));
            } else {
                Move queenPromoMove = new Move(currIdx, rightCaptureSqr, piece, board[rightCaptureSqr], moveType);
                queenPromoMove.setPromoPiece(Piece.QUEEN | (piece & Piece.COLOR_MASK));
                moves.add(queenPromoMove);
                if (ifForGUI == false) {
                    Move knightPromoMove = new Move(currIdx, rightCaptureSqr, piece, board[rightCaptureSqr], moveType);
                    knightPromoMove.setPromoPiece(Piece.KNIGHT | (piece & Piece.COLOR_MASK));
                    moves.add(knightPromoMove);
                    Move rookPromoMove = new Move(currIdx, rightCaptureSqr, piece, board[rightCaptureSqr], moveType);
                    rookPromoMove.setPromoPiece(Piece.ROOK | (piece & Piece.COLOR_MASK));
                    moves.add(rookPromoMove);
                    Move bishopPromoMove = new Move(currIdx, rightCaptureSqr, piece, board[rightCaptureSqr], moveType);
                    bishopPromoMove.setPromoPiece(Piece.BISHOP | (piece & Piece.COLOR_MASK));
                    moves.add(bishopPromoMove);

                }
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
                boolean ifBeside = (lastNewIdx == currIdx + dirToCompassRoseDirHM.get("east") && numToEdgeRight != 0)
                        || (lastNewIdx == currIdx + dirToCompassRoseDirHM.get("west") && numToEdgeLeft != 0);
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
        return moves;

    }

    public Collection<Move> generateSlidingMoves(int idx, int piece) {
        ArrayList<Move> moves = new ArrayList<Move>();
        // generate directions piece can move
        ArrayList<Integer> directionsPieceCanMove = generateDirections(piece);
        // for each direction piece can move find the valid moves
        for (Integer direction : directionsPieceCanMove) {
            int numToEdge = dirIntToEdgeHM.get(direction)[idx];
            int currIdx = idx;
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

    public Collection<Move> generateKnightMoves(int currIdx) {
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
        int piece = board[currIdx];
        // for each direction piece can move find the valid moves
        for (int direction : directionsPieceCanMove) {

            int newIdx = currIdx + direction;
            // Decomp the currIdx into new Rank and new File

            int newFile = newIdx % 8;
            // If knight on the right side of the board and the new move
            // is on the left break out
            int file = currIdx % 8;
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

  
    public Collection<Move> generateKingMoves(int currIdx) {
        ArrayList<Move> moves = new ArrayList<Move>();
        int piece = board[currIdx];
        int color = piece & Piece.COLOR_MASK;
        // for each direction piece can move find the valid moves
        for (String eachDirection : dirToCompassRoseDirHM.keySet()) {

            int direction = dirToCompassRoseDirHM.get(eachDirection);
            int newIdx = currIdx + direction;
            // If the move is not dont consider it
            if (isOnBoard(newIdx) == false)
                continue;

            int numToEdge = dirStrToEdgeHM.get(eachDirection)[currIdx];
            boolean ifNewSqrDifColor = (board[newIdx] & Piece.COLOR_MASK) != (piece & Piece.COLOR_MASK);
            boolean ifNewSqrEmpty = board[newIdx] == Piece.NONE;
            if (ifNewSqrEmpty == true && numToEdge > 0) {
                moves.add(new Move(currIdx, newIdx, piece, Piece.NONE, Move.NORMAL));

            } else if (ifNewSqrDifColor == true && numToEdge > 0) {
                moves.add(new Move(currIdx, newIdx, piece, board[newIdx], Move.NORMAL));
            }

        }
        // Add boolean flags from position to Fenn is castling rights are available
        boolean kingSideCastle = color == Piece.WHITE ? ifWhiteKingSideCastle : ifBlackKingSideCastle;
        boolean queenSideCastle = color == Piece.WHITE ? ifWhiteQueenSideCastle : ifBlackQueenSideCastle;
        // Castling

        // If king has moved dont consider castling
        if ((piece & Piece.MOVED_MASK) == Piece.MOVED)
            return moves;
        // If king isnt in its position then dont consider castling
        int whereKingIdxShouldBe = color == Piece.WHITE ? 4 : 60;
        if (currIdx != whereKingIdxShouldBe)
            return moves;
        // If king is in check dont consider castling
        if (isKingInCheck(piece & Piece.COLOR_MASK))
            return moves;

        // Determine if king can castle on the right side of the board
        boolean ifRightCastleValid = kingSideCastle;
        // Check space is clear on the right side
        for (int i = currIdx + 1; i < currIdx + 3 && ifRightCastleValid == true; i++) {

            if (board[i] != Piece.NONE)
                ifRightCastleValid = false;
            if (i <= currIdx + 2 && isIndexInCheck(i, color))
                ifRightCastleValid = false;

        }

        // Check the right rook if present it hasnt moved or the rook isnt there
        if ((board[whereKingIdxShouldBe + 3] & Piece.PIECE_MASK) == Piece.ROOK
                && (board[whereKingIdxShouldBe + 3] & Piece.MOVED_MASK) == Piece.MOVED)
            ifRightCastleValid = false;
        if ((board[whereKingIdxShouldBe + 3] & Piece.PIECE_MASK) != Piece.ROOK)
            ifRightCastleValid = false;
        // Determine if king can castle on the left side of the board
        boolean ifLeftCastleValid = queenSideCastle;
        // Check space is clear on the left side
        for (int i = currIdx - 1; i > currIdx - 4 && ifLeftCastleValid == true; i--) {

            if (board[i] != Piece.NONE)
                ifLeftCastleValid = false;
            if (i >= currIdx - 2 && isIndexInCheck(i, color))
                ifLeftCastleValid = false;

        }
        // Check the left rook if present it hasnt moved or the rook isnt there
        if ((board[whereKingIdxShouldBe - 4] & Piece.PIECE_MASK) == Piece.ROOK
                && (board[whereKingIdxShouldBe - 4] & Piece.MOVED_MASK) == Piece.MOVED)
            ifLeftCastleValid = false;
        if ((board[whereKingIdxShouldBe - 4] & Piece.PIECE_MASK) != Piece.ROOK)
            ifLeftCastleValid = false;

        // Add castle moves ifLeftCastleValid or ifRightCastleValid is true
        if (ifLeftCastleValid == true)
            moves.add(new Move(currIdx, currIdx - 2, piece, Piece.NONE, Move.CASTLE));
        if (ifRightCastleValid == true)
            moves.add(new Move(currIdx, currIdx + 2, piece, Piece.NONE, Move.CASTLE));
        return moves;
    }

    public ArrayList<Integer> generateDirections(int piece) {
        int pieceType = piece & Piece.PIECE_MASK;
        ArrayList<Integer> directions = null;
        if (pieceType == Piece.ROOK) {
            directions = new ArrayList<Integer>(Arrays.asList(8, -1, 1, -8));
        } else if (pieceType == Piece.BISHOP) {
            directions = new ArrayList<Integer>(Arrays.asList(7, -9, -7, 9));
        } else if (pieceType == Piece.QUEEN) {
            directions = new ArrayList<Integer>(Arrays.asList(7, -9, -7, 9, 8, -1, 1, -8));
        }
        return directions;
    }

    public int[] getBoardArray() {
        return board;
    }

    public void setBoardFromFEN(String fen) {
        HashMap<Character, Integer> pieceToPieceMaskHM = new HashMap<Character, Integer>();
        pieceToPieceMaskHM.put('p', Piece.PAWN | Piece.BLACK);
        pieceToPieceMaskHM.put('n', Piece.KNIGHT | Piece.BLACK);
        pieceToPieceMaskHM.put('b', Piece.BISHOP | Piece.BLACK);
        pieceToPieceMaskHM.put('r', Piece.ROOK | Piece.BLACK);
        pieceToPieceMaskHM.put('q', Piece.QUEEN | Piece.BLACK);
        pieceToPieceMaskHM.put('k', Piece.KING | Piece.BLACK);
        pieceToPieceMaskHM.put('P', Piece.PAWN | Piece.WHITE);
        pieceToPieceMaskHM.put('N', Piece.KNIGHT | Piece.WHITE);
        pieceToPieceMaskHM.put('B', Piece.BISHOP | Piece.WHITE);
        pieceToPieceMaskHM.put('R', Piece.ROOK | Piece.WHITE);
        pieceToPieceMaskHM.put('Q', Piece.QUEEN | Piece.WHITE);
        pieceToPieceMaskHM.put('K', Piece.KING | Piece.WHITE);

        // Clear board
        board = new int[64];

        String[] fenArr = fen.split(" ");
        // Determine the piece placement
        int file = 0;
        int rank = 7;
        String piecePlacement = fenArr[0];
        for (int i = 0; i < piecePlacement.length(); i++) {
            char currChar = piecePlacement.charAt(i);
            if (currChar == '/') {
                rank--;
                file = 0;
            } else {
                // Either a number or a piece
                if (Character.isDigit(currChar)) {
                    file += Character.getNumericValue(currChar);
                } else {
                    int piece = pieceToPieceMaskHM.get(currChar);
                    int currIdx = 8 * rank + file;
                    board[currIdx] = piece;
                    file++;
                }

            }

        }
        // Determine the active color/ side to move
        String activeColor = fenArr[1];
        if (activeColor.equals("w")) {
            whiteToMove = true;
        } else {
            whiteToMove = false;
        }

        // Determine castling rights
        String castlingRights = fenArr[2];
        for (int i = 0; i < castlingRights.length(); i++) {
            char currChar = castlingRights.charAt(i);
            if (currChar == '-') {
                ifWhiteKingSideCastle = false;
                ifWhiteQueenSideCastle = false;
                ifBlackKingSideCastle = false;
                ifBlackQueenSideCastle = false;
                break;
            } else {
                if (currChar == 'K') {
                    ifWhiteKingSideCastle = true;
                } else if (currChar == 'Q') {
                    ifWhiteQueenSideCastle = true;
                } else if (currChar == 'k') {
                    ifBlackKingSideCastle = true;
                } else if (currChar == 'q') {
                    ifBlackQueenSideCastle = true;
                }
            }
        }

        // Determine en passant square
        String enPassantSquare = fenArr[3];
        if (enPassantSquare.equals("-")) {
            // DoNothing
        } else {
            int fileIdx = enPassantSquare.charAt(0) - 'a';
            int rankIdx = enPassantSquare.charAt(1) - '1';
            int enPassantSquareIdx = 8 * rankIdx + fileIdx;
            if (whiteToMove) {
                registeredMoves.add(new Move(enPassantSquareIdx + dirToCompassRoseDirHM.get("nort"),
                        enPassantSquareIdx + dirToCompassRoseDirHM.get("sout"), Piece.PAWN | Piece.BLACK, Piece.NONE,
                        Move.ENPASSANT));
            } else {
                registeredMoves.add(new Move(enPassantSquareIdx + dirToCompassRoseDirHM.get("sout"),
                        enPassantSquareIdx - dirToCompassRoseDirHM.get("nort"), Piece.PAWN | Piece.WHITE, Piece.NONE,
                        Move.ENPASSANT));
            }
        }
        setPieceLists();
    }

    public boolean getWhiteToMove() {
        return whiteToMove;
    }
}
