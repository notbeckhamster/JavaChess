package src;

import java.util.ArrayList;

public class MoveGen {
    private Board board;
    private double[] pieceWeights = {0, 200,9, 5, 3, 3, 1};

    public MoveGen(Board board) {
        this.board = board;
    }

    public void playResponse() {
        Move move = rootNegaMax(4);
        board.makeMove(move, true);
       
        

    }

    

    public Move getRandomMove(){
        //Generate all possible moves
        ArrayList<Move> allMoves = genAllPseudoLegalMoves();
        //Choose a random move
        int randomIndex = (int) (Math.random() * allMoves.size());
        return allMoves.size() == 0 ? null : allMoves.get(randomIndex);


    }

    public double negaMax(int depth){
        
        if (depth == 0) return evaluate();
        double max = Double.NEGATIVE_INFINITY;
        for (Move eachmove : genAllPseudoLegalMoves()){
            board.makeMove(eachmove, false);
            double score = (-1)*negaMax(depth-1);
             board.unmakeMove(eachmove, false);
             if (score > max) max = score;
        }
        return max;
    }

    /** https://www.chessprogramming.org/Negamax reference logic kinad hard to explian here */
    public Move rootNegaMax(int depth){
        //Gen all of root moves 

       
        ArrayList<Move> rootMoves = genAllPseudoLegalMoves();
      
        double max = Double.NEGATIVE_INFINITY;
        Move optimalMove = null;
        for (Move eachMove : rootMoves){
            board.makeMove(eachMove, false);
            double score = negaMax(depth - 1);
            score = (-1)*score;
            board.unmakeMove(eachMove, false);
            if (score>max) {
                max = score;
                optimalMove = eachMove;
            }
        }
      
        
        return optimalMove;
    }

    public double evaluate(){
        //find turn
        int turn = board.getWhiteToMove() == true ? Piece.WHITE : Piece.BLACK;
        int oppTurn = turn == Piece.WHITE ? Piece.BLACK : Piece.WHITE;
        double mobilWeight = 0.25;
        return materialScore(turn) /** + mobilWeight*(generateMobilityEval(turn) - generateMobilityEval(oppTurn)) */;
    }

    /** <p> Generates the evalution of the mobility using Papa's/Wita entropy method </p>  
     * @param color
     * @return
     */
    public double generateMobilityEval(int color){
        int oppColor = color==Piece.WHITE ? Piece.BLACK : Piece.WHITE;
        double moveRatio = (double)genAllPseudoLegalMoves(color).size() /  (double)genAllPseudoLegalMoves(oppColor).size();
        double squaredControlledRatio =  (double)countControlledSquares(color) /  (double)countControlledSquares(oppColor);
        return Math.log(moveRatio * squaredControlledRatio);

    }

    /** <p> Generates the evaluation of the material </p> */
    public double materialScore(int color){
        //find the number of pieces on both sides
        ArrayList<Integer>[] currentSide = color == Piece.WHITE ? board.getWhitePieceArr() : board.getBlackPieceArr();
        ArrayList<Integer>[] oppSide = color == Piece.WHITE ? board.getBlackPieceArr() : board.getWhitePieceArr();
        double result = 0;
        for (int i = 1; i<=6; i++){
            result += pieceMaterialScore(i, currentSide[i].size(), oppSide[i].size());
        }
        return result;


    }

    public double pieceMaterialScore(int piece, int currSideNum, int oppSideNum){
        return pieceWeights[piece]*(currSideNum - oppSideNum);
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
            ArrayList<Integer> pieces = (ArrayList<Integer>) piecesArr[i].clone();
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
            ArrayList<Integer> pieces = (ArrayList<Integer>) piecesArr[i].clone();
            for (int j = 0; j<pieces.size(); j++){
                int position = pieces.get(j);
                result.addAll(board.validMoves(position/8, position%8, false));
            }
      
        }
        return result;


    }
}
