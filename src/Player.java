import java.util.*;
import java.util.stream.Collectors;

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