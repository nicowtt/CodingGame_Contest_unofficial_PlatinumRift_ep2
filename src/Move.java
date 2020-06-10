import java.util.*;
import java.util.stream.Collectors;

class Move {
    Utils utils = new Utils();
    private final int NOPATH = -1;

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
        int firstMovetoZone;

        // if opp is in range of 7 zones! -> attack with 10 pods
        if (board.getZoneList().get(board.getMyBaseZoneId()).getDistance().get(board.oppBAseZoneId) <= 8) {
            // attack with 10 drones
            firstMovetoZone = utils.getFirstMoveToBFSPath(board.getMyBaseZoneId(), board.getOppBAseZoneId(), board); // first zone direction to oppBase
//            List<Integer> pathToOpp = board.getPathToOpp();
//            pathToOpp.remove(0);
            Zone zoneConcerned = board.getZoneList().get(firstMovetoZone);
            zoneConcerned.setGoal(board.oppBAseZoneId);
            board.updateZone(zoneConcerned);
            return "10 " + board.getMyBaseZoneId() + " " + firstMovetoZone;
        } else {
            // todo divise par 2 -> go closed zone with platinum
            return "";
//            return "10 " + myBaseId + " " + firstMovetoZone;
        }

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
     * if pods are at 7 zone of ennemies try to go on opp base
     * @param board
     * @param myId
     * @return
     */
    public String moveIA3(Board board, int myId) {
        String move = "";

        for (int i = 0; i < board.getZoneList().size(); i++) {
            if (board.getZoneList().get(i).getGoal() != null ) {
                int from = board.getZoneList().get(i).getzId();
                int nbrOfPodOnZone = board.getZoneList().get(i).getPodsP0();
                int to = utils.getFirstMoveToBFSPath(from, board.getZoneList().get(i).getGoal(), board);
                if (to == NOPATH) { to = board.getZoneList().get(i).getNeighbor().get(0); }
                board.getZoneList().get(to).setGoal(board.getZoneList().get(i).getGoal());
                board.getZoneList().get(i).setGoal(null);

                move += nbrOfPodOnZone + " " + from + " " + to + " ";
                break;
            }
        }
        return move.trim();
    }

}
