class Zone {
    Integer zId;
    Integer ownerId;
    Integer podsP0;
    Integer podsP1;
    Integer visible;
    Integer platinum;

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
                '}';
    }
}
