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



