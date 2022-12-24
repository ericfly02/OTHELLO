package edu.upc.epsevg.prop.othello.players.omatic;

import edu.upc.epsevg.prop.othello.CellType;
import edu.upc.epsevg.prop.othello.GameStatus;

public class Heuristica {
    private CellType jugador;
    private int turns;

    private static final int POND_CORNERS = 100;
    private static final int POND_EDGES = 20;
    private static final int POND_SEDGES = 60;
    private static final double POND_MOBILITAT = 2;
    private static final double POND_PARITAT = 1;
    private static final int TURN_TRESHOLD = 20;


    public Heuristica(CellType jugador){
        this.jugador = jugador;
        turns = 0;
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
               
        return (int)(POND_CORNERS*corners);
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
        return (int)(POND_PARITAT*paritat);
    }

    public boolean checkEdgeEstable(int fil, int col, GameStatus s) {
        // Pre: fil col representa un edge del tauler
        // El Edge (fil, col) es estable (te una sequencia de fitxes del mateix color com a minim fins a un corner)
        CellType color = s.getPos(fil, col);
        int i;
        if(fil == 0 || fil == 7){
            //Comprova si hi ha una linea de fitxes en direccio horitzontal fins al corner.
            for(i = fil+1; i < s.getSize(); i++){
                if(s.getPos(fil, i) != color)
                    break;
                else if(i == s.getSize()-1 && s.getPos(fil, i) == color)
                    return true;
            }
            for(i = fil-1; i >= 0; i--){
                if(s.getPos(fil, i) != color)
                    break;
                else if(i == 0 && s.getPos(fil, i) == color)
                    return true;
            }      
        }
        if(col == 0 || col == 7){
            //Comprova si hi ha una linea de fitxes en direccio vertical fins al corner.
            for(i = col+1; i < s.getSize(); i++){
                if(s.getPos(i, col) != color)
                    break;
                else if(i == s.getSize()-1 && s.getPos(i, col) == color)
                    return true;
            }
            for(i = col-1; i >= 0; i--){
                if(s.getPos(i, col) != color)
                    break;
                else if(i == 0 && s.getPos(i, col) == color)
                    return true;
            }
        }
        return false;
}

    /**
    * Algorisme dissenyat de minmax amb poda alfa-beta. Retorna la posició on es millor tirar. Per aquesta heuristica tindrem en compte els seguents factors:  La mobilitat, l'estabilitat, les cantonades i la paritat de peces.
    *
    * @param t tauler sobre el qual fer el moviment
    * @param profunditat profunditat del arbre de jugades.
    */
    public int hEdges(GameStatus s){
        // Check if the piece is stable in the horizontal direction
        int edges = 0;  // Numero de edges
        int sEdges = 0; // Numero de edges estables
        for(int i = 0; i < s.getSize(); i++){
            for(int j = 0; j < s.getSize(); j++){
                if (((i == 0 || i == 7) && (j > 0 && j < 7)) || ((j == 0 || j == 7) && (i > 0 && i < 7))) { 
                    if(s.getPos(i,j) == jugador){
                        if(checkEdgeEstable(i, j, s)){
                            sEdges++;
                        }
                        edges++; 
                    }
                    else if(s.getPos(i,j) == CellType.opposite(jugador)){
                        if(checkEdgeEstable(i, j, s)){
                            sEdges--;
                        }
                        edges--;
                    }
                        
                }
            }
        }
        //System.out.println("Edges Normals: "+edges);
        //System.out.println("Edges Estables: "+sEdges);
        return POND_EDGES*edges+POND_SEDGES*sEdges;
    }

    /**
    * Algorisme dissenyat de minmax amb poda alfa-beta. Retorna la posició on es millor tirar. Per aquesta heuristica tindrem en compte els seguents factors:  La mobilitat, l'estabilitat, les cantonades i la paritat de peces.
    *
    * @param t tauler sobre el qual fer el moviment
    * @param profunditat profunditat del arbre de jugades.
    */
    public int hMobilitat(GameStatus s){
        return (int)(POND_MOBILITAT *s.getMoves().size());
    }
    
    /**
    * Algorisme dissenyat de minmax amb poda alfa-beta. Retorna la posició on es millor tirar. Per aquesta heuristica tindrem en compte els seguents factors:  La mobilitat, l'estabilitat, les cantonades i la paritat de peces.
    *
    * @param t tauler sobre el qual fer el moviment
    * @param profunditat profunditat del arbre de jugades.
    */
    public int getHeuristica(GameStatus s){
        return hCorners(s)+hEdges(s)+hParitat(s)+hMobilitat(s);
    }
    
}
