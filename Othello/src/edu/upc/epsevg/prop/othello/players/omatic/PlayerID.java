package edu.upc.epsevg.prop.othello.players.omatic;


import edu.upc.epsevg.prop.othello.CellType;
import edu.upc.epsevg.prop.othello.GameStatus;
import edu.upc.epsevg.prop.othello.IAuto;
import edu.upc.epsevg.prop.othello.IPlayer;
import edu.upc.epsevg.prop.othello.Move;
import edu.upc.epsevg.prop.othello.SearchType;
import edu.upc.epsevg.prop.othello.players.omatic.Heuristica;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Random;

/**
 * Jugador aleatori
 * @author bernat
 */
public class PlayerID implements IPlayer, IAuto {

    private String name;
    private GameStatus s;
    private CellType jugador;
    private final int MAX = 1000000;
    private int nodesExplorats = 0;
    private boolean timeoutFlag = false;
    private Heuristica h;
    public PlayerID() {
        this.name = "OMatic(IDS)";
    }

    @Override
    public void timeout() {
        timeoutFlag = true;
    }

    /**
     * Ens avisa que hem de parar la cerca en curs perquè s'ha exhaurit el temps
     * de joc.
     */
    @Override
    public String getName() {
        return this.name;
    }
    
    @Override
    public Move move(GameStatus s) {
        timeoutFlag = false;
        this.jugador = s.getCurrentPlayer();
        h = new Heuristica(jugador);

        return IDS(s);
    }
    
    public Move IDS(GameStatus s){
        Move bestMove = null;
        int prof = 1;
        double startTime = System.currentTimeMillis();
        
        while(!timeoutFlag && prof < s.getEmptyCellsCount()+1){
            System.out.println("** PROFUNDITAT " + prof + " **");
            nodesExplorats = 0;
            Move move = minMax(s, prof);
            if(!timeoutFlag)
                bestMove = move;
            prof++;
        }
        double endTime = System.currentTimeMillis();
        double time = (endTime - startTime)/1000.0;
        System.out.println(time);

        return bestMove;
    }

    public Move minMax(GameStatus s, int profunditat){
        if(timeoutFlag)     //No ens importa el valor retornat. Només sortir lo més rapid del bucle
            return null;
        nodesExplorats = 0;
        Integer valor = -MAX-1;                     // valor d'heuristica ha de començar el mes petit possible per a poder superarla facil
        int alfa = -MAX;
        int beta = MAX;
        ArrayList<Point> moves =  s.getMoves();     // Llista de moviments
        Point moviment = null;                      // Moviment que realitzarem;
        if(moves.isEmpty()){
            // no podem moure, el moviment (de tipus Point) es passa null.
            return new Move(null, 0L, 0,  SearchType.MINIMAX); 
        } else {
            for (int i = 0; i < moves.size(); ++i){
               //tem.out.println(" " + (moves.size()-i) + " movements left...");
                GameStatus aux = new GameStatus(s);
                aux.movePiece(moves.get(i)); //Cal fer una tirada auxiliar cada cop
                int min = minValor(aux, alfa, beta, profunditat-1);
                if (valor <= min){
                    moviment = moves.get(i);
                    valor = min;
                }
                if (beta < valor){
                    break;
                }
                alfa = Math.max(valor,alfa);

            }     
        } 
        //TODO:
        //Minimax: que retorni el moviment be (cal implementar saber quants nodes s'ha explorat i l'alçada).
       // System.out.println("Valor retornat: " + valor);   
        return new Move(moviment, nodesExplorats, profunditat, SearchType.MINIMAX_IDS);
    }
    
    /**
    * Funcio de suport per l'algoritme minmax creat.
    *
    * @param s tauler sobre el qual fer el moviment
    * @param alfa valor de alfa per a la poda
    * @param beta valor de beta per a la poda.
    * @param profunditat profunditat del arbre de jugades.
    */
    public int maxValor(GameStatus s, int alfa, int beta, int profunditat){
        if(timeoutFlag)     //No ens importa el valor retornat. Només sortir lo més rapid del bucle
            return 0;
        nodesExplorats++;
        if(s.checkGameOver()){
            if(s.getScore(jugador) > s.getScore(CellType.opposite(jugador)))
                return MAX;
            else 
                //Si score(jugador) < score(oposat) o son iguals, considerem que hem perdut.
                return -MAX;
        }
            
        if(profunditat > 0){
            Integer valor = -MAX-1;
            ArrayList<Point> moves =  s.getMoves();
            for (int i = 0; i < moves.size(); ++i){
                GameStatus aux = new GameStatus(s);
                aux.movePiece(moves.get(i)); //Cal fer una tirada auxiliar cada cop
                valor = Math.max(valor, minValor(aux, alfa, beta, profunditat-1));
                if (beta < valor){
                    return valor;
                }
                alfa = Math.max(valor,alfa);
            }
            return valor;
        }
        else{
            if(!timeoutFlag)
                return h.getHeuristica(s);
            else
                return 0;
        }
        
    }
    /**
    * Funcio de suport per l'algoritme minmax creat.
    *
    * @param s tauler sobre el qual fer el moviment
    * @param alfa valor de alfa per a la poda
    * @param beta valor de beta per a la poda.
    * @param profunditat profunditat del arbre de jugades.
    */
    public int minValor(GameStatus s, int alfa, int beta, int profunditat){
        if(timeoutFlag)     //No ens importa el valor retornat. Només sortir lo més rapid del bucle
            return 0;
        nodesExplorats++;
        if(s.checkGameOver()){
            if(s.getScore(jugador) > s.getScore(CellType.opposite(jugador)))
                return MAX;
            else 
                //Si score(jugador) < score(oposat) o son iguals, considerem que hem perdut.
                return -MAX;
        }
        if(profunditat > 0){
            Integer valor = MAX-1;
            ArrayList<Point> moves =  s.getMoves();
            for (int i = 0; i < moves.size(); ++i){
                GameStatus aux = new GameStatus(s);
                aux.movePiece(moves.get(i)); //Cal fer una tirada auxiliar cada cop
                valor = Math.min(valor, maxValor(aux, alfa, beta, profunditat-1));
                if (valor < alfa){
                    return valor; 
                }
                beta = Math.min(valor,beta);
            }
            return valor;
        }
        else{   //Si es fulla
            if(!timeoutFlag)
                return h.getHeuristica(s);
            else
                return 0;
        }
    }   
}