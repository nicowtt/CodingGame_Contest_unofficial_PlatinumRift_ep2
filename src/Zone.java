import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class Zone {
    Integer zId;
    Integer ownerId;
    Integer podsP0;
    Integer podsP1;
    Integer visible;
    Integer platinum;
    List<Integer> neighbor;
    Map<Integer, Integer> distance;
    Integer goal;
    Boolean visited;

    // constructor
    public Zone() {
    }

    public Zone(Integer zId, Integer ownerId, Integer podsP0, Integer podsP1, Integer visible, Integer platinum) {
        this.zId = zId;
        this.ownerId = ownerId;
        this.podsP0 = podsP0;
        this.podsP1 = podsP1;
        this.visible = visible;
        this.platinum = platinum;
        this.neighbor = new ArrayList<>();
        this.distance = new HashMap<>();
        this.goal = null;
        this.visited = false;
    }

    // getters and setters
    public Integer getzId() {
        return zId;
    }

    public void setzId(Integer zId) {
        this.zId = zId;
    }

    public Integer getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Integer ownerId) {
        this.ownerId = ownerId;
    }

    public Integer getPodsP0() {
        return podsP0;
    }

    public void setPodsP0(Integer podsP0) {
        this.podsP0 = podsP0;
    }

    public Integer getPodsP1() {
        return podsP1;
    }

    public void setPodsP1(Integer podsP1) {
        this.podsP1 = podsP1;
    }

    public Integer getVisible() {
        return visible;
    }

    public void setVisible(Integer visible) {
        this.visible = visible;
    }

    public Integer getPlatinum() {
        return platinum;
    }

    public void setPlatinum(Integer platinum) {
        this.platinum = platinum;
    }

    public List<Integer> getNeighbor() {
        return neighbor;
    }

    public void setNeighbor(List<Integer> neighbor) {
        this.neighbor = neighbor;
    }

    public Map<Integer, Integer> getDistance() {
        return distance;
    }

    public void setDistance(Map<Integer, Integer> distance) {
        this.distance = distance;
    }

    public Integer getGoal() {
        return goal;
    }

    public void setGoal(Integer goal) {
        this.goal = goal;
    }

    public Boolean getVisited() {
        return visited;
    }

    public void setVisited(Boolean visited) {
        this.visited = visited;
    }

    // to string
    @Override
    public String toString() {
        return "Zone{" +
                "zId=" + zId +
                ", ownerId=" + ownerId +
                ", podsP0=" + podsP0 +
                ", podsP1=" + podsP1 +
                ", visible=" + visible +
                ", platinum=" + platinum +
                ", neighbor=" + neighbor +
                ", distance=" + distance +
                ", goal=" + goal +
                ", visited=" + visited +
                '}';
    }

    public void addNeighbor(Integer inputNbr) {
        neighbor.add(inputNbr);
    }

    public void addDistance(Integer to, Integer distanceInput) {
        distance.put(to, distanceInput);
    }
}
