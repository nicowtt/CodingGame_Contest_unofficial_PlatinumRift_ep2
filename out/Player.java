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
import java.util.List;
import java.util.Map;

class Board {
    int zoneCount;
    int linkCount;
    List<Zone> zoneList;
    List<MoveObj> movePossiblity;
    int myBaseZoneId;
    int oppBAseZoneId;
    List<Integer> zoneVisited;
    List<Integer> pathToOpp;

    // constructor
    public Board() {
    }

    public Board(int zoneCount, int linkCount, List<MoveObj> movePossiblity, List<Integer> zoneVisited) {
        this.zoneCount = zoneCount;
        this.linkCount = linkCount;
        this.movePossiblity = movePossiblity;
        this.zoneVisited = zoneVisited;
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

    public List<Integer> getZoneVisited() {
        return zoneVisited;
    }

    public void setZoneVisited(List<Integer> zoneVisited) {
        this.zoneVisited = zoneVisited;
    }

    public List<Integer> getPathToOpp() {
        return pathToOpp;
    }

    public void setPathToOpp(List<Integer> inputPathList) {
        this.pathToOpp = inputPathList;
    }

    // methods
    public void addZoneVisited(Integer idZoneVisited) {
        this.zoneVisited.add(idZoneVisited);
    }
}

class Move {
    Utils utils = new Utils();

    /**
     * move 10 pods
     * choice zone with platinum first
     * @param board
     * @return
     */
    public String firstMoveIA1and2(Board board) {
        int myBaseId = board.getMyBaseZoneId();
        board.addZoneVisited(board.getMyBaseZoneId());
        int moveZoneId = -1;
        List<Zone> zonesAroundMyBase = utils.findZonesAroundZone(board.getMovePossiblity(), board.getMyBaseZoneId(), board);
        List<Zone> platinumZonePossibility = utils.findPlatinumOnZoneList(zonesAroundMyBase);

        if (!platinumZonePossibility.isEmpty()) {
            moveZoneId = platinumZonePossibility.get(0).getzId();
            board.addZoneVisited(platinumZonePossibility.get(0).getzId());
        }
        else {
            moveZoneId = zonesAroundMyBase.get(0).getzId();
            board.addZoneVisited(zonesAroundMyBase.get(0).getzId());
        }
        return "10 " + myBaseId + " " + moveZoneId;
    }

    /**
     * move 10 pods
     * choice zone with platinum first
     * @param board
     * @return
     */
    public String firstMoveIA13(Board board) {
        int myBaseId = board.getMyBaseZoneId();
        board.addZoneVisited(board.getMyBaseZoneId());
        int moveZoneId = -1;
        List<Zone> zonesAroundMyBase = utils.findZonesAroundZone(board.getMovePossiblity(), board.getMyBaseZoneId(), board);
        List<Zone> platinumZonePossibility = utils.findPlatinumOnZoneList(zonesAroundMyBase);

        if (!platinumZonePossibility.isEmpty()) {
            moveZoneId = platinumZonePossibility.get(0).getzId();
            board.addZoneVisited(platinumZonePossibility.get(0).getzId());
        }
        else {
            moveZoneId = zonesAroundMyBase.get(0).getzId();
            board.addZoneVisited(zonesAroundMyBase.get(0).getzId());
        }
        return "10 " + myBaseId + " " + moveZoneId;
    }

    /**
     * Random move
     * @param board
     * @param myId
     * @return
     */
    public String moveIA1(Board board, int myId) {
        String move = "";
        Random rand = new Random();
        List<Zone> podZones = utils.findPodZonesList(board, myId);

        for (Zone podZone: podZones ) {
            List<Zone> zonesAroundPodZones = utils.findZonesAroundZone(board.getMovePossiblity(), podZone.getzId(), board);
            int max = zonesAroundPodZones.size() - 1;
            int min = 0;
            int randomNum = rand.nextInt((max - min) + 1) + min;

            int moveZoneId = zonesAroundPodZones.get(randomNum).getzId();
            // todo try to avoid already pass zone
            // todo find better path

            int nbrOfPodOnZone = utils.findNbrOfMyPodOnZone(podZone, myId);
            move += nbrOfPodOnZone + " " + podZone.getzId() + " " + moveZoneId + " ";
        }
        System.err.println(move);
        return move.trim();
    }

    /**
     * every 5 turns, mode exploration is on
     * else random move
     * @param board
     * @param myId
     * @return
     */
    public String moveIA2(Board board, int myId) {
        String move = "";
        List<Zone> podZones = utils.findPodZonesList(board, myId);

        for (Zone podZone: podZones ) {
            List<Zone> zonesAroundPodZones = utils.findZonesAroundZone(board.getMovePossiblity(), podZone.getzId(), board);
            List<Integer> zoneAroundPodsWithoutAlreadyVisited = utils.removeLastVisited(zonesAroundPodZones, board.getZoneVisited());
            int nbrOfPodOnZone = utils.findNbrOfMyPodOnZone(podZone, myId);

            Optional<Integer> oppBaseIsOnList = utils.checkIfOppBaseIsOnList(zonesAroundPodZones, board.getOppBAseZoneId());
            if (oppBaseIsOnList.isPresent()) {
                System.err.println("passage opp found!");
                move += nbrOfPodOnZone + " " + podZone.getzId() + " " + board.getOppBAseZoneId() + " ";
            } else {
                if (zoneAroundPodsWithoutAlreadyVisited.size() > 0 ) {
                    int randomNbr = utils.getRandomInt(0, zoneAroundPodsWithoutAlreadyVisited.size() - 1);
                    int moveToZoneFiltered = zoneAroundPodsWithoutAlreadyVisited.get(randomNbr);
                    move += nbrOfPodOnZone + " " + podZone.getzId() + " " + moveToZoneFiltered + " ";
                    board.addZoneVisited(moveToZoneFiltered);
                    System.err.println("passage exploration");
                } else {
                    int randomNbr = utils.getRandomInt(0, zonesAroundPodZones.size() - 1);
                    int moveToZone = zonesAroundPodZones.get(randomNbr).getzId();
                    move += nbrOfPodOnZone + " " + podZone.getzId() + " " + moveToZone + " ";
                    board.addZoneVisited(moveToZone);
                }
            }
        }
        System.err.println(move);
        return move.trim();
    }

    /**
     * I know path from myBase to oppBase
     *
     * @param board
     * @param myId
     * @return
     */
    public String moveIA3(Board board, int myId) {
        String move = "";
        List<Zone> podZones = utils.findPodZonesList(board, myId);

        for (Zone podZone: podZones ) {
            List<Zone> zonesAroundPodZones = utils.findZonesAroundZone(board.getMovePossiblity(), podZone.getzId(), board);
            List<Integer> zoneAroundPodsWithoutAlreadyVisited = utils.removeLastVisited(zonesAroundPodZones, board.getZoneVisited());
            int nbrOfPodOnZone = utils.findNbrOfMyPodOnZone(podZone, myId);

            Optional<Integer> oppBaseIsOnList = utils.checkIfOppBaseIsOnList(zonesAroundPodZones, board.getOppBAseZoneId());
            if (oppBaseIsOnList.isPresent()) {
                System.err.println("passage opp found!");
                move += nbrOfPodOnZone + " " + podZone.getzId() + " " + board.getOppBAseZoneId() + " ";
            } else {
                if (zoneAroundPodsWithoutAlreadyVisited.size() > 0 ) {
                    int randomNbr = utils.getRandomInt(0, zoneAroundPodsWithoutAlreadyVisited.size() - 1);
                    int moveToZoneFiltered = zoneAroundPodsWithoutAlreadyVisited.get(randomNbr);
                    move += nbrOfPodOnZone + " " + podZone.getzId() + " " + moveToZoneFiltered + " ";
                    board.addZoneVisited(moveToZoneFiltered);
                    System.err.println("passage exploration");
                } else {
                    int randomNbr = utils.getRandomInt(0, zonesAroundPodZones.size() - 1);
                    int moveToZone = zonesAroundPodZones.get(randomNbr).getzId();
                    move += nbrOfPodOnZone + " " + podZone.getzId() + " " + moveToZone + " ";
                    board.addZoneVisited(moveToZone);
                }
            }
            // todo find better path
            System.err.println("path to opp:" + board.getPathToOpp().toString());

        }
        System.err.println(move);
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
    Zone zone;
    MoveObj moveObj;
    Utils utils;
    Move move;

    public static void main(String args[]) {
        new Player().run();
    }
    public void run() {
        int turnCount = 0;
        Map<Integer, Integer> moveZone = new HashMap<>();
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
        board = new Board(zoneCount, linkCount, moveObjList, new ArrayList<>());

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
                zone = new Zone(zId, ownerId, podsP0, podsP1, visible, platinum, new ArrayList<>(), new HashMap<>());
                zoneList.add(zone);
            }
            board.setZoneList(zoneList);
            zoneList = new ArrayList<>();

            if (turnCount == 1) { // search for first move
                int myZoneBaseId = utils.findMyBaseZoneId(board, myId);
                int oppZoneBasId = utils.findOppBAseZoneId(board, utils.findOppId(myId));
                board.setMyBaseZoneId(myZoneBaseId);
                board.setOppBAseZoneId(oppZoneBasId);
                utils.recordNeighborOnZone(board);

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
            if (turnCount % 10 == 0) { // only for moveIA2
                List<Integer> newList = new ArrayList<>();
                board.setZoneVisited(newList);
            }

            // first line for movement commands, second line no longer used (see the protocol in the statement for details)
            if (turnCount == 1) {
                System.out.println(move.firstMoveIA1and2(board));
            } else {
                System.out.println(move.moveIA2(board, myId));
            }
            System.out.println("WAIT");
        }
    }
}

class Utils {

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

    public List<Zone> findPodZonesList(Board board, int myId) {
        List<Zone> podZonesList = new ArrayList<>();

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
            System.err.println("Pod is on zone(player:0): " + inputZone.toString());
            return inputZone.getPodsP0();
        } else {
            System.err.println("Pod is on zone(player1): " + inputZone.toString());
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

    // constructor
    public Zone() {
    }

    public Zone(Integer zId, Integer ownerId, Integer podsP0, Integer podsP1, Integer visible, Integer platinum, List<Integer> neighbor, Map<Integer, Integer> distance) {
        this.zId = zId;
        this.ownerId = ownerId;
        this.podsP0 = podsP0;
        this.podsP1 = podsP1;
        this.visible = visible;
        this.platinum = platinum;
        this.neighbor = neighbor;
        this.distance = distance;
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
                '}';
    }

    public void addNeighbor(Integer inputNbr) {
        neighbor.add(inputNbr);
    }

    public void addDistance(Integer to, Integer distanceInput) {
        distance.put(to, distanceInput);
    }
}
