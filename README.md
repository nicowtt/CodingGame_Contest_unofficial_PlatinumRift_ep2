# Coding Game un-official compétition "Platinum Rift ep2"
Platinum rift est une compétition de creation d'IA (Bot) mondiale d'une durée de 10 jours
ouverte à tous et à tout les languages de programmation.<br/>

Apprentissage:
- Recherche de chemins
- Multi-Agent
- Gestion de ressources

Dans ce puzzle, vous devez gérer un nombre important d'unités (multi-agent). Chaque unité devant être déplacée individuellement,<br/>
 une gestion des ressources et un ordonnancement des tâches optimisé sont nécessaires afin d'obtenir une certaine cohésion de groupe. <br/>
 Vous devrez également gérer le manque d'information dû au brouillard de guerre.

Cette compétition (non-officielle) à été organisée sur la base d'une ancienne compétition coding game qui s'est déroulée
en 2016.
Il y a donc deux classements:
- Le classement par rapport aux IA déja crées par la compétition de 2016.
- Le classement de cette compétition du 5 au 15 juin 2020.

### HISTOIRE
Ce jeu fait suite à «  Platinum Rift » dans lequel votre objectif était de retourner sur Terre pour miner et extraire un <br/>
maximum de Platine servant à augmenter la population de votre faction.

Le but de ce puzzle est de gérer les déplacements de vos unités à travers un brouillard de guerre. Votre programme devra <br/>
contrôler plusieurs centaines d'unités très rapidement. Trouvez une stratégie efficace pour envoyer vos unités vers les zones <br/>
pertinentes et soyez le premier à prendre le contrôle de la base adverse.

![alt text](https://github.com/nicowtt/CodingGame_Contest_unofficial_june2020_PlatinumRift_ep2/blob/master/Tof1.png)

![alt text](https://github.com/nicowtt/CodingGame_Contest_unofficial_june2020_PlatinumRift_ep2/blob/master/Tof2.png)

### Techniques utilisés pour ce projet:
- Language JAVA 8.
- Programation Orienté Objet (POO).

### Fonctions développé:

    * Mes 10 pods (base de depart) se scinde en fonction du nombre de case voisine pour maximiser l'exploration et donc
    la capture de platinum (ressources).
    * Si la base adverse est a moins de 8 zones -> attaque directe sur la base adverse avec 8 pods(2 en défense).
    * Mes pods ont la capacitées de se scinder quand ils sont en mode exploration.
    * Il y a toujours des pods dans ma base en défense. 
    * Ma base lache 1 pod par tour. (direction la base adverse)
    * Lorsqu'il y a 20 pods dans ma base, le groupe est lachés directement vers la base adverse.
    
### Algorithme
    Pour ce jeux, je ne connais pas les dimensions de la map, uniquement les liens possibles entre les zones:
    
    * Utilisation de l'algorithme de parcours en largeur BFS (Breadth First Search):
     permet le parcours d'un graphe ou d'un arbre de la manière suivante : on commence par explorer un nœud 
     source, puis ses successeurs, puis les successeurs non explorés des successeurs, etc. 
     L'algorithme de parcours en largeur permet de calculer les distances de tous les nœuds depuis 
     un nœud source dans un graphe non pondéré (orienté ou non orienté). 
     Il peut aussi servir à déterminer si un graphe non orienté est connexe.
     
![alt text](https://github.com/nicowtt/CodingGame_Contest_unofficial_june2020_PlatinumRift_ep2/blob/master/tof.png)
     
### Contrainte:
Response time first turn ≤ 1000 ms<br/>
Response time per turn ≤ 200 ms

### Les règles officielles:
https://www.codingame.com/ide/puzzle/platinum-rift-episode-2

### La page officielle de l'organisateur:
https://thibpat.now.sh/contest/unofficial-codingame-contest

### Resultat:
Officiel Coding game (2016):<br/>
1 721 participants dans le monde!<br/>
Résultat: <br/>
350 ème <br/>
-> 20/100 <br/>

Non-officiel -> cette compétition: <br/>
340 participants dans le monde.<br/>
mon résultat:<br/>
65 ème<br/>
![alt text](https://github.com/nicowtt/CodingGame_Contest_unofficial_june2020_PlatinumRift_ep2/blob/master/Tof3.png)

Voici mon évolution de classement durant cette compétition:
![alt text](https://github.com/nicowtt/CodingGame_Contest_unofficial_june2020_PlatinumRift_ep2/blob/master/Tof4.png)

### Dernière partie:
https://www.codingame.com/replay/472873911

### Impressions personnelles:
- Troisième participations à un concours d'IA de bot et c'est de plus en plus fun! <br/>
- J'ai l'impression de connaître mieux mon language JAVA et l'achitecture Objet. En tous cas je suis
plus à l'aise. <br/>
- Un peu pertubé par le fait que l'on connait uniquement les zones qui se touche et le nombre total de zone! <br/>
- Heureux d'avoir réussi à coder l'algorithme BFS pour la recherche de chemin le plus court! <br/>

### Conclusion:
1ère compétition (Ocean of code)-> résultat -> 40ème/100 <br/>
2ème compétition (Pacman /shiFouMi) -> résultat -> 30ème/100 <br/>
3ème compétition (Platinum rift 2) -> résultat -> 20ème/100 <br/>

C'est toujours difficile comme compétition mais le bonheur d'apprendre et la satisfaction quand j'y arrive est pour moi <br/>
trés agréable! <br/>
Cependant, Je ne suis pas sur d'arriver à améliorer mon score personnel à la prochaine compétition!! la compétition est rude!



