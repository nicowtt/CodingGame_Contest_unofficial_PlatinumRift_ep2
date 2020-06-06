import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.*;
import java.util.ArrayList;
import java.util.List;

class Board {
    int zoneCount;
    int linkCount;
    List<Zone> zoneList;
    List<MoveObj> movePossiblity;
    int myBaseZoneId;
    int oppBAseZoneId;

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
}

class Move {
    Utils utils = new Utils();

    /**
     * move 10 pods
     * choice zone with platinum first
     * @param board
     * @return
     */
    public String firstMove(Board board) {
        int myBaseId = board.getMyBaseZoneId();
        int moveZoneId = -1;
        List<Zone> zonesAroundMyBase = utils.findZonesAroundZone(board.getMovePossiblity(), board.getMyBaseZoneId(), board);
//        for (Zone zone: zonesAroundMyBase ) { System.err.println("posibility firstMove : " + zone.getzId()); }

        List<Zone> platinumZonePossibility = utils.findPlatinumOnZoneList(zonesAroundMyBase);
//        for (Zone zone: platinumZonePossibility ) { System.err.println("posibility of platinum zone : " + zone.getzId()); }

        if (!platinumZonePossibility.isEmpty()) {
            moveZoneId = platinumZonePossibility.get(0).getzId();
        } else {
            moveZoneId = zonesAroundMyBase.get(0).getzId();
        }
        return "10 " + myBaseId + " " + moveZoneId;
    }

    public String moveIA1(Board board, int myId) {
        String move = "";
        Random rand = new Random();
        List<Zone> podZones = utils.findPodZonesList(board, myId);

        for (Zone podZone: podZones ) {
            int moveZoneId = -1;

            List<Zone> zonesAroundPodZones = utils.findZonesAroundZone(board.getMovePossiblity(), podZone.getzId(), board);
//            for (Zone zone: zonesAroundPodZones ) { System.err.println("posibility podZone" + podZone.getzId() + ": " + zone.getzId()); }
            int max = zonesAroundPodZones.size() - 1;
            int min = 0;
            int randomNum = rand.nextInt((max - min) + 1) + min;

            moveZoneId = zonesAroundPodZones.get(randomNum).getzId();
            // todo try to remove bug cummul de pod zone avec 10 pods dessus
            // todo try to avoid already pass zone

            int nbrOfPodOnZone = utils.findNbrOfMyPodOnZone(podZone, myId);
            move += nbrOfPodOnZone + " " + podZone.getzId() + " " + moveZoneId + " ";
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
        board = new Board(zoneCount, linkCount, moveObjList );

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
                zone = new Zone(zId, ownerId, podsP0, podsP1, visible, platinum);
                zoneList.add(zone);
            }
            board.setZoneList(zoneList);
            zoneList = new ArrayList<>();

            if (turnCount == 1) { // search for first move
                int myZoneBaseId = utils.findMyBaseZoneId(board, myId);
                int oppZoneBasId = utils.findOppBAseZoneId(board, utils.findOppId(myId));
                board.setMyBaseZoneId(myZoneBaseId);
                board.setOppBAseZoneId(oppZoneBasId);
            }

            // first line for movement commands, second line no longer used (see the protocol in the statement for details)
            if (turnCount == 1) {
                System.out.println(move.firstMove(board));
            } else {
                System.out.println(move.moveIA1(board, myId));
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
            System.err.println("test on zone(pass0): " + inputZone.toString());
            return inputZone.getPodsP0();
        } else {
            System.err.println("test on zone(pass1): " + inputZone.toString());
            return inputZone.getPodsP1();
        }
    }
}
class Zone {
    Integer zId;
    Integer ownerId;
    Integer podsP0;
    Integer podsP1;
    Integer visible;
    Integer platinum;

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
                '}';
    }
}
