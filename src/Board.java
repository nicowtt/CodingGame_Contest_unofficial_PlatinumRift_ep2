import java.util.HashMap;
import java.util.List;
import java.util.Map;

class Board {
    int zoneCount;
    int linkCount;
    Map<Integer, Integer> moveZoneMap;
    List<Zone> zoneList;

    // constructor
    public Board() {
    }

    public Board(int zoneCount, int linkCount, List<Zone> zoneList) {
        this.zoneCount = zoneCount;
        this.linkCount = linkCount;
        this.zoneList = zoneList;
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

    public Map<Integer, Integer> getMoveZoneMap() {
        return moveZoneMap;
    }

    public void setMoveZoneMap(Map<Integer, Integer> moveZoneMap) {
        this.moveZoneMap = moveZoneMap;
    }

    public List<Zone> getZoneList() {
        return zoneList;
    }

    public void setZoneList(List<Zone> zoneList) {
        this.zoneList = zoneList;
    }
}
