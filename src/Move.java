import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

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

            if (zoneAroundPodsWithoutAlreadyVisited.size() > 0 ) {
                int randomNbr = utils.getRandomInt(0, zoneAroundPodsWithoutAlreadyVisited.size() - 1);
                int moveToZoneFiltered = zoneAroundPodsWithoutAlreadyVisited.get(randomNbr);
                int nbrOfPodOnZone = utils.findNbrOfMyPodOnZone(podZone, myId);
                move += nbrOfPodOnZone + " " + podZone.getzId() + " " + moveToZoneFiltered + " ";
                board.addZoneVisited(moveToZoneFiltered);
                System.err.println("passage exploration");
            } else {
                int randomNbr = utils.getRandomInt(0, zonesAroundPodZones.size() - 1);
                int moveToZone = zonesAroundPodZones.get(randomNbr).getzId();
                int nbrOfPodOnZone = utils.findNbrOfMyPodOnZone(podZone, myId);
                move += nbrOfPodOnZone + " " + podZone.getzId() + " " + moveToZone + " ";
                board.addZoneVisited(moveToZone);
            }

            // todo find better path


        }
        System.err.println(move);
        return move.trim();
    }

}
