package edu.upc.epsevg.prop.othello;

import edu.upc.epsevg.prop.othello.players.HumanPlayer;
import edu.upc.epsevg.prop.othello.players.RandomPlayer;
import edu.upc.epsevg.prop.othello.Level;
import edu.upc.epsevg.prop.othello.IPlayer;
import edu.upc.epsevg.prop.othello.players.DesdemonaPlayer;
<<<<<<< Updated upstream
import edu.upc.epsevg.prop.othello.players.omatic.PlayerMiniMax;
=======
<<<<<<< HEAD
import edu.upc.epsevg.prop.othello.players.omatic.*;
=======
import edu.upc.epsevg.prop.othello.players.omatic.PlayerMiniMax;
>>>>>>> f38258d2abfe1123e6f7e54b49fb73ac93208df7
>>>>>>> Stashed changes


import javax.swing.SwingUtilities;

/**
 * Lines Of Action: el joc de taula.
 * @author bernat
 */
public class Game {
        /**
     * @param args
     */
    public static void main(String[] args) {
        
        
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                
<<<<<<< Updated upstream
=======
<<<<<<< HEAD
                IPlayer player1 = new PlayerID();
=======
>>>>>>> f38258d2abfe1123e6f7e54b49fb73ac93208df7
>>>>>>> Stashed changes
                //IPlayer player1 = new RandomPlayer("Crazy Ivan");
                //IPlayer player1 = new HumanPlayer("Human1");
                IPlayer player1 = new DesdemonaPlayer(30);//GB
                IPlayer player2 = new PlayerMiniMax(8);//GB

                                
                new Board(player2 , player1, 5, false);
             }
        });
    }
}
