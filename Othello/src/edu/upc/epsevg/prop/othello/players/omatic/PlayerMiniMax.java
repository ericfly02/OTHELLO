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
 * @author icorn1, eric_gonsalez
 */
public class PlayerMiniMax implements IPlayer, IAuto {

    private String name;
    private int profunditat;
    private long nodesExplorats;
    private CellType jugador;
    final private int MAX = 1000000;

    public PlayerMiniMax(int profunditat) {
        this.profunditat = profunditat;
        this.name = "OMatic(" + profunditat + ")";
    }

    @Override
    public void timeout() {
        // Nothing to do! She dont give a fo - oohhh
    }

    /**
     * Ens avisa que hem de parar la cerca en curs perquè s'ha exhaurit el temps
     * de joc.
     */
    @Override
    public String getName() {
        return this.name;
    }

    /**
    * Algorisme dissenyat de minmax amb poda alfa-beta. Retorna la posició on es millor tirar. Per aquesta heuristica tindrem en compte els seguents factors:  La mobilitat, l'estabilitat, les cantonades i la paritat de peces.
    *
    * @param t tauler sobre el qual fer el moviment
    * @param profunditat profunditat del arbre de jugades.
    */
    public int hCorners(GameStatus s){
        int corners = 0;
        if(( s.getPos(0, 0) == jugador) || 
            (s.getPos(0, 7) == jugador) || 
            (s.getPos(7, 0) == jugador) || 
            (s.getPos(7, 7) == jugador)){
                corners++;
        }
        else if((s.getPos(0, 0) == CellType.opposite(jugador)) || 
                (s.getPos(0, 7) == CellType.opposite(jugador)) || 
                (s.getPos(7, 0) == CellType.opposite(jugador)) || 
                (s.getPos(7, 7) == CellType.opposite(jugador))){
                    corners--;
                }
               
        return 25*corners;
    }

    /**
    * Algorisme dissenyat de minmax amb poda alfa-beta. Retorna la posició on es millor tirar. Per aquesta heuristica tindrem en compte els seguents factors:  La mobilitat, l'estabilitat, les cantonades i la paritat de peces.
    *
    * @param t tauler sobre el qual fer el moviment
    * @param profunditat profunditat del arbre de jugades.
    */
    public int hParitat(GameStatus s){
        int paritat = 0;
        int blanc = 0;
        int negre = 0;
        for(int i = 0; i < 8; i++){
            for(int j = 0; j < 8; j++){
                if(s.getPos(i, j) == jugador) blanc++;
                if(s.getPos(i, j) == CellType.opposite(jugador)) negre++;
            }
        }
        paritat = blanc - negre;
        return paritat;    
    }

    /**
    * Algorisme dissenyat de minmax amb poda alfa-beta. Retorna la posició on es millor tirar. Per aquesta heuristica tindrem en compte els seguents factors:  La mobilitat, l'estabilitat, les cantonades i la paritat de peces.
    *
    * @param t tauler sobre el qual fer el moviment
    * @param profunditat profunditat del arbre de jugades.
    */
    public int hEstabilitat(GameStatus s){
        int estabilitat = 0;
        int blanc = 0;
        int negre = 0;
        for(int i = 0; i < 8; i++){
            for(int j = 0; j < 8; j++){
                if(s.getPos(i, j) == jugador) blanc++;
                if(s.getPos(i, j) == CellType.opposite(jugador)) negre++;
            }
        }
        estabilitat = blanc - negre;
        return estabilitat;   
    }

    /**
    * Algorisme dissenyat de minmax amb poda alfa-beta. Retorna la posició on es millor tirar. Per aquesta heuristica tindrem en compte els seguents factors:  La mobilitat, l'estabilitat, les cantonades i la paritat de peces.
    *
    * @param t tauler sobre el qual fer el moviment
    * @param profunditat profunditat del arbre de jugades.
    */
    public int hMobilitat(GameStatus s){
        int mobilitat = 0;
        int blanc = 0;
        int negre = 0;
        for(int i = 0; i < 8; i++){
            for(int j = 0; j < 8; j++){
                if(s.getPos(i, j) == CellType.PLAYER1) blanc++;
                if(s.getPos(i, j) == CellType.PLAYER2) negre++;
            }
        }
        mobilitat = blanc - negre;
        return mobilitat;
    }
    
    @Override
    public Move move(GameStatus s) {
        this.jugador = s.getCurrentPlayer();
        return minMax(s);
    }
    
    /**
    * Algorisme dissenyat de minmax amb poda alfa-beta. Retorna la posició on es millor tirar. Per aquesta heuristica tindrem en compte els seguents factors:  La mobilitat, l'estabilitat, les cantonades i la paritat de peces.
    *
    * @param t tauler sobre el qual fer el moviment
    * @param profunditat profunditat del arbre de jugades.
    */
    public int getHeuristica(GameStatus s){
        return hCorners(s);//+s.getScore(jugador);
    }

    

    public Move minMax(GameStatus s){
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
                GameStatus aux = new GameStatus(s);
                aux.movePiece(moves.get(i)); //Cal fer una tirada auxiliar cada cop
                int min = minValor(aux, alfa, beta, profunditat-1);
                if (valor < min){
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
        return new Move(moviment, nodesExplorats, 8, SearchType.MINIMAX);
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
        if(s.checkGameOver())
            return -MAX;
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
            nodesExplorats++;
            return getHeuristica(s);
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
        if(s.checkGameOver())   
            return MAX;
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
            nodesExplorats++;
            return getHeuristica(s);
        }
    }
}
