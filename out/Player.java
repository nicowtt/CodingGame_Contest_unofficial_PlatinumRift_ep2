import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.*;
import java.util.stream.Collectors;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.*;
import java.util.stream.Collectors;
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
        boolean fivePodAlreadyStart = false;
        boolean tenPodAlreadyStart = false;
        String move = "";

        // if opp is in range of 7 zones! -> attack with 10 pods
        if (board.getZoneList().get(board.getMyBaseZoneId()).getDistance().get(board.oppBAseZoneId) <= 8) {
            System.err.println("pass direct attack!!");
            // attack with 10 drones
            int firstMovetoZone = utils.getFirstMoveToBFSPath(board.getMyBaseZoneId(), board.getOppBAseZoneId(), board); // first zone direction to oppBase

            Zone nextMoveZone = board.getZoneList().get(firstMovetoZone);
            nextMoveZone.setGoal(board.oppBAseZoneId);
            nextMoveZone.setVisited(true);
            board.updateZone(nextMoveZone);
            return "10 " + board.getMyBaseZoneId() + " " + firstMovetoZone;
        } else {
            //
            int myBaseId = board.getMyBaseZoneId();
            Zone myBaseZone;
            Zone firstNextMoveZone;
            Zone secondNextMoveZone;

            List<Zone> zonesAroundMyBase = utils.findZonesAroundZone(board.getMovePossiblity(), board.getMyBaseZoneId(), board);
            List<Zone> platinumZonePossibility = utils.findPlatinumOnZoneList(zonesAroundMyBase);

            if (platinumZonePossibility.size() == 1) { // run 5 pods
                int moveZoneId = platinumZonePossibility.get(0).getzId();

                firstNextMoveZone = board.getZoneList().get(moveZoneId);
                firstNextMoveZone.setVisited(true);
                board.updateZone(firstNextMoveZone);

                move += "5 " + myBaseId + " " + firstNextMoveZone.getzId() + " ";
                fivePodAlreadyStart = true;
            }

            if (platinumZonePossibility.size() > 1){
                int firstMoveZoneId = platinumZonePossibility.get(0).getzId();
                int secondMoveZoneId = platinumZonePossibility.get(1).getzId();

                firstNextMoveZone = board.getZoneList().get(firstMoveZoneId);
                firstNextMoveZone.setVisited(true);
                board.updateZone(firstNextMoveZone);

                secondNextMoveZone = board.getZoneList().get(secondMoveZoneId);
                secondNextMoveZone.setVisited(true);
                board.updateZone(secondNextMoveZone);

                move += "5 " + myBaseId + " " + firstNextMoveZone.getzId() + " ";
                move += "5 " + myBaseId + " " + secondNextMoveZone.getzId() + " ";
                tenPodAlreadyStart = true;
            }

            if (platinumZonePossibility.isEmpty()) {
                if (fivePodAlreadyStart ) {
                    // start 5 other first neighbor
                    int moveZoneId = zonesAroundMyBase.get(0).getzId();

                    secondNextMoveZone = board.getZoneList().get(moveZoneId);
                    secondNextMoveZone.setVisited(true);
                    board.updateZone(secondNextMoveZone);

                    move += "5 " + myBaseId + " " + secondNextMoveZone.getzId()+ " ";
                    tenPodAlreadyStart = true;
                }
                if (!tenPodAlreadyStart) {
                    // start 2 * 5 pods if you can
                    if (zonesAroundMyBase.size() > 1) {
                        int firstMoveZoneId = zonesAroundMyBase.get(0).getzId();
                        int secondMoveZoneId = zonesAroundMyBase.get(1).getzId();

                        firstNextMoveZone = board.getZoneList().get(firstMoveZoneId);
                        firstNextMoveZone.setVisited(true);
                        board.updateZone(firstNextMoveZone);

                        secondNextMoveZone = board.getZoneList().get(secondMoveZoneId);
                        secondNextMoveZone.setVisited(true);
                        board.updateZone(secondNextMoveZone);

                        move += "5 " + myBaseId + " " + firstNextMoveZone.getzId() + " ";
                        move += "5 " + myBaseId + " " + secondNextMoveZone.getzId() + " ";
                    }
                    else {
                        int firstMoveZoneId = zonesAroundMyBase.get(0).getzId();

                        firstNextMoveZone = board.getZoneList().get(firstMoveZoneId);
                        firstNextMoveZone.setVisited(true);
                        board.updateZone(firstNextMoveZone);

                        move += "10 " + myBaseId + " " + firstNextMoveZone.getzId() + " ";
                    }

                }
            }

            myBaseZone = board.getZoneList().get(board.getMyBaseZoneId());
            myBaseZone.setVisited(true);
            board.updateZone(myBaseZone);
            return move;
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
        //todo check if zonePod > 20 in far to oppBase


        // for pod with goal -> direct attack for now
        for (int i = 0; i < board.getZoneList().size(); i++) {
            if (board.getZoneList().get(i).getGoal() != null ) {
                int from = board.getZoneList().get(i).getzId();
                int nbrOfPodOnZone = board.getZoneList().get(i).getPodsP0();
                int to = utils.getFirstMoveToBFSPath(from, board.getZoneList().get(i).getGoal(), board);
                if (to == NOPATH) { to = board.getZoneList().get(i).getNeighbor().get(0); }
                board.getZoneList().get(to).setGoal(board.getZoneList().get(i).getGoal());
                board.getZoneList().get(i).setGoal(null);
                board.getZoneList().get(i).setVisited(true);

                move += nbrOfPodOnZone + " " + from + " " + to + " ";
                break;
            }
        }

        // MOVE IA2 -> explore or random -> if neighbor is oppBase -> attack
        move += this.moveExplAndRandAndAttackNeihbotBaseOpp(board, myId);

        System.err.println(move);
        return move.trim();
    }

    public String moveExplAndRandAndAttackNeihbotBaseOpp(Board board, int myId) {
        String move = "";
        Set<Zone> podZones = utils.findPodZonesList(board, myId);
//        check
//        for (Zone zone : podZones ) {
//            System.err.println("podZones: " + zone.getzId());
//            List<Integer> neighbor = zone.getNeighbor();
//            System.err.println("neighbor: " + zone.getNeighbor());
//            for (int i = 0; i < neighbor.size(); i++) {
//                System.err.println("id: " + board.getZoneList().get(neighbor.get(i)).getzId() + " visited: " + board.getZoneList().get(neighbor.get(i)).getVisited());
//            }
//        }

        for (Zone podZone: podZones ) {
            List<Zone> zonesAroundPodZones = utils.findZonesAroundZone(board.getMovePossiblity(), podZone.getzId(), board);
            List<Integer> zoneAroundPodsWithoutAlreadyVisited = new ArrayList<>();
            for (Zone zone: zonesAroundPodZones) {
                if (!zone.getVisited()) {
                    zoneAroundPodsWithoutAlreadyVisited.add(zone.getzId());
                }
            }
            int nbrOfPodOnZone = utils.findNbrOfMyPodOnZone(podZone, myId);

            Optional<Integer> oppBaseIsOnList = utils.checkIfOppBaseIsOnList(zonesAroundPodZones, board.getOppBAseZoneId());
            if (oppBaseIsOnList.isPresent()) {
                System.err.println("passage opp found!");
                move += nbrOfPodOnZone + " " + podZone.getzId() + " " + board.getOppBAseZoneId() + " ";
            } else {
                if (!zoneAroundPodsWithoutAlreadyVisited.isEmpty()) {
                    System.err.println("passage exploration");
                    int randomNbr = utils.getRandomInt(0, zoneAroundPodsWithoutAlreadyVisited.size() - 1);
                    int moveToZoneFiltered = zoneAroundPodsWithoutAlreadyVisited.get(randomNbr);
                    move += nbrOfPodOnZone + " " + podZone.getzId() + " " + moveToZoneFiltered + " ";
                    Zone zoneConcerned2 = board.getZoneList().get(moveToZoneFiltered);
                    zoneConcerned2.setVisited(true);
                    board.updateZone(zoneConcerned2);
                } else {
                    System.err.println("passage random");
                    int randomNbr = utils.getRandomInt(0, zonesAroundPodZones.size() - 1);
                    int moveToZone = zonesAroundPodZones.get(randomNbr).getzId();
                    move += nbrOfPodOnZone + " " + podZone.getzId() + " " + moveToZone + " ";
                    Zone zoneConcerned2 = board.getZoneList().get(moveToZone);
                    zoneConcerned2.setVisited(true);
                    board.updateZone(zoneConcerned2);
                }
            }
        }
        return move;
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
                    Zone zone = new Zone(zId, ownerId, podsP0, podsP1, visible, platinum);
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
                System.err.println("BFS time" + Duration.between(start, end));

                // find distances from myBase to every other zone
                Instant start2 = Instant.now();
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
                Instant end2 = Instant.now();
                System.err.println("BFS time my base to each zone" + Duration.between(start2, end2));
            }

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

    // constructor
    public Zone() {
    }

    public Zone(Integer zId, Integer ownerId, Integer podsP0, Integer podsP1, Integer visible, Integer platinum) {
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
                '}';
    }

    public void addNeighbor(Integer inputNbr) {
        neighbor.add(inputNbr);
    }

    public void addDistance(Integer to, Integer distanceInput) {
        distance.put(to, distanceInput);
    }
}
