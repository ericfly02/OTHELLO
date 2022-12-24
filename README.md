# OTHELLO (PROP Q1 2022-2023) Eric Gonzalez - Ixent Cornella

## Introducció

El joc de taula Othello és un dels jocs d'estratègia més coneguts juntament amb els escacs o el Go. Es juga en un tauler de 8x8 caselles i cada jugador té un conjunt de fitxes del color propi, que poden ser negres o blanques. L'objectiu del joc és col·locar les fitxes de manera que envoltar i capturar les fitxes de l'oponent, per a finalment comptar amb més fitxes del color propi en el tauler al final del joc.

En aquest treball, es presenta la creació d'un jugador de Othello basat en l'algorisme minimax amb poda alfa-beta. L'algorisme minimax és una tècnica utilitzada en jocs d'estratègia per a trobar la millor opció de joc en un determinat moment. L'algorisme avalua totes les possibles jugades que un jugador pot realitzar, així com les respostes de l'oponent, i selecciona la jugada que maximitza la puntuació del jugador, minimitzant al mateix temps la puntuació de l'oponent. La poda alfa-beta és una variant de l'algorisme minimax que s'utilitza per a accelerar el procés de cerca de la millor jugada, eliminant de l'avaluació aquelles branques de l'arbre de joc que no són prometedores.

En aquest treball, es desenvoluparan dues versions del jugador de Othello basat en l'algorisme minimax amb poda alfa-beta:

PlayerMinimax: aquesta versió del jugador estarà parametritzada amb el nombre de nivells que s'explora mitjançant l'algorisme minimax. Això significa que el jugador podrà avaluar un nombre determinat de jugades i respostes de l'oponent, i seleccionarà la jugada que maximitzi la seva puntuació.

PlayerID: aquesta versió del jugador utilitzarà el mecanisme de timeout previst per la interfície gràfica, cosa que significa que el jugador tindrà un temps límit per a prendre una decisió. Per a poder tallar en "qualsevol" moment la cerca, s'implementarà l'algorisme Iterative-*Deepening, que consisteix a anar augmentant progressivament el nivell de profunditat de la cerca fins que s'aconsegueixi el temps límit.

L'heurística és la part clau d'aquest treball, ja que determinarà l'eficiència i la capacitat del jugador per a prendre decisions estratègiques. Per tant, és fonamental pensar acuradament què tenir en compte i com calcular-la de forma eficient.

<br />

## Heuristica
Abans d’analitzar en detall quina heurística hem emprat per aquesta pràctica, farem una petita introducció sobre que es heurística, i com es pot aplicar en el jo de taula “Othello”. 

Una heurística és un conjunt de regles o pautes que s'utilitzen per a prendre decisions en situacions en les quals no és possible trobar una solució òptima de manera ràpida o senzilla. En el joc de Othello, una heurística pot ajudar un jugador a avaluar la situació del tauler i a prendre decisions sobre el seu pròxim moviment de manera més ràpida i eficient.

A més a més, es pot utilitzar com una manera de simplificar el procés de presa de decisions, ja que permet al jugador considerar només un conjunt limitat de factors en lloc d'haver d'avaluar totes les possibles opcions de manera exhaustiva. Això pot ser especialment útil en situacions de temps limitat, com pot ser el joc “Othello” en el qual cada jugador té un límit de temps per a fer el seu moviment.

Per altra banda, l'heurística també pot ser útil per a millorar l'efectivitat d'un jugador. Ja que en considerar només un conjunt limitat de factors, un jugador pot enfocar-se en el que és més important en cada situació i prendre decisions més estratègiques en lloc de simplement reaccionar a cada moviment de l'oponent.

Un cop feta aquesta petita introducció sobre el funcionament de l’heurística, ja podem procedir a analitzar quina hem utilitzat per aquesta pràctica. 

En primer lloc, hi han diferents factors que es poden combinar per a obtenir una heurística altament efecient, com poden ser la paritat, la mobilitat, els corners, o la estabilitat. Cal tenir en compte que aquests factors han de ser considerats com una combinació de tots ells, ja que cap dels factors serà el mes adeqüat per totes les situacions donades

<br />

**PARITAT →** es refereix a l'equilibri de peces blanques i negres en el tauler. Un jugador amb més peces del seu color en el tauler té avantatge, ja que disposa de més opcions per a realitzar moviments i voltejar les peces del seu oponent.. No obstant això, no sempre és necessari prioritzar la paritat per sobre de tota la resta. Per exemple, si un jugador té l'opció de fer un moviment que augmenti la seva paritat però també li dona a l'oponent l'oportunitat de fer un moviment que augmenti la seva pròpia paritat, pot ser més beneficiós per al jugador fer un moviment diferent.

Pel que fa a la nostra implementació, la funció `hParitat()` retorna un enter que representa la puntuació de l'estat actual del joc. La puntuació es calcula tenint en compte la paritat de les fitxes del jugador i de l'oponent en el tauler, i realitzant la resta entre el nombre de pces del jugador, menys el nombre de peces de l’oponent. Si el jugador actual té més fitxes que l'oponent, es retornarà un valor positiu, per altra banda, si l'oponent té més fitxes que el jugador, es retornarà un valor negatiu. Si tots dos jugadors tenen la mateixa quantitat de fitxes, es retorna 0. Cal afegir que el resultat obtingut es multiplicarà per un pes baix com ara pot ser 0.5, ja que la paritat no té tanta importància com poden ser els corners

Per exemple, si el jugador actual té 12 fitxes i l'oponent té 10 fitxes, la funció `hParitat()` retornaria una puntuacó de 1 (ja que es faria 12 - 10 i despres es multiplicaria per 0.5). Si tots dos jugadors tinguessin la mateixa quantitat de fitxes, la funció retornaria 0

<img width="823" alt="Othello - Eric i Ixent" src="https://user-images.githubusercontent.com/20001491/209415568-d3ce79ed-407b-42a3-a8c0-d7771baf6eaf.png">


**ESTABILITAT -->** es refereix a la capacitat d'una peça per a romandre en el tauler. Les peces que estan envoltades per peces amigues es consideren estables, mentre que les que estan envoltades per peces enemigues corren el risc de ser voltejades. Per altra banda, podem diferenciar tres tipus diferents de peces estables:
Estable → son peces que no poden ser voltejades en cap moment de la partida
Semi-estable → son aquelles peces que poden ser voltejades en algun moment futur de la partida, pero que no corren perill per al pròxim moviment
Inestable → Son aquelles peces que poden ser voltejades just en el següent moviment

Pel que fa a la nostra implementació, la funció `hEdges()` retorna un enter que representa la puntuació de l'estat actual del joc. La puntuació es calcula tenint en compte les fitxes del jugador i de l'oponent que es troben en les vores del tauler (les files i columnes 0 i 7). Si el jugador actual té alguna fitxa en algun de les vores, se li suma una puntuació de 1. Si l'oponent té alguna fitxa en algun de les vores, se li resta una puntuació de 1. Si cap dels dos jugadors té cap fitxa en les vores, es retorna 0.

Per a calcular la puntuació, la funció `hEdges()` utilitza el mètode `checkEdgeEstable()`, que rep com a paràmetres la fila i la columna d'una fitxa i l'estat actual del joc, i retorna true si la fitxa és estable (és a dir, si no pot ser voltada per l'oponent de cap de les maneres) o false en cas contrari.

Per altra banda el pes que se li dona al factor “edges”, es de 10 per a les peces que no son del tot estables (semi-estables o inestables), i 30 per a les peces que ja son estables i no poden ser voltades 

Per exemple, si el jugador actual té la fitxa (0,0) i (7,7) en les vores del tauler i l'oponent té la fitxa (0,7) en una vora, la funció `hEdges()` retornaria una puntuació de 20 (10 punts per tenir dues fitxes en les vores, menys 10 punts perquè l'oponent té una fitxa en una vora). Si cap dels dos jugadors tingués cap fitxa en les vores, la funció retornaria 0.

<img width="920" alt="Othello - Eric i Ixent (4)" src="https://user-images.githubusercontent.com/20001491/209415632-95e70ba0-c72b-4aa0-9d5b-5be3577932da.png">
<img width="920" alt="Othello - Eric i Ixent (3)" src="https://user-images.githubusercontent.com/20001491/209415637-079f41e6-7479-4e39-889e-8f764dd8276c.png">


**CORNERS →** Quan ens referim als corners, ens referim a les posicions: (0,0), (0,7), (7,0) i (7,7). Aquestes posicions són especials perquè una vegada que són capturades per un jugador, no poden ser voltejats per l'oponent. A més, permeten que un jugador construeixi peces al voltant d'ells, proporcionant estabilitat a les peces del jugador en aquesta zona del tauler. Hi ha una alta correlació entre el nombre de cantonades capturades per un jugador i la seva probabilitat de guanyar la partida. Encara que capturar la majoria de les cantonades no garanteix necessàriament la victòria, permet construir una major estabilitat en el tauler i per tant pot ser beneficiós per al jugador

Pel que fa a la nostra implementació, la funció `hCorners()` retorna un enter que representa la puntuació de l'estat actual del joc. La puntuació es calcula tenint en compte les cantonades del tauler (les caselles en les posicions (0,0), (0,7), (7,0) i (7,7)). Si el jugador actual té alguna d'aquestes caselles, se li suma una puntuació de 1. Si l'oponent té alguna d'aquestes caselles, se li resta una puntuació de 1. Si cap dels dos jugadors té cap d'aquestes caselles, es retorna 0. Per ultim es multiplica el resultat final per el seu pes corresponent que en el cas dels corners seria el més elevat (ja que te mes importancia) i es 50. 

Per exemple, si el jugador actual té les caselles (0,0) i (7,7) i l'oponent té la casella (0,7), la funció hCorners retornaria una puntuació de 50 (2 punts per tenir dues cantonades, menys 1 punt perquè l'oponent té una cantonada. i el resultat, multiplicat per 50). Si cap dels dos jugadors tingués cap cantonada, la funció retornaria 0.

<img width="823" alt="Othello - Eric i Ixent" src="https://user-images.githubusercontent.com/20001491/209415657-fa10a5ad-78dc-439f-a939-52ae40c2910c.png">


**MOBILITAT →** es refereix al nombre de moviments disponibles que té un jugador. Un jugador amb més mobilitat té més opcions per a fer moviments i voltejar les peces del seu oponent. No obstant això, també és important tenir en compte que donar a l'oponent massa opcions pot ser perjudicial per al jugador.

Pel que fa a la nostra implementació no hem afegit el concepte de mobilitat en la ultima versió ja que vam notar que el resultat obtingut no era gaire eficient. Pero tot i aixi, mostrarem el seu funcionamen. La funció `hMobilitat()` en primer lloc, obte el tamany de la llista de moviments, `s.getMoves().size()`. El resultat obtingut el multipliquem per el seu pes corresponent que es 0.1

<img width="920" alt="Othello - Eric i Ixent (5)" src="https://user-images.githubusercontent.com/20001491/209415685-8a8bb6ee-c695-4ced-b2ca-c0528c07193b.png">


<br />

## MiniMax i ID
Els algorismes Minimax i Iterative Deepening són tècniques utilitzades en la intel·ligència artificial per a prendre decisions en jocs de taula com Othello. Aquests algorismes s'utilitzen per a avaluar diferents opcions de joc i seleccionar la que ofereixi la major probabilitat de guanyar. Per a realitzar aquesta pràctica, hem utilitzat els dos tipus d’algorismes per a poder observar quin és el més eficient. Seguidament es realitzara una petita introducció sobre el funcionament de cada algorisme, com l’hem aplicat a la pràctica, i finalment, una petita comparativa entre ambdós algorsimes. 

**MiniMax →** L'algorisme Minimax s'utilitza principalment per a prendre decisions en jocs de taula on dos jugadors competeixen entre si. L'objectiu d'aquest algorisme és avaluar totes les possibles combinacions de moviments de tots dos jugadors i seleccionar el que ofereixi el major valor per al jugador que l'està utilitzant.

Per a entendre com funciona l'algorisme Minimax, és necessari entendre el concepte d'arbre de joc. Un arbre de joc és una representació gràfica de totes les possibles combinacions de moviments que poden realitzar-se en un joc. L'arbre de joc es construeix a partir de l'estat inicial del joc i s'estén a mesura que es realitzen moviments.

L'algorisme Minimax explora l'arbre de joc complet i avalua cadascun dels estats finals possibles. Cada estat final s'assigna un valor de guany o pèrdua depenent de si el jugador que l'utilitza ha guanyat o perdut. L'algorisme Minimax llavors selecciona el moviment que ofereixi el major valor per al jugador que l'està utilitzant, tenint en compte els valors de guany o pèrdua dels estats finals possibles.

Pel que es refereix a la implementació de l’algorisme MiniMax en la nostra pràctica, el que hem realitzat es un metode anomenat minMax(GameStatus s) . Aquest mètode reb com a paràmetre unicament el estat del joc actual. 

L'algorisme funciona de la següent manera:

    - Si s'aconsegueix la profunditat màxima o l'estat del joc és terminal, s'avalua l'estat del joc i es retorna el valor de l'heurística.
    - Si és el torn del jugador, es busca el valor màxim entre tots els possibles moviments. Per a cada moviment, s'actualitza el valor de alpha i es diu recursivamente al mètode minmax amb els paràmetres actualitzats.
    - Si és el torn de l'oponent, es busca el valor mínim entre tots els possibles moviments. Per a cada moviment, s'actualitza el valor de beta i es diu recursivamente al mètode minmax amb els paràmetres actualitzats.
    
<img width="920" alt="Othello - Eric i Ixent (2)" src="https://user-images.githubusercontent.com/20001491/209446247-8dc43169-b8d9-404f-8b6e-02cf7b27aaf9.png">


**Iterative Deeping (ID) →** L'algorisme Iterative Deepening és una variant de l'algorisme Minimax que s'utilitza per a buscar solucions en arbres de joc de gran profunditat. En lloc d'explorar tot l'arbre de joc d'una sola vegada, l'algorisme Iterative Deepening explora l'arbre de manera incremental, augmentant la profunditat de cerca en cada iteració. Això permet que l'algorisme trobi solucions més ràpidament en arbres de joc de gran grandària.

L'algorisme Iterative Deepening funciona de la següent manera:

    - S'estableix un límit de profunditat inicial per a la cerca.
    - Es realitza una cerca en profunditat fins a aconseguir el límit de profunditat establert.
    - Si es troba una solució, es retorna immediatament. Si no es troba una solució, s'augmenta el límit de profunditat i es torna a realitzar la cerca en profunditat.
    - Aquest procés es repeteix fins a trobar una solució o fins que s'esgoti el temps de cerca.

L'algorisme Iterative Deepening s'utilitza sovint en jocs de taula com Othello per a trobar solucions de manera més eficient en arbres de joc de gran grandària. A més, també s'ha utilitzat en altres problemes de presa de decisions on és necessari buscar solucions en arbres de gran grandària.

<img width="920" alt="Othello - Eric i Ixent (8)" src="https://user-images.githubusercontent.com/20001491/209446255-8afe6cef-98b0-465e-81fc-9d2aa8aaa220.png">

**Comparativa →**

<br />

## Grafiques de numero de nivells baixats



<br />

## Estrategies d'optimització utilitzades
Com en comentat en punts anteriors, per aquesta pràctica hem implementat tant l’algorisme MinMax com l’algorisme Iterative Deeping (ID). No obstant això, la complexitat d'aquests algorismes pot ser molt alta, especialment en jocs amb moltes opcions i una gran quantitat d'estats possibles. Per tant, és necessari utilitzar tècniques d'optimització per a fer que aquests algorismes siguin més eficients i ràpids. Algunes d'aquestes tècniques inclouen taules de transposició, zobrist hash i poda alfa beta.

**Poda alfa-beta →** Per a l’algorisme MinMax, hem utilitzat un tipus d’optimització anomenat “poda alfa-beta”. Aquesta tècnica, s’utilitza per a reduir el temps de cerca en descartar ràpidament branques de l'arbre de cerca que no són prometedores. Això s'aconsegueix establint dos valors, alfa i beta, que representen el valor màxim i mínim, respectivament, que s'han trobat fins al moment en la cerca.

Alpha representa el valor màxim que s'ha trobat fins al moment en la branca de l'arbre que està sent avaluada per l'agent MAX, mentre que beta representa el valor mínim que s'ha trobat fins al moment en la branca de l'arbre que està sent avaluada per l'agent MIN. A mesura que s'avança en la cerca, si es troba un valor major que alpha en la branca de l'agent MAX o un valor menor que beta en la branca de l'agent MIN, s'actualitza el valor de alpha o beta respectivament. Si en algun moment es troba que alpha és major o igual que beta, es talla la cerca en aquesta branca ja que se sap que el resultat final no serà millor que els valors trobats fins al moment.

En el codi que es mostra seguidament. Si la profunditat màxima és 0 o l'estat del joc és terminal (és a dir, no hi ha més moviments possibles), es retorna el valor de l'heurística. En cas contrari, s'inicialitzen les variables alpha i beta amb els valors rebuts com a paràmetres i es crea una llista de moviments possibles a partir de l'estat actual del joc.


A continuació, s'itera sobre la llista de moviments i es diu recursivamente a la funció `minMax()` per a cadascun d'ells, passant com a paràmetres l'estat del joc resultant de realitzar aquest moviment, la profunditat restant - 1 i els límits alpha i beta actualitzats en cada iteració. Si l'estat del joc és el torn del jugador Max, s'actualitza el valor de alpha amb el màxim entre alpha i el valor retornat per l'anomenada recursiva a `minMax()`. Si l'estat del joc és el torn del jugador Min, s'actualitza el valor de beta amb el mínim entre beta i el valor retornat per l'anomenada recursiva a `minMax()`.


També es pot veure en la funció `minMax()` què es realitza una comparació entre alpha i beta, i si alpha és major o igual que beta es talla la cerca i es retorna el valor de alpha si s'està avaluant una branca de l'agent MAX o el valor de beta si s'està avaluant una branca de l'agent MIN.

<img width="920" alt="Othello - Eric i Ixent (6)" src="https://user-images.githubusercontent.com/20001491/209446401-1781f131-b1d9-4dff-8d11-3cf9261d770d.png">

<img width="920" alt="Othello - Eric i Ixent (7)" src="https://user-images.githubusercontent.com/20001491/209446404-160c1598-7886-45a8-a0cb-529a9d0892c2.png">


**Poda Prob-Cut →** Un altre tipus d’optimització podria ser la poda Prob-Cut (no l’hem utilitzat en aquesta practica per falta de temps), la qual és una tècnica de poda utilitzada en l'algorisme MiniMax per a optimitzar la seva execució, y es similar a la poda alfa-beta que hem utilitzat en aquesta practica. Aquesta tècnica es basa en la probabilitat que el node actual sigui tallat per un node de major valor en l'arbre de cerca. Si la probabilitat que això ocorri és molt alta, llavors es pot tallar la cerca en aquest node per a estalviar temps de càlcul.

Per a implementar aquesta poda en el codi minmax, el que hauriem de fer, es agregar una condició en el mètode minmax() que verifiqui si la probabilitat que el node actual sigui tallat per un node de major valor és prou alta. Si és així, es pot retornar el valor d'aquest node sense haver d'explorar més profundament en l'arbre de cerca. Això es pot fer comparant el valor del node actual amb el valor d'un node tallat prèviament per un node de major valor, i si el valor del node actual és menor que el valor del node tallat, llavors es pot retornar el valor del node actual sense explorar més profundament en l'arbre de cerca.


**Zobrist Hashing i Taules de Transposició →** El mètode zobristhash s'utilitza per a calcular una "clau hash" de l'estat actual del tauler de joc. Aquesta clau hash s'utilitza per a emmagatzemar l'estat del tauler en una taula de transposicions i evitar haver de tornar a calcular el valor d'un estat del tauler que ja ha estat avaluat prèviament. Això permet una major eficiència en la cerca, ja que evita haver de tornar a explorar estats del joc que ja han estat avaluats.

La taula de transposició és una tècnica utilitzada per a emmagatzemar i recuperar ràpidament els resultats de subproblemes que s'hagin resolt prèviament. Això permet evitar haver de tornar a calcular subproblemes que ja s'hagin resolt anteriorment i, per tant, redueix la complexitat de l'algorisme.








