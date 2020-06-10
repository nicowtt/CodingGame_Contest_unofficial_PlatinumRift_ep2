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
    public String firstMoveIA13(Board board) {
        int firstMovetoZone;
        String move = "";

        // if opp is in range of 7 zones! -> attack with 10 pods
        if (board.getZoneList().get(board.getMyBaseZoneId()).getDistance().get(board.oppBAseZoneId) <= 8) {
            System.err.println("passage attaque directe!!");
            // attack with 10 drones
            firstMovetoZone = utils.getFirstMoveToBFSPath(board.getMyBaseZoneId(), board.getOppBAseZoneId(), board); // first zone direction to oppBase
            Zone zoneConcerned = board.getZoneList().get(firstMovetoZone);
            zoneConcerned.setGoal(board.oppBAseZoneId);
            zoneConcerned.setVisited(true);
            board.updateZone(zoneConcerned);
            return "10 " + board.getMyBaseZoneId() + " " + firstMovetoZone;
        } else {
            // firstmoveIa2
        }
            int myBaseId = board.getMyBaseZoneId();
            int moveZoneId = -1;
            List<Zone> zonesAroundMyBase = utils.findZonesAroundZone(board.getMovePossiblity(), board.getMyBaseZoneId(), board);
            List<Zone> platinumZonePossibility = utils.findPlatinumOnZoneList(zonesAroundMyBase);

            if (!platinumZonePossibility.isEmpty()) {
                moveZoneId = platinumZonePossibility.get(0).getzId();
                Zone zoneConcerned = board.getZoneList().get(board.getMyBaseZoneId());
                zoneConcerned.setVisited(true);
                board.updateZone(zoneConcerned);
                Zone zoneConcerned2 = board.getZoneList().get(moveZoneId);
                zoneConcerned2.setVisited(true);
                board.updateZone(zoneConcerned2);
            }
            else {
                moveZoneId = zonesAroundMyBase.get(0).getzId();
                Zone zoneConcerned = board.getZoneList().get(board.getMyBaseZoneId());
                zoneConcerned.setVisited(true);
                board.updateZone(zoneConcerned);
                Zone zoneConcerned2 = board.getZoneList().get(moveZoneId);
                zoneConcerned2.setVisited(true);
                board.updateZone(zoneConcerned2);
            }
            return "10 " + myBaseId + " " + moveZoneId;

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
        // for pod with goal
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

        // MOVE IA2
        Set<Zone> podZones = utils.findPodZonesList(board, myId);
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
        System.err.println(move);
        return move.trim();
    }

}
