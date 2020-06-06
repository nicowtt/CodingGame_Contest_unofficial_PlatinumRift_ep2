import java.util.ArrayList;
import java.util.List;
import java.util.Random;
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
