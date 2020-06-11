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
