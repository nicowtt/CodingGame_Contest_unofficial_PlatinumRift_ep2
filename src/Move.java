import java.util.List;

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
        for (Zone zone: zonesAroundMyBase ) { System.err.println("posibility firstMove : " + zone.getzId()); }

        List<Zone> platinumZonePossibility = utils.findPlatinumOnZoneList(zonesAroundMyBase);
        for (Zone zone: platinumZonePossibility ) { System.err.println("posibility of platinum zone : " + zone.getzId()); }

        if (!platinumZonePossibility.isEmpty()) {
            moveZoneId = platinumZonePossibility.get(0).getzId();
        } else {
            moveZoneId = zonesAroundMyBase.get(0).getzId();
        }
    return "10 " + myBaseId + " " + moveZoneId;
    }
}
