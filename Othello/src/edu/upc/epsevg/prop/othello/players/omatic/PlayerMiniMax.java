package edu.upc.epsevg.prop.othello.players.omatic;


import edu.upc.epsevg.prop.othello.CellType;
import edu.upc.epsevg.prop.othello.GameStatus;
import edu.upc.epsevg.prop.othello.IAuto;
import edu.upc.epsevg.prop.othello.IPlayer;
import edu.upc.epsevg.prop.othello.Move;
import edu.upc.epsevg.prop.othello.SearchType;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Random;

/**
 * Jugador aleatori
 * @author bernat
 */
public class PlayerMiniMax implements IPlayer, IAuto {

    private String name;
    private GameStatus s;
    private int profunditat;
    final private int MAX = 1000000;

    public PlayerMiniMax(int profunditat) {
        this.profunditat = profunditat;
    }

    @Override
    public void timeout() {
        // Nothing to do! She dont give a fo - oohhh
    }

    /**
     * Decideix el moviment del jugador donat un tauler i un color de peça que
     * ha de posar.
     *
     * @param s Tauler i estat actual de joc.
     * @return el moviment que fa el jugador.
     */
    @Override
    public Move move(GameStatus s) {

        ArrayList<Point> moves =  s.getMoves();
        if(moves.isEmpty())
        {
            // no podem moure, el moviment (de tipus Point) es passa null.
            return new Move(null, 0L,0,  SearchType.RANDOM); 
        } else {
            Random rand = new Random();
            int q = rand.nextInt(moves.size());
            return new Move( moves.get(q), 0L, 0, SearchType.RANDOM);         
        }
    }

    /**
     * Ens avisa que hem de parar la cerca en curs perquè s'ha exhaurit el temps
     * de joc.
     */
    @Override
    public String getName() {
        return "OMatic(" + profunditat + ")";
    }
    
    /**
    * Algorisme dissenyat de minmax amb poda alfa-beta. Retorna la columna on es millor tirar.
    *
    * @param t tauler sobre el qual fer el moviment
    * @param profunditat profunditat del arbre de jugades.
    */
    public int getHeuristica(){
        
    }
    public Move minMax(GameStatus s, int profunditat){
        int col = 0;
        Integer valor = -MAX-1;
        int alfa = -MAX;
        int beta = MAX;
        ArrayList<Point> moves =  s.getMoves();
        if(moves.isEmpty())
        {
            // no podem moure, el moviment (de tipus Point) es passa null.
            return new Move(null, 0L,0,  SearchType.MINIMAX); 
        } else {
               for (int i = 0; i < moves.size(); ++i){
                   moves.
                   GameStatus aux = new GameStatus(s);
                   aux.afegeix(i,color); //Cal fer un tauler auxiliar cada cop
                   int min = minValor(aux, i, alfa, beta, profunditat-1);
                   if (valor < min){
                       col = i;
                       valor = min;
                   }
                   if (beta < valor){
                       return col;
                   }
                   alfa = Math.max(valor,alfa);

            }     
        }
        
        return col;
    }
    
    /**
    * Funcio de suport per l'algoritme minmax creat.
    *
    * @param t tauler sobre el qual fer el moviment
    * @param col columna sobre la qual s'ha fet l'ultima jugada.
    * @param alfa valor de alfa per a la poda
    * @param beta valor de beta per a la poda.
    * @param profunditat profunditat del arbre de jugades.
    */
    public int maxValor(Tauler t, int col, int alfa, int beta, int profunditat){
        if(t.solucio(col, -color))
            return -MAX;
        if(profunditat > 0){
            Integer valor = -MAX-1;
            for (int i = 0; i < t.getMida(); ++i){
                if(t.movpossible(i)){
                    Tauler aux = new Tauler(t);
                    aux.afegeix(i,color);
                    valor = Math.max(valor, minValor(aux,i, alfa, beta, profunditat-1));
                    if (beta < valor){
                        return valor;
                    }
                    alfa = Math.max(valor,alfa);
                }
            }
            return valor;
        }else{
            return getHeuristica(t);
        }
        
    }
    /**
    * Funcio de suport per l'algoritme minmax creat.
    *
    * @param t tauler sobre el qual fer el moviment
    * @param col columna sobre la qual s'ha fet l'ultima jugada.
    * @param alfa valor de alfa per a la poda
    * @param beta valor de beta per a la poda.
    * @param profunditat profunditat del arbre de jugades.
    */
    public int minValor(Tauler t, int col, int alfa, int beta, int profunditat){
        if(t.solucio(col, color))   
            return MAX;
        if(profunditat > 0){
            Integer valor = MAX-1;
            for (int i = 0; i < t.getMida(); ++i){
                if(t.movpossible(i)){
                    Tauler aux = new Tauler(t);
                    aux.afegeix(i,-color);
                    valor = Math.min(valor, maxValor(aux,i, alfa, beta, profunditat-1));
                    if (valor < alfa){
                        return valor; 
                    }
                    beta = Math.min(valor,beta);
                }
            }
            return valor;
        }
        else{
            return getHeuristica(t);
        }
    }
}
