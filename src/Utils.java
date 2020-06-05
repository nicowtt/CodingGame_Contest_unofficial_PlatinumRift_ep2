import java.util.ArrayList;
import java.util.List;
class Utils {

    public List<Zone> findZonesAroundZone(List<MoveObj> moveObjList, int inputZoneId, Board board) {
        List<Integer> zoneIdAroundInputInteger = new ArrayList<>();
        List<Zone> zoneAroundInputZone = new ArrayList<>();
        for (MoveObj move: moveObjList) {
            if (move.firstZoneId == inputZoneId ) {
                zoneAroundInputZone.add(this.findZoneWithId(board, move.secondZoneId));
                zoneIdAroundInputInteger.add(move.secondZoneId);
            }
            if (move.secondZoneId == inputZoneId) {
                zoneAroundInputZone.add(this.findZoneWithId(board, move.firstZoneId));
                zoneIdAroundInputInteger.add(move.firstZoneId);
            }
        }
        return zoneAroundInputZone;
    }

    public Zone findZoneWithId(Board board, int zoneId) {
        for (int i = 0; i < board.getZoneList().size(); i++) {
            if (board.getZoneList().get(i).getzId() == zoneId) {
                return board.getZoneList().get(i);
            }
        }
        return new Zone();
    }

    public int findMyBaseZoneId(Board board, int myId) {
        for (Zone zone: board.getZoneList()) {
            if (zone.ownerId == myId) {
                return zone.getzId();
            }
        }
        return -1;
    }

    public int findOppBAseZoneId(Board board, int oppId) {
        for (Zone zone: board.getZoneList()) {
            if (zone.ownerId == oppId) {
                return zone.getzId();
            }
        }
        return -1;
    }

    public int findOppId(int myId) {
        if (myId == 0) {
            return 1;
        } else {
            return 0;
        }
    }

    public List<Zone> findPlatinumOnZoneList(List<Zone> inputListZone) {
        List<Zone> platinumZoneList = new ArrayList<>();
        for (Zone zone: inputListZone ) {
            if (zone.platinum > 0) {
                platinumZoneList.add(zone);
            }
        }
        return platinumZoneList;
    }
}
