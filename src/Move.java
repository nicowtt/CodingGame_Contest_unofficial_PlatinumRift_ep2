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
        String move = "";

        // if opp is in range of 7 zones! -> attack with 10 pods
        if (board.getZoneList().get(board.getMyBaseZoneId()).getDistance().get(board.oppBAseZoneId) <= 8) {
//            System.err.println("pass direct attack!!");
            // attack with 10 drones
            int firstMovetoZone = utils.getFirstMoveToBFSPath(board.getMyBaseZoneId(), board.getOppBAseZoneId(), board); // first zone direction to oppBase

            Zone nextMoveZone = board.getZoneList().get(firstMovetoZone);
            nextMoveZone.setGoal(board.oppBAseZoneId);
            nextMoveZone.setVisited(true);
            nextMoveZone.setBlitzAttack(true);
            board.updateZone(nextMoveZone);
            return "8 " + board.getMyBaseZoneId() + " " + firstMovetoZone;
        } else {
            List<Integer> myBaseNeighbor = board.getZoneList().get(board.getMyBaseZoneId()).getNeighbor();
            if (myBaseNeighbor.size() < 6) {
                for (int i = 0; i < myBaseNeighbor.size(); i++) {
                    move += "2 " + board.getMyBaseZoneId() + " " + myBaseNeighbor.get(i) + " ";
                }
                Zone myBaseZone = board.getZoneList().get(board.getMyBaseZoneId());
                myBaseZone.setVisited(true);
                board.updateZone(myBaseZone);
                return move;
            } else {
                for (int i = 0; i < myBaseNeighbor.size(); i++) {
                    move += "1 " + board.getMyBaseZoneId() + " " + myBaseNeighbor.get(i) + " ";
                }
                Zone myBaseZone = board.getZoneList().get(board.getMyBaseZoneId());
                myBaseZone.setVisited(true);
                board.updateZone(myBaseZone);
                return move;
            }
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
        // on my base, pods > 20 -> BLITZATTACK to oppBase !!!
        Set<Zone> podZones = utils.findPodZonesList(board, myId);
        List<Zone> podForAttack = utils.getGroupOfPodForAttackOppBase(podZones, myId, 20);
        if (!podForAttack.isEmpty()) {
            for (Zone zone: podForAttack ) {
                Zone zoneForAttack = board.getZoneList().get(zone.getzId()); // put goal and visited zone for attack
                zoneForAttack.setGoal(board.oppBAseZoneId);
                zoneForAttack.setVisited(true);
                board.updateZone(zoneForAttack);
            }
        }

        // for pod with goal -> following their path
        Set<Zone> zoneWithGoal = new HashSet<>();
        int nbrOfPodOnZone = 0;
        for (Zone zone : board.getZoneList() ) {
            if (zone.getGoal() != null) { zoneWithGoal.add(zone); }
        }

        for (Zone zone : zoneWithGoal ) {
            int from = zone.getzId();
            if (myId == 0) {
               nbrOfPodOnZone = zone.getPodsP0();
            } else {
                nbrOfPodOnZone = zone.getPodsP1();
            }
            int to = utils.getFirstMoveToBFSPath(from, zone.getGoal(), board);
            if (to == NOPATH) { to = board.getZoneList().get(zone.getzId()).getNeighbor().get(0); }
            board.getZoneList().get(to).setGoal(zone.getGoal());
            board.getZoneList().get(to).setBlitzAttack(true);
            board.getZoneList().get(zone.getzId()).setVisited(true);
            if (zone.getBlitzAttack()) {
                board.getZoneList().get(zone.getzId()).setBlitzAttack(false);
            }

            move += nbrOfPodOnZone + " " + from + " " + to + " ";
        }


        // for other pods:
        // only one pod getOut, other pods are for my base defense
        // explore or random -> if neighbor is oppBase -> attack
        move += this.moveWithScore(board, myId);

        // cleaning old goal without zone with blitzAttack
        utils.removeOldGoalExceptBlitzZone(board);

        return move.trim();
    }

    public String moveExplAndRandAndAttackNeihbotBaseOpp(Board board, int myId) {
        String move = "";
        Set<Zone> podZones = utils.findPodZonesList(board, myId);

        for (Zone podZone: podZones ) {
            if (podZone.getGoal() == null) { // get only if no goal
                if (board.getMyBaseZoneId() == podZone.getzId()) {
                    if (myId == 0) {
                        if (board.getZoneList().get(board.getMyBaseZoneId()).getPodsP0() > 1) { // always keep +1 on my base!
                            List<Zone> zonesAroundPodZones = utils.findZonesAroundZone(board.getMovePossiblity(), podZone.getzId(), board);
                            int randomNbr = utils.getRandomInt(0, zonesAroundPodZones.size() - 1);
                            int moveToZone = zonesAroundPodZones.get(randomNbr).getzId();
                            move += "1" + " " + podZone.getzId() + " " + moveToZone + " ";
                            Zone zoneConcerned2 = board.getZoneList().get(moveToZone);
                            zoneConcerned2.setVisited(true);
                            board.updateZone(zoneConcerned2);
                        }
                    } else {
                        if (board.getZoneList().get(board.getMyBaseZoneId()).getPodsP1() > 1) { // always keep +1 on my base!
                            List<Zone> zonesAroundPodZones = utils.findZonesAroundZone(board.getMovePossiblity(), podZone.getzId(), board);
                            int randomNbr = utils.getRandomInt(0, zonesAroundPodZones.size() - 1);
                            int moveToZone = zonesAroundPodZones.get(randomNbr).getzId();
                            move += "1" + " " + podZone.getzId() + " " + moveToZone + " ";
                            Zone zoneConcerned2 = board.getZoneList().get(moveToZone);
                            zoneConcerned2.setVisited(true);
                            board.updateZone(zoneConcerned2);
                        }
                    }


                } else {
                    List<Zone> zonesAroundPodZones = utils.findZonesAroundZone(board.getMovePossiblity(), podZone.getzId(), board);
                    List<Integer> zoneAroundPodsWithoutAlreadyVisited = new ArrayList<>();
                    for (Zone zone: zonesAroundPodZones) {
                        if ((!zone.getVisited()) && (zone.getzId() != board.getMyBaseZoneId())) {
                            zoneAroundPodsWithoutAlreadyVisited.add(zone.getzId());
                        }
                    }
                    int nbrOfPodOnZone = utils.findNbrOfMyPodOnZone(podZone, myId);

                    Optional<Integer> oppBaseIsOnList = utils.checkIfOppBaseIsOnList(zonesAroundPodZones, board.getOppBAseZoneId());
                    if (oppBaseIsOnList.isPresent()) {
//                System.err.println("passage opp found!");
                        move += nbrOfPodOnZone + " " + podZone.getzId() + " " + board.getOppBAseZoneId() + " ";
                    } else {
                        if (!zoneAroundPodsWithoutAlreadyVisited.isEmpty()) {
//                    System.err.println("passage exploration");
                            int randomNbr = utils.getRandomInt(0, zoneAroundPodsWithoutAlreadyVisited.size() - 1);
                            int moveToZoneFiltered = zoneAroundPodsWithoutAlreadyVisited.get(randomNbr);
                            move += nbrOfPodOnZone + " " + podZone.getzId() + " " + moveToZoneFiltered + " ";
                            Zone zoneConcerned2 = board.getZoneList().get(moveToZoneFiltered);
                            zoneConcerned2.setVisited(true);
                            board.updateZone(zoneConcerned2);
                        } else {
//                    System.err.println("passage random");
                            int randomNbr = utils.getRandomInt(0, zonesAroundPodZones.size() - 1);
                            int moveToZone = zonesAroundPodZones.get(randomNbr).getzId();
                            move += nbrOfPodOnZone + " " + podZone.getzId() + " " + moveToZone + " ";
                            Zone zoneConcerned2 = board.getZoneList().get(moveToZone);
                            zoneConcerned2.setVisited(true);
                            board.updateZone(zoneConcerned2);
                        }
                    }
                }


            }

        }
        return move.trim();
    }

    public String moveWithScore(Board board, int myId) {
        String move = "";
        Set<Zone> podZones = utils.findPodZonesList(board, myId);
        Integer nbrOfPod;
        Double score = Double.MIN_VALUE;
        Integer bestZoneChoice  = -1000;
        Set<Integer> bestZoneListSet = new HashSet<>();
        List<Integer> bestZoneList = new ArrayList<>();

        for (Zone podZone: podZones ) {
            if (podZone.getGoal() == null) {
                Long nbrOfNeighbor = podZone.getNeighbor().stream().count(); // count neighbor
                nbrOfPod = utils.getNbrOfPodOnZone(podZone, myId); // count nbr of pod on zone
//                System.err.println("podzone: " + podZone.getzId());
                for (Integer neighborId : podZone.getNeighbor()) { // find best score on neighbor on each zonePod
                    Zone zone = board.getZoneList().get(neighborId);
//                    System.err.println("neighbor:" + neighborId + " Score: " + board.getZoneList().get(neighborId).getScore());
                    if (zone.getScore() > score) {
                        score = zone.getScore();
                        bestZoneChoice = neighborId;
                    }
                }
                if (bestZoneChoice != -1000) {
                    bestZoneChoice = board.getZoneList().get(bestZoneChoice).getzId();
                } else {
                    bestZoneChoice = null;
                }

                for (Integer neighborId : podZone.getNeighbor()) { // other turn for take best score zone list
                    Zone zone = board.getZoneList().get(neighborId);
                    if (zone.getScore().equals(score)) {
                        bestZoneListSet.add(neighborId);
                    }
                }
                bestZoneList.addAll(bestZoneListSet);

//                System.err.println("pods on: " + podZone.getzId() + " bestNextMove: " + bestZoneChoice);
//                System.err.println("best Zone list: " + bestZoneList.toString());

                if (bestZoneList.size() == nbrOfNeighbor) { // find neighbor closed of oppBase
//                    System.err.println("---> passage -> search closed to opp base !!!");
                    Integer closedToOppZoneId = utils.getClosedZoneIdToOppBase(bestZoneList, board);
//                    System.err.println("closed neighbor to opp base: " + closedToOppZoneId);
                    move += "1 " + podZone.getzId() + " " + closedToOppZoneId + " ";
                }
                if (bestZoneList.size() > 1) { // zone with equal score, pod can divise?
                    for (int i = 0; i < bestZoneList.size(); i++) {
//                        System.err.println("bestzone list: " + board.getZoneList().get(bestZoneList.get(i)).getzId());
                        move += "1 " + podZone.getzId() + " " + bestZoneList.get(i) + " ";
                    }
                }
                else {
                    move += nbrOfPod + " " + podZone.getzId() + " " + bestZoneChoice + " ";
                }
                move += nbrOfPod + " " + podZone.getzId() + " " + bestZoneChoice + " ";

                // cleaning old goal without zone with blitzAttack
                utils.removeOldGoalExceptBlitzZone(board);
                bestZoneListSet = new HashSet<>();
                bestZoneList = new ArrayList<>();
                score = Double.MIN_VALUE;
                bestZoneChoice = -1;
            }
        }
        return move.trim();
    }

}
