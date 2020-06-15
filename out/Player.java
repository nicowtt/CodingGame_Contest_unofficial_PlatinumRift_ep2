import java.util.List;
import java.util.*;
import java.util.stream.Collectors;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class Board {
    int zoneCount;
    int linkCount;
    List<Zone> zoneList;
    List<MoveObj> movePossiblity;
    int myBaseZoneId;
    int oppBAseZoneId;
    List<Integer> pathToOpp;

    // constructor
    public Board() {
    }

    public Board(int zoneCount, int linkCount, List<MoveObj> movePossiblity) {
        this.zoneCount = zoneCount;
        this.linkCount = linkCount;
        this.movePossiblity = movePossiblity;
    }

    // getters and setters
    public Integer getZoneCount() {
        return zoneCount;
    }

    public void setZoneCount(Integer zoneCount) {
        this.zoneCount = zoneCount;
    }

    public Integer getLinkCount() {
        return linkCount;
    }

    public void setLinkCount(Integer linkCount) {
        this.linkCount = linkCount;
    }

    public List<MoveObj> getMovePossiblity() {
        return movePossiblity;
    }

    public void setMovePossiblity(List<MoveObj> movePossiblity) {
        this.movePossiblity = movePossiblity;
    }

    public List<Zone> getZoneList() {
        return zoneList;
    }

    public void setZoneList(List<Zone> zoneList) {
        this.zoneList = zoneList;
    }

    public int getMyBaseZoneId() {
        return myBaseZoneId;
    }

    public void setMyBaseZoneId(int myBaseZoneId) {
        this.myBaseZoneId = myBaseZoneId;
    }

    public int getOppBAseZoneId() {
        return oppBAseZoneId;
    }

    public void setOppBAseZoneId(int oppBAseZoneId) {
        this.oppBAseZoneId = oppBAseZoneId;
    }

    public List<Integer> getPathToOpp() {
        return pathToOpp;
    }

    public void setPathToOpp(List<Integer> inputPathList) {
        this.pathToOpp = inputPathList;
    }

    // methods
    public void updateZone(Zone zoneToUpdate) {
        zoneList.add(zoneToUpdate);
    }
}

class Move {
    Utils utils = new Utils();
    private final int NOPATH = -1;


    /**
     * move 10 pods
     * choice zone with platinum first
     * @param board
     * @return
     */
    public String firstMoveIA13(Board board) {
        String move = "";

        // if opp is in range of 7 zones! -> attack with 10 pods
        if (board.getZoneList().get(board.getMyBaseZoneId()).getDistance().get(board.oppBAseZoneId) <= 8) {
//            System.err.println("pass direct attack!!");
            // attack with 10 drones
            int firstMovetoZone = utils.getFirstMoveToBFSPath(board.getMyBaseZoneId(), board.getOppBAseZoneId(), board); // first zone direction to oppBase

            Zone nextMoveZone = board.getZoneList().get(firstMovetoZone);
            nextMoveZone.setGoal(board.oppBAseZoneId);
            nextMoveZone.setVisited(true);
            nextMoveZone.setBlitzAttack(true);
            board.updateZone(nextMoveZone);
            return "8 " + board.getMyBaseZoneId() + " " + firstMovetoZone;
        } else {
            List<Integer> myBaseNeighbor = board.getZoneList().get(board.getMyBaseZoneId()).getNeighbor();
            if (myBaseNeighbor.size() < 6) {
                for (int i = 0; i < myBaseNeighbor.size(); i++) {
                    move += "2 " + board.getMyBaseZoneId() + " " + myBaseNeighbor.get(i) + " ";
                }
                Zone myBaseZone = board.getZoneList().get(board.getMyBaseZoneId());
                myBaseZone.setVisited(true);
                board.updateZone(myBaseZone);
                return move;
            } else {
                for (int i = 0; i < myBaseNeighbor.size(); i++) {
                    move += "1 " + board.getMyBaseZoneId() + " " + myBaseNeighbor.get(i) + " ";
                }
                Zone myBaseZone = board.getZoneList().get(board.getMyBaseZoneId());
                myBaseZone.setVisited(true);
                board.updateZone(myBaseZone);
                return move;
            }
        }
    }

    /**
     * I know path from myBase to oppBase
     * if pods are at 7 zone of ennemies try to go on opp base
     * @param board
     * @param myId
     * @return
     */
    public String moveIA3(Board board, int myId) {
        String move = "";
        // on my base, pods > 20 -> BLITZATTACK to oppBase !!!
        Set<Zone> podZones = utils.findPodZonesList(board, myId);
        List<Zone> podForAttack = utils.getGroupOfPodForAttackOppBase(podZones, myId, 20);
        if (!podForAttack.isEmpty()) {
            for (Zone zone: podForAttack ) {
                Zone zoneForAttack = board.getZoneList().get(zone.getzId()); // put goal and visited zone for attack
                zoneForAttack.setGoal(board.oppBAseZoneId);
                zoneForAttack.setVisited(true);
                board.updateZone(zoneForAttack);
            }
        }

        // for pod with goal -> following their path
        Set<Zone> zoneWithGoal = new HashSet<>();
        int nbrOfPodOnZone = 0;
        for (Zone zone : board.getZoneList() ) {
            if (zone.getGoal() != null) { zoneWithGoal.add(zone); }
        }

        for (Zone zone : zoneWithGoal ) {
            int from = zone.getzId();
            if (myId == 0) {
               nbrOfPodOnZone = zone.getPodsP0();
            } else {
                nbrOfPodOnZone = zone.getPodsP1();
            }
            int to = utils.getFirstMoveToBFSPath(from, zone.getGoal(), board);
            if (to == NOPATH) { to = board.getZoneList().get(zone.getzId()).getNeighbor().get(0); }
            board.getZoneList().get(to).setGoal(zone.getGoal());
            board.getZoneList().get(to).setBlitzAttack(true);
            board.getZoneList().get(zone.getzId()).setVisited(true);
            if (zone.getBlitzAttack()) {
                board.getZoneList().get(zone.getzId()).setBlitzAttack(false);
            }

            move += nbrOfPodOnZone + " " + from + " " + to + " ";
        }


        // for other pods:
        // only one pod getOut, other pods are for my base defense
        // explore or random -> if neighbor is oppBase -> attack
        move += this.moveWithScore(board, myId);

        // cleaning old goal without zone with blitzAttack
        utils.removeOldGoalExceptBlitzZone(board);

        return move.trim();
    }

    public String moveExplAndRandAndAttackNeihbotBaseOpp(Board board, int myId) {
        String move = "";
        Set<Zone> podZones = utils.findPodZonesList(board, myId);

        for (Zone podZone: podZones ) {
            if (podZone.getGoal() == null) { // get only if no goal
                if (board.getMyBaseZoneId() == podZone.getzId()) {
                    if (myId == 0) {
                        if (board.getZoneList().get(board.getMyBaseZoneId()).getPodsP0() > 1) { // always keep +1 on my base!
                            List<Zone> zonesAroundPodZones = utils.findZonesAroundZone(board.getMovePossiblity(), podZone.getzId(), board);
                            int randomNbr = utils.getRandomInt(0, zonesAroundPodZones.size() - 1);
                            int moveToZone = zonesAroundPodZones.get(randomNbr).getzId();
                            move += "1" + " " + podZone.getzId() + " " + moveToZone + " ";
                            Zone zoneConcerned2 = board.getZoneList().get(moveToZone);
                            zoneConcerned2.setVisited(true);
                            board.updateZone(zoneConcerned2);
                        }
                    } else {
                        if (board.getZoneList().get(board.getMyBaseZoneId()).getPodsP1() > 1) { // always keep +1 on my base!
                            List<Zone> zonesAroundPodZones = utils.findZonesAroundZone(board.getMovePossiblity(), podZone.getzId(), board);
                            int randomNbr = utils.getRandomInt(0, zonesAroundPodZones.size() - 1);
                            int moveToZone = zonesAroundPodZones.get(randomNbr).getzId();
                            move += "1" + " " + podZone.getzId() + " " + moveToZone + " ";
                            Zone zoneConcerned2 = board.getZoneList().get(moveToZone);
                            zoneConcerned2.setVisited(true);
                            board.updateZone(zoneConcerned2);
                        }
                    }


                } else {
                    List<Zone> zonesAroundPodZones = utils.findZonesAroundZone(board.getMovePossiblity(), podZone.getzId(), board);
                    List<Integer> zoneAroundPodsWithoutAlreadyVisited = new ArrayList<>();
                    for (Zone zone: zonesAroundPodZones) {
                        if ((!zone.getVisited()) && (zone.getzId() != board.getMyBaseZoneId())) {
                            zoneAroundPodsWithoutAlreadyVisited.add(zone.getzId());
                        }
                    }
                    int nbrOfPodOnZone = utils.findNbrOfMyPodOnZone(podZone, myId);

                    Optional<Integer> oppBaseIsOnList = utils.checkIfOppBaseIsOnList(zonesAroundPodZones, board.getOppBAseZoneId());
                    if (oppBaseIsOnList.isPresent()) {
//                System.err.println("passage opp found!");
                        move += nbrOfPodOnZone + " " + podZone.getzId() + " " + board.getOppBAseZoneId() + " ";
                    } else {
                        if (!zoneAroundPodsWithoutAlreadyVisited.isEmpty()) {
//                    System.err.println("passage exploration");
                            int randomNbr = utils.getRandomInt(0, zoneAroundPodsWithoutAlreadyVisited.size() - 1);
                            int moveToZoneFiltered = zoneAroundPodsWithoutAlreadyVisited.get(randomNbr);
                            move += nbrOfPodOnZone + " " + podZone.getzId() + " " + moveToZoneFiltered + " ";
                            Zone zoneConcerned2 = board.getZoneList().get(moveToZoneFiltered);
                            zoneConcerned2.setVisited(true);
                            board.updateZone(zoneConcerned2);
                        } else {
//                    System.err.println("passage random");
                            int randomNbr = utils.getRandomInt(0, zonesAroundPodZones.size() - 1);
                            int moveToZone = zonesAroundPodZones.get(randomNbr).getzId();
                            move += nbrOfPodOnZone + " " + podZone.getzId() + " " + moveToZone + " ";
                            Zone zoneConcerned2 = board.getZoneList().get(moveToZone);
                            zoneConcerned2.setVisited(true);
                            board.updateZone(zoneConcerned2);
                        }
                    }
                }


            }

        }
        return move.trim();
    }

    public String moveWithScore(Board board, int myId) {
        String move = "";
        Set<Zone> podZones = utils.findPodZonesList(board, myId);
        Integer nbrOfPod;
        Double score = Double.MIN_VALUE;
        Integer bestZoneChoice  = -1000;
        Set<Integer> bestZoneListSet = new HashSet<>();
        List<Integer> bestZoneList = new ArrayList<>();

        for (Zone podZone: podZones ) {
            if (podZone.getGoal() == null) {
                Long nbrOfNeighbor = podZone.getNeighbor().stream().count(); // count neighbor
                nbrOfPod = utils.getNbrOfPodOnZone(podZone, myId); // count nbr of pod on zone
//                System.err.println("podzone: " + podZone.getzId());
                for (Integer neighborId : podZone.getNeighbor()) { // find best score on neighbor on each zonePod
                    Zone zone = board.getZoneList().get(neighborId);
//                    System.err.println("neighbor:" + neighborId + " Score: " + board.getZoneList().get(neighborId).getScore());
                    if (zone.getScore() > score) {
                        score = zone.getScore();
                        bestZoneChoice = neighborId;
                    }
                }
                if (bestZoneChoice != -1000) {
                    bestZoneChoice = board.getZoneList().get(bestZoneChoice).getzId();
                } else {
                    bestZoneChoice = null;
                }

                for (Integer neighborId : podZone.getNeighbor()) { // other turn for take best score zone list
                    Zone zone = board.getZoneList().get(neighborId);
                    if (zone.getScore().equals(score)) {
                        bestZoneListSet.add(neighborId);
                    }
                }
                bestZoneList.addAll(bestZoneListSet);

//                System.err.println("pods on: " + podZone.getzId() + " bestNextMove: " + bestZoneChoice);
//                System.err.println("best Zone list: " + bestZoneList.toString());

                if (bestZoneList.size() == nbrOfNeighbor) { // find neighbor closed of oppBase
//                    System.err.println("---> passage -> search closed to opp base !!!");
                    Integer closedToOppZoneId = utils.getClosedZoneIdToOppBase(bestZoneList, board);
//                    System.err.println("closed neighbor to opp base: " + closedToOppZoneId);
                    move += "1 " + podZone.getzId() + " " + closedToOppZoneId + " ";
                }
                if (bestZoneList.size() > 1) { // zone with equal score, pod can divise?
                    for (int i = 0; i < bestZoneList.size(); i++) {
//                        System.err.println("bestzone list: " + board.getZoneList().get(bestZoneList.get(i)).getzId());
                        move += "1 " + podZone.getzId() + " " + bestZoneList.get(i) + " ";
                    }
                }
                else {
                    move += nbrOfPod + " " + podZone.getzId() + " " + bestZoneChoice + " ";
                }
                move += nbrOfPod + " " + podZone.getzId() + " " + bestZoneChoice + " ";

                // cleaning old goal without zone with blitzAttack
                utils.removeOldGoalExceptBlitzZone(board);
                bestZoneListSet = new HashSet<>();
                bestZoneList = new ArrayList<>();
                score = Double.MIN_VALUE;
                bestZoneChoice = -1;
            }
        }
        return move.trim();
    }

}
class MoveObj {

    int firstZoneId;
    int secondZoneId;

    // constructor
    public MoveObj(int firstZoneId, int secondZoneId) {
        this.firstZoneId = firstZoneId;
        this.secondZoneId = secondZoneId;
    }
}
 class Node {
    Integer id;
    Node parent;

    // constructor
     public Node() {};

     public Node(Integer id, Node parent) {
         this.id = id;
         this.parent = parent;
     }

     //getters and setters

     public Integer getId() {
         return id;
     }

     public void setId(Integer id) {
         this.id = id;
     }

     public Node getParent() {
         return parent;
     }

     public void setParent(Node parent) {
         this.parent = parent;
     }

     //to string

     @Override
     public String toString() {
         return "Node{" +
                 "id=" + id +
                 ", parent=" + parent +
                 '}';
     }
 }

/**
 * Auto-generated code below aims at helping you parse
 * the standard input according to the problem statement.
 **/
class Player {
    Board board;
//    Zone zone;
    MoveObj moveObj;
    Utils utils;
    Move move;

    public static void main(String args[]) {
        new Player().run();
    }
    public void run() {
        int turnCount = 0;
        List<Zone> zoneList = new ArrayList<>();
        List<MoveObj> moveObjList = new ArrayList<>();
        utils = new Utils();
        move = new Move();

        Scanner in = new Scanner(System.in);
        int playerCount = in.nextInt(); // the amount of players (always 2)
        int myId = in.nextInt(); // my player ID (0 or 1)
        System.err.println("myId:" + myId);
        int zoneCount = in.nextInt(); // the amount of zones on the map
        int linkCount = in.nextInt(); // the amount of links between all zones
        for (int i = 0; i < zoneCount; i++) {
            int zoneId = in.nextInt(); // this zone's ID (between 0 and zoneCount-1)
            int platinumSource = in.nextInt(); // Because of the fog, will always be 0
        }

        for (int i = 0; i < linkCount; i++) {
            int zone1 = in.nextInt();
            int zone2 = in.nextInt();
            moveObj = new MoveObj(zone1, zone2);
            moveObjList.add(moveObj);
        }
        board = new Board(zoneCount, linkCount, moveObjList);

        // game loop
        while (true) {
            turnCount++;
            int myPlatinum = in.nextInt(); // your available Platinum
            for (int i = 0; i < zoneCount; i++) {
                int zId = in.nextInt(); // this zone's ID
                int ownerId = in.nextInt(); // the player who owns this zone (-1 otherwise)
                int podsP0 = in.nextInt(); // player 0's PODs on this zone
                int podsP1 = in.nextInt(); // player 1's PODs on this zone
                int visible = in.nextInt(); // 1 if one of your units can see this tile, else 0
                int platinum = in.nextInt(); // the amount of Platinum this zone can provide (0 if hidden by fog)
                if (turnCount == 1) {
                    Zone zone = new Zone(zId, ownerId, podsP0, podsP1, visible, platinum, 0d);
                    zoneList.add(zone);
                } else {
                    Zone zone = board.getZoneList().get(i);
                    zone.setOwnerId(ownerId);
                    zone.setPodsP0(podsP0);
                    zone.setPodsP1(podsP1);
                    zone.setVisible(visible);
                    board.updateZone(zone);
                }

            }
            if (turnCount == 1) { // search for first move
                board.setZoneList(zoneList);
                int myZoneBaseId = utils.findMyBaseZoneId(board, myId);
                int oppZoneBasId = utils.findOppBAseZoneId(board, utils.findOppId(myId));
                board.setMyBaseZoneId(myZoneBaseId);
                board.setOppBAseZoneId(oppZoneBasId);
                utils.recordNeighborOnZone(board);
                System.err.println("my base: " + board.getZoneList().get(board.getMyBaseZoneId()).toString());


                // find opp path base with BFS and store on Board
                Instant start = Instant.now();
                List<Integer> oppPathList = new ArrayList<>();
                Node result = utils.BFS(board.getMyBaseZoneId(), board.getOppBAseZoneId(), board);
                while (result != null) {
                    oppPathList.add(result.getId());
                    result = result.parent;
                }
                Collections.reverse(oppPathList);
                board.setPathToOpp(oppPathList);
                Instant end = Instant.now();
//                System.err.println("BFS time" + Duration.between(start, end));

                // find distances from myBase to every other zone
                for (int i = 0; i < board.getZoneList().size(); i++) {
                    List<Integer> distanceResult = new ArrayList<>();
                    Node distanceZone = utils.BFS(board.getMyBaseZoneId(), board.getZoneList().get(i).getzId(), board);
                    while (distanceZone != null) {
                        distanceResult.add(distanceZone.getId());
                        distanceZone = distanceZone.parent;
                    }
                    Long distanceLong = distanceResult.stream().count();
                    board.getZoneList().get(board.getMyBaseZoneId()).addDistance(board.getZoneList().get(i).getzId(), distanceLong.intValue());
                }
            }

            // create score for all zone
            List<Zone> allZone = board.getZoneList();
            List<Zone> allZoneScoreUpdated = new ArrayList<>();
            for (Zone zoneToUpdateScore : allZone ) {
                Double score  = utils.updateScoreForZone(board, zoneToUpdateScore.getzId() ,myId);
                zoneToUpdateScore.setScore(score);
                allZoneScoreUpdated.add(zoneToUpdateScore);
            }
            board.setZoneList(allZoneScoreUpdated);

            // all zone visited to false every 10 turns
            if (turnCount % 10 == 0) {
                List<Zone> allList = board.getZoneList();
                for (Zone zone: allList ) {
                    zone.setVisited(false);
                }
                board.setZoneList(allList);
            }

            // first line for movement commands, second line no longer used (see the protocol in the statement for details)
            if (turnCount == 1) {
                System.out.println(move.firstMoveIA13(board));
            } else {
                System.out.println(move.moveIA3(board, myId));
            }
            System.out.println("WAIT");
        }
    }
}

class Utils {
    private final int NOPATH = -1;

    public List<Zone> findZonesAroundZone(List<MoveObj> moveObjList, int inputZoneId, Board board) {
        List<Integer> zoneIdAroundInputInteger = new ArrayList<>();
        List<Zone> zoneAroundInputZone = new ArrayList<>();
        for (MoveObj move: moveObjList) {
            if (move.firstZoneId == inputZoneId ) {
                zoneAroundInputZone.add(this.findZoneWithId(board, move.secondZoneId));
                zoneIdAroundInputInteger.add(move.secondZoneId);
            }
            if (move.secondZoneId == inputZoneId) {
                zoneAroundInputZone.add(this.findZoneWithId(board, move.firstZoneId));
                zoneIdAroundInputInteger.add(move.firstZoneId);
            }
        }
        return zoneAroundInputZone;
    }

    public List<Integer> getIdZonesAroundZone(List<MoveObj> moveObjList, int inputZoneId) {
        List<Integer> zoneIdAroundInputInteger = new ArrayList<>();

        for (MoveObj move: moveObjList) {
            if (move.firstZoneId == inputZoneId ) {
                zoneIdAroundInputInteger.add(move.secondZoneId);
            }
            if (move.secondZoneId == inputZoneId) {
                zoneIdAroundInputInteger.add(move.firstZoneId);
            }
        }

        return zoneIdAroundInputInteger;
    }

    public void recordNeighborOnZone(Board board) {
        List<MoveObj> moveObjList = board.getMovePossiblity();
        List<Zone> zoneList = board.getZoneList();
        for (Zone zone: zoneList) {
            for (MoveObj move: moveObjList) {
                if (move.firstZoneId == zone.getzId() ) {
                    board.getZoneList().get(zone.getzId()).addNeighbor(move.secondZoneId);
                }
                if (move.secondZoneId == zone.getzId()) {
                    board.getZoneList().get(zone.getzId()).addNeighbor(move.firstZoneId);
                }
            }
        }


    }

    public int findMyBaseZoneId(Board board, int myId) {
        for (Zone zone: board.getZoneList()) {
            if (zone.ownerId == myId) {
                return zone.getzId();
            }
        }
        return -1;
    }

    public int findOppBAseZoneId(Board board, int oppId) {
        for (Zone zone: board.getZoneList()) {
            if (zone.ownerId == oppId) {
                return zone.getzId();
            }
        }
        return -1;
    }

    public int findOppId(int myId) {
        if (myId == 0) {
            return 1;
        } else {
            return 0;
        }
    }

    public List<Zone> findPlatinumOnZoneList(List<Zone> inputListZone) {
        List<Zone> platinumZoneList = new ArrayList<>();
        for (Zone zone: inputListZone ) {
            if (zone.platinum > 0) {
                platinumZoneList.add(zone);
            }
        }
        return platinumZoneList;
    }

    public Set<Zone> findPodZonesList(Board board, int myId) {
        Set<Zone> podZonesList = new HashSet<>();

        // find zone of pod
        for (Zone zonePods: board.getZoneList() ) {
            if (myId == 0) {
                if (zonePods.getPodsP0() > 0) {
                    podZonesList.add(zonePods);
                }
            } else {
                if (zonePods.getPodsP1() > 0) {
                    podZonesList.add(zonePods);
                }
            }
        }
        return podZonesList;
    }

    public Zone findZoneWithId(Board board, int zoneId) {
        return board.getZoneList().stream()
                .filter(zone -> zone.getzId() == zoneId)
                .findFirst()
                .orElse(null);
    }

    public int findNbrOfMyPodOnZone(Zone inputZone, int myId) {

        if (myId == 0) {
//            System.err.println("Pod is on zone(player:0): " + inputZone.toString());
            return inputZone.getPodsP0();
        } else {
//            System.err.println("Pod is on zone(player1): " + inputZone.toString());
            return inputZone.getPodsP1();
        }
    }

    public int getRandomInt(int min, int max) {
        Random rand = new Random();
        return  rand.nextInt((max - min) + 1) + min;
    }

    public List<Integer> removeLastVisited(List<Zone> inputZoneList, List<Integer> idZoneLastVisitedList) {
        List<Integer> inputZone = new ArrayList<>();
        for (Zone zone: inputZoneList) { inputZone.add(zone.getzId()); }
        Set<Integer> finalListSet = new HashSet<>();
        List<Integer> finalList = new ArrayList<>();

        if (idZoneLastVisitedList != null) {
//            System.err.println("last visited: " + idZoneLastVisitedList.toString());
        }
//        System.err.println("input list posibility: " + inputZone.toString());

        Boolean same = false;

        for (int i = 0; i < inputZone.size(); i++) {
            for (int j = 0; j < idZoneLastVisitedList.size(); j++) {
                if (inputZone.get(i).equals(idZoneLastVisitedList.get(j))) {
                    same = true;
                    break;
                }
            }
            if (!same) {
                finalListSet.add(inputZone.get(i));
            }
            same = false;
        }

//        System.err.println("final: " + finalListSet.toString());
        for (Integer i : finalListSet) {
            finalList.add(i);
        }


        return finalList;
    }

    public Optional<Integer> checkIfOppBaseIsOnList(List<Zone> inputzoneList, int oppBase) {
        Optional<Integer> result = inputzoneList.stream()
                .filter(zone -> zone.getzId().equals(oppBase))
                .findFirst()
                .map(Zone::getzId);

        return result;
    }

    public Integer getFirstMoveToBFSPath(Integer from, Integer to, Board board) {
        List<Integer> oppPathList = new ArrayList<>();

        Node result = this.BFS(from, to, board);
        while (result != null) {
            oppPathList.add(result.getId());
            result = result.parent;
        }
        Collections.reverse(oppPathList);
        oppPathList.remove(0);
        if (!oppPathList.isEmpty()) {
            return oppPathList.get(0);
        } else {
            return NOPATH;
        }
    }

    public Node BFS(Integer from, Integer to, Board board) {
        Set<Integer> discovered = new HashSet<>();
        List<Node> queue = new ArrayList<>();
        queue.add(new Node(from, null));
        discovered.add(from);

        while (queue.size() > 0) {
            Node current = queue.get(0);
            queue.remove(0);

            if (current.getId().equals(to)) {
                return current;
            }

            List<Integer> neighbor = board.getZoneList().get(current.getId()).getNeighbor();
            for (int i = 0; i < neighbor.size(); i++) {
                if (!discovered.contains(neighbor.get(i))) {
                    discovered.add(neighbor.get(i));
                    queue.add(new Node(neighbor.get(i), current));
                }
            }
        }
        return null;
    }

    public List<Zone> getGroupOfPodForAttackOppBase(Set<Zone> podZone, int myId, int nbrMinForAttack) {
        List<Zone> result = new ArrayList<>();

        for (Zone zone: podZone ) {
            if ((myId == 0) && (zone.getPodsP0() > nbrMinForAttack)) {
//                System.err.println("add for attack");
                result.add(zone);
            }
            if ((myId == 1) && (zone.getPodsP1() > nbrMinForAttack)) {
//                System.err.println("add for attack");
                result.add(zone);
            }
        }
        return result;
    }

    public void removeOldGoalExceptBlitzZone(Board board) {
        for (int i = 0; i < board.getZoneList().size(); i++) {
            if ((board.getZoneList().get(i).getGoal() != null) && (!board.getZoneList().get(i).getBlitzAttack())) {
                Zone zoneToUpdate = board.getZoneList().get(i);
                zoneToUpdate.setGoal(null);
                board.updateZone(zoneToUpdate);
            }
        }
    }

    public Double updateScoreForZone(Board board, Integer zoneId, int myId) {
        double score = 0;
        Zone zoneToUpdate = board.getZoneList().get(zoneId);

        if ((zoneToUpdate.getPlatinum() > 0) && (zoneToUpdate.getOwnerId() != myId)) { // platinum zone Owner other than me
            for (int i = 0; i < zoneToUpdate.getPlatinum(); i++) {
                score += 15;
            }
        }

        if ((zoneToUpdate.getPlatinum() > 0) && (zoneToUpdate.getOwnerId() == myId)) { // platinum zone Owner is me
            for (int i = 0; i < zoneToUpdate.getPlatinum(); i++) {
                score += 0;
            }
        }
        if (zoneToUpdate.getOwnerId() == -1) { // for neutral zone
            score += 2;
        }
        if (myId == 0) { // zone occuped
            if (zoneToUpdate.getOwnerId() == 0) {
                score += 1;
            } else {
                score += 1.5; // opp zone
            }
        }
        if (myId == 1) { // zone occuped
            if (zoneToUpdate.getOwnerId() == 1) {
                score += 1;
            } else {
                score += 1.5; // opp zone
            }
        }
        return score;
    }

    public Integer getNbrOfPodOnZone(Zone zone, int myId) {
        int nbrOfPod;

        if (myId == 0) {
            nbrOfPod = zone.getPodsP0();
        } else {
            nbrOfPod = zone.getPodsP1();
        }
        return nbrOfPod;
    }

    public Integer getClosedZoneIdToOppBase(List<Integer> zoneId, Board board) {
        Long resultLong = 5000l;
        List<Integer> oppPathList;
        Integer forReturn = 0;

        for (int i = 0; i < zoneId.size(); i++) {
            oppPathList = new ArrayList<>();
            Node result = this.BFS(zoneId.get(i), board.getOppBAseZoneId(), board);
            while (result != null) {
                oppPathList.add(result.getId());
                result = result.parent;
            }
            Long nbr = oppPathList.stream().count();
//            System.err.println("nbr onf zone between zone and opp (" + zoneId.get(i) + "): " + nbr);
            if (nbr < resultLong) {
                resultLong = nbr;
                forReturn = zoneId.get(i);
            }
        }
        return forReturn;
    }
}

class Zone {
    Integer zId;
    Integer ownerId;
    Integer podsP0;
    Integer podsP1;
    Integer visible;
    Integer platinum;
    List<Integer> neighbor;
    Map<Integer, Integer> distance;
    Integer goal;
    Boolean visited;
    Boolean blitzAttack;
    Double score;

    // constructor
    public Zone() {
    }

    public Zone(Integer zId, Integer ownerId, Integer podsP0, Integer podsP1, Integer visible, Integer platinum, Double score) {
        this.zId = zId;
        this.ownerId = ownerId;
        this.podsP0 = podsP0;
        this.podsP1 = podsP1;
        this.visible = visible;
        this.platinum = platinum;
        this.neighbor = new ArrayList<>();
        this.distance = new HashMap<>();
        this.goal = null;
        this.visited = false;
        this.blitzAttack = false;
        this.score = score;
    }

    // getters and setters
    public Integer getzId() {
        return zId;
    }

    public void setzId(Integer zId) {
        this.zId = zId;
    }

    public Integer getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Integer ownerId) {
        this.ownerId = ownerId;
    }

    public Integer getPodsP0() {
        return podsP0;
    }

    public void setPodsP0(Integer podsP0) {
        this.podsP0 = podsP0;
    }

    public Integer getPodsP1() {
        return podsP1;
    }

    public void setPodsP1(Integer podsP1) {
        this.podsP1 = podsP1;
    }

    public Integer getVisible() {
        return visible;
    }

    public void setVisible(Integer visible) {
        this.visible = visible;
    }

    public Integer getPlatinum() {
        return platinum;
    }

    public void setPlatinum(Integer platinum) {
        this.platinum = platinum;
    }

    public List<Integer> getNeighbor() {
        return neighbor;
    }

    public void setNeighbor(List<Integer> neighbor) {
        this.neighbor = neighbor;
    }

    public Map<Integer, Integer> getDistance() {
        return distance;
    }

    public void setDistance(Map<Integer, Integer> distance) {
        this.distance = distance;
    }

    public Integer getGoal() {
        return goal;
    }

    public void setGoal(Integer goal) {
        this.goal = goal;
    }

    public Boolean getVisited() {
        return visited;
    }

    public void setVisited(Boolean visited) {
        this.visited = visited;
    }

    public Boolean getBlitzAttack() {
        return blitzAttack;
    }

    public void setBlitzAttack(Boolean blitzAttack) {
        this.blitzAttack = blitzAttack;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    // to string
    @Override
    public String toString() {
        return "Zone{" +
                "zId=" + zId +
                ", ownerId=" + ownerId +
                ", podsP0=" + podsP0 +
                ", podsP1=" + podsP1 +
                ", visible=" + visible +
                ", platinum=" + platinum +
                ", neighbor=" + neighbor +
                ", distance=" + distance +
                ", goal=" + goal +
                ", visited=" + visited +
                ", blitzAttack=" + blitzAttack +
                ", score=" + score +
                '}';
    }

    public void addNeighbor(Integer inputNbr) {
        neighbor.add(inputNbr);
    }

    public void addDistance(Integer to, Integer distanceInput) {
        distance.put(to, distanceInput);
    }
}
