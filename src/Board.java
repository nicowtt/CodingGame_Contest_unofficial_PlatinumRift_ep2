import java.util.HashMap;
import java.util.List;
import java.util.Map;

class Board {
    int zoneCount;
    int linkCount;
    List<Zone> zoneList;
    List<MoveObj> movePossiblity;
    int myBaseZoneId;
    int oppBAseZoneId;
    List<Integer> pathToOpp;

    // constructor
    public Board() {
    }

    public Board(int zoneCount, int linkCount, List<MoveObj> movePossiblity) {
        this.zoneCount = zoneCount;
        this.linkCount = linkCount;
        this.movePossiblity = movePossiblity;
    }

    // getters and setters
    public Integer getZoneCount() {
        return zoneCount;
    }

    public void setZoneCount(Integer zoneCount) {
        this.zoneCount = zoneCount;
    }

    public Integer getLinkCount() {
        return linkCount;
    }

    public void setLinkCount(Integer linkCount) {
        this.linkCount = linkCount;
    }

    public List<MoveObj> getMovePossiblity() {
        return movePossiblity;
    }

    public void setMovePossiblity(List<MoveObj> movePossiblity) {
        this.movePossiblity = movePossiblity;
    }

    public List<Zone> getZoneList() {
        return zoneList;
    }

    public void setZoneList(List<Zone> zoneList) {
        this.zoneList = zoneList;
    }

    public int getMyBaseZoneId() {
        return myBaseZoneId;
    }

    public void setMyBaseZoneId(int myBaseZoneId) {
        this.myBaseZoneId = myBaseZoneId;
    }

    public int getOppBAseZoneId() {
        return oppBAseZoneId;
    }

    public void setOppBAseZoneId(int oppBAseZoneId) {
        this.oppBAseZoneId = oppBAseZoneId;
    }

    public List<Integer> getPathToOpp() {
        return pathToOpp;
    }

    public void setPathToOpp(List<Integer> inputPathList) {
        this.pathToOpp = inputPathList;
    }

    // methods
    public void updateZone(Zone zoneToUpdate) {
        zoneList.add(zoneToUpdate);
    }
}
