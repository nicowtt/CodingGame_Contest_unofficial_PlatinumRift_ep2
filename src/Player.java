import java.time.Duration;
import java.time.Instant;
import java.util.*;

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

            // create score for all zone
            List<Zone> allZone = board.getZoneList();
            List<Zone> allZoneScoreUpdated = new ArrayList<>();
            for (Zone zoneToUpdateScore : allZone ) {
                Double score  = utils.updateScoreForZone(board, zoneToUpdateScore.getzId() ,myId);
                zoneToUpdateScore.setScore(score);
                allZoneScoreUpdated.add(zoneToUpdateScore);
            }
            board.setZoneList(allZoneScoreUpdated);
            // check
//            System.err.println("zone37 score: " + board.getZoneList().get(37).getScore());
//            System.err.println("zone21 score: " + board.getZoneList().get(21).getScore());


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