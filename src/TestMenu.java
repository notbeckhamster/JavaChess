package src;


import java.awt.event.ActionEvent;
import java.util.ArrayList;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JMenu;

public class TestMenu{
    private ArrayList<Action> actions;

    public TestMenu() {
        actions = new ArrayList<Action>();
        actions.add(new UnmakeLastMove());
        actions.add(new HighLightWhiteAttacks());
        actions.add(new HighLightBlackAttacks());
        actions.add(new ResetAttackHighlights());
    
    }


    public JMenu createMenu(){
        JMenu menu = new JMenu("Testing");
        for (Action action : actions){
            menu.add(action);
        }
        return menu;
    }

    public class UnmakeLastMove extends AbstractAction{
        public UnmakeLastMove(){
            super("Unmake Last Move");
        }
        public void actionPerformed(ActionEvent e){
            Board board =  ChessFrame.getChessPanel().getBoard();
            board.unmakeMove(board.getRegisteredMoves().get(board.getRegisteredMoves().size()-1), true);
            ChessFrame.getChessPanel().getTopPanel().updateForTesting();
            ChessFrame.getChessPanel().revalidate();
            ChessFrame.getChessPanel().repaint();
        }
    }
    public class HighLightWhiteAttacks extends AbstractAction{
        public HighLightWhiteAttacks(){
            super("HighLightWhiteAttacks");
        }
        public void actionPerformed(ActionEvent e){
            ChessFrame.getChessPanel().highlightAttackedSquares(Piece.WHITE);
            ChessFrame.getChessPanel().getTopPanel().updateForTesting();
            ChessFrame.getChessPanel().revalidate();
            ChessFrame.getChessPanel().repaint();
        }
    }
    public class HighLightBlackAttacks extends AbstractAction{
        public HighLightBlackAttacks(){
            super("HighLightBlackAttacks");
        }
        public void actionPerformed(ActionEvent e){
             ChessFrame.getChessPanel().highlightAttackedSquares(Piece.BLACK);
            ChessFrame.getChessPanel().getTopPanel().updateForTesting();
            ChessFrame.getChessPanel().revalidate();
            ChessFrame.getChessPanel().repaint();
        }
    }
    public class ResetAttackHighlights extends AbstractAction{
        public ResetAttackHighlights(){
            super("ResetAttackHighlights");
        }
        public void actionPerformed(ActionEvent e){
             ChessFrame.getChessPanel().resetAttackHighlights();
            ChessFrame.getChessPanel().getTopPanel().updateForTesting();
            ChessFrame.getChessPanel().revalidate();
            ChessFrame.getChessPanel().repaint();
        }
    }
   
}
