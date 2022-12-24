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

**MiniMax →** El mètode minMax és una implementació de l'algorisme Minimax, que és una tècnica utilitzada en jocs de dos jugadors per a determinar el millor moviment possible en cada torn. L'algorisme funciona avaluant totes les possibles jugades en un torn donat i seleccionant la jugada que maximitza la puntuació del jugador en el seu pròxim torn i minimitza la puntuació de l'oponent.

**Iterative Deeping (ID) →** La tècnica IDS consisteix a realitzar una cerca en profunditat de manera iterativa, augmentant la profunditat en cada iteració fins que s'esgoti el temps de joc o es trobi una solució. En aquest cas, el mètode IDS és l'encarregat de realitzar aquesta cerca. Dins del mètode, s'utilitza un bucle per a anar augmentant la profunditat i cridant al mètode minMax amb cada profunditat. Si el temps de joc no s'esgota, el millor moviment trobat en cada iteració s'emmagatzema en la variable bestMove.
