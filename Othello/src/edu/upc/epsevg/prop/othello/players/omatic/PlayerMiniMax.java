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
    private Random rand = new Random();

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
        if(s.getPos(0, 0) == jugador) corners++;
        else if(s.getPos(0, 0) == CellType.opposite(jugador)) corners--;
        
        if(s.getPos(0, 7) == jugador) corners++;
        else if(s.getPos(0, 7) == CellType.opposite(jugador)) corners--;
        
        if(s.getPos(7, 0) == jugador) corners++;
        else if(s.getPos(7, 0) == CellType.opposite(jugador)) corners--;
        
        if(s.getPos(7, 7) == jugador) corners++;
        else if(s.getPos(7, 7) == CellType.opposite(jugador)) corners--;
               
        return (int)(50*corners);//+(-12.5*adjacents));
    }

    /**
    * Algorisme dissenyat de minmax amb poda alfa-beta. Retorna la posició on es millor tirar. Per aquesta heuristica tindrem en compte els seguents factors:  La mobilitat, l'estabilitat, les cantonades i la paritat de peces.
    *
    * @param t tauler sobre el qual fer el moviment
    * @param profunditat profunditat del arbre de jugades.
    */
    public int hParitat(GameStatus s){
        // retornem la diferencia de peçes de l'oponent i les nostres
        int paritat = s.getScore(jugador) - s.getScore(CellType.opposite(jugador));
        //System.out.println("Paritat: "+ paritat);
        return (int)(0.5*paritat);
    }

    /**
     * @param s
     * @return
     */
    public int hPeces(GameStatus s){
        int V[][] =  {
            {20, -3, 11, 8, 8, 11, -3, 20},
            {-3, -7, -4, 1, 1, -4, -7, -3},
            {11, -4, 2, 2, 2, 2, -4, 11},
            {8, 1, 2, -3, -3, 2, 1, 8},
            {8, 1, 2, -3, -3, 2, 1, 8},
            {11, -4, 2, 2, 2, 2, -4, 11},
            {-3, -7, -4, 1, 1, -4, -7, -3},
            {20, -3, 11, 8, 8, 11, -3, 20}
        };
        int pond = 0;
        for(int i = 0; i < s.getSize(); i++){
            for(int j = 0; j < s.getSize(); j++){
                if(s.getPos(i,j) == jugador){
                    pond += V[i][j];
                }
                else if(s.getPos(i,j) == CellType.opposite(jugador)){
                    pond -= V[i][j];
                }
            }
        }

        return pond;
    }

    /**
    * Algorisme dissenyat de minmax amb poda alfa-beta. Retorna la posició on es millor tirar. Per aquesta heuristica tindrem en compte els seguents factors:  La mobilitat, l'estabilitat, les cantonades i la paritat de peces.
    *
    * @param t tauler sobre el qual fer el moviment
    * @param profunditat profunditat del arbre de jugades.
    */
    public int hEdges(GameStatus s){
        // Check if the piece is stable in the horizontal direction
        int edges = 0;
        for(int i = 0; i < s.getSize(); i++){
            for(int j = 0; j < s.getSize(); j++){
                if(s.getPos(i,j) == jugador){
                    if (((i == 0 || i == 7) && (j > 0 && j < 7)) || ((j == 0 || j == 7) && (i > 0 && i < 7))) { 
                       edges++; 
                    }
                }
            }
        }
        
        
        //System.out.println("Estabilitat: "+estabilitat);
        return 10*edges;
    }


    /**
    * Algorisme dissenyat de minmax amb poda alfa-beta. Retorna la posició on es millor tirar. Per aquesta heuristica tindrem en compte els seguents factors:  La mobilitat, l'estabilitat, les cantonades i la paritat de peces.
    *
    * @param t tauler sobre el qual fer el moviment
    * @param profunditat profunditat del arbre de jugades.
    */
    public int hMobilitat(GameStatus s){
        return (int)(0.1*s.getMoves().size());
    }
    
    /**
    * Algorisme dissenyat de minmax amb poda alfa-beta. Retorna la posició on es millor tirar. Per aquesta heuristica tindrem en compte els seguents factors:  La mobilitat, l'estabilitat, les cantonades i la paritat de peces.
    *
    * @param t tauler sobre el qual fer el moviment
    * @param profunditat profunditat del arbre de jugades.
    */
    public int getHeuristica(GameStatus s){
        int c = hCorners(s);
        //int e = hEstabilitat(s);
        int p = hParitat(s);
        int m = hMobilitat(s);
        int e = hEdges(s);
        int tot = c+p+m+e;
        
        return tot;//hCorners(s)+hEstabilitat(s)+hParitat(s)+hMobilitat(s);
    }


    @Override
    public Move move(GameStatus s) {
        this.jugador = s.getCurrentPlayer();
        return minMax(s);
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
                System.out.println(" " + (moves.size()-i) + " movements left...");
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
        System.out.println("Valor retornat: " + valor);   
        return new Move(moviment, nodesExplorats, profunditat, SearchType.MINIMAX);
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
            //nodesExplorats++;
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
                //de vegades retorna MAX-1 perque la funcio maxValor retorna MAX, per tant MAX-1 < MAX.
                valor = Math.min(valor, maxValor(aux, alfa, beta, profunditat-1));
                if (valor < alfa){
                    return valor; 
                }
                beta = Math.min(valor,beta);
            }
            return valor;
        }
        else{   //Si es fulla
            return getHeuristica(s);
        }
    }
}
/*
if(info.profunditatPendent >= node.profunditatPendent){
    if(info.type == EXACTE){
        return info.heuristica;
    }
    else if (info.type == PODA ALPHA) {
        alpha = max(alpha, info.heuristica);
    }
    else{
        beta = min(beta, info.heuristica);
    }
}
*/