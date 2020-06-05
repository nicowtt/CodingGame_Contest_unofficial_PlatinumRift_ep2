import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.*;
import java.util.stream.Collectors;

class Board {
    int zoneCount;
    int linkCount;
    Map<Integer, Integer> moveZoneMap;
    List<Zone> zoneList;

    // constructor
    public Board() {
    }

    public Board(int zoneCount, int linkCount, List<Zone> zoneList) {
        this.zoneCount = zoneCount;
        this.linkCount = linkCount;
        this.zoneList = zoneList;
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

    public Map<Integer, Integer> getMoveZoneMap() {
        return moveZoneMap;
    }

    public void setMoveZoneMap(Map<Integer, Integer> moveZoneMap) {
        this.moveZoneMap = moveZoneMap;
    }

    public List<Zone> getZoneList() {
        return zoneList;
    }

    public void setZoneList(List<Zone> zoneList) {
        this.zoneList = zoneList;
    }
}

/**
 * Auto-generated code below aims at helping you parse
 * the standard input according to the problem statement.
 **/
class Player {
    Board board;
    Zone zone;

    public static void main(String args[]) {
        new Player().run();
    }
    public void run() {
        Map<Integer, Integer> moveZone = new HashMap<>();
        List<Zone> zoneList = new ArrayList<>();
        board = new Board();

        Scanner in = new Scanner(System.in);
        int playerCount = in.nextInt(); // the amount of players (always 2)
        int myId = in.nextInt(); // my player ID (0 or 1)
        int zoneCount = in.nextInt(); // the amount of zones on the map
        int linkCount = in.nextInt(); // the amount of links between all zones
        board.setZoneCount(zoneCount);
        board.setLinkCount(linkCount);
        for (int i = 0; i < zoneCount; i++) {
            int zoneId = in.nextInt(); // this zone's ID (between 0 and zoneCount-1)
            int platinumSource = in.nextInt(); // Because of the fog, will always be 0

        }

        for (int i = 0; i < linkCount; i++) {
            int zone1 = in.nextInt();
            int zone2 = in.nextInt();
            moveZone.put(zone1, zone2);
        }
        board.setMoveZoneMap(moveZone);

        // game loop
        while (true) {
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

            // find where my pod are
            List<Zone> myPodPresence = board.getZoneList().stream()
                    .filter(z -> z.ownerId == myId)
                    .collect(Collectors.toList());
            System.err.println("pod presence list :" + myPodPresence.toString());

            // try to write my first move
            // todo est ce qu'il y a un lien avec la zone d'a coté?
            Map<Integer, Integer> possibleMove = board.getMoveZoneMap();
            Integer firstMove = possibleMove.get(myPodPresence.get(0).getzId());
            System.err.println("possible first move:" + firstMove);



            // Write an action using System.out.println()
            // To debug: System.err.println("Debug messages...");


            // first line for movement commands, second line no longer used (see the protocol in the statement for details)
            System.out.println("10 " + myPodPresence.get(0).getzId() + " " + firstMove);
            System.out.println("WAIT");
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
