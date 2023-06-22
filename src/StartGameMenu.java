package src;

import java.awt.event.ActionEvent;
import java.util.ArrayList;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JMenu;

public class StartGameMenu{
    private ArrayList<Action> actions;

    public StartGameMenu() {
        actions = new ArrayList<Action>();
        actions.add(new PlayWhiteAction());
        actions.add(new PlayBlackAction());
        actions.add(new AIvsAIAction());
    }


    public JMenu createMenu(){
        JMenu menu = new JMenu("Start Game");
        for (Action action : actions){
            menu.add(action);
        }
        return menu;
    }

    public class PlayWhiteAction extends AbstractAction{
        public PlayWhiteAction(){
            super("Play as White");
        }
        public void actionPerformed(ActionEvent e){
         //   ChessFrame.getChessPanel().startGame(Piece.WHITE);
        }
    }
    public class PlayBlackAction extends AbstractAction{
        public PlayBlackAction(){
            super("Play as Black");
        }
        public void actionPerformed(ActionEvent e){
          //  ChessFrame.getChessPanel().startGame(Piece.BLACK);
        }
    }
    public class AIvsAIAction extends AbstractAction{
        public AIvsAIAction(){
            super("AI vs AI");
        }
        public void actionPerformed(ActionEvent e){
           // ChessFrame.getChessPanel().startGame(Piece.WHITE);
        }
    }
}
