import java.util.*;

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
                System.out.println("WAIT");
            }
            System.out.println("WAIT");
        }
    }
}