import java.util.*;
import java.util.stream.Collectors;

class Utils {
    private final int NOPATH = -1;

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

    public List<Integer> getIdZonesAroundZone(List<MoveObj> moveObjList, int inputZoneId) {
        List<Integer> zoneIdAroundInputInteger = new ArrayList<>();

        for (MoveObj move: moveObjList) {
            if (move.firstZoneId == inputZoneId ) {
                zoneIdAroundInputInteger.add(move.secondZoneId);
            }
            if (move.secondZoneId == inputZoneId) {
                zoneIdAroundInputInteger.add(move.firstZoneId);
            }
        }

        return zoneIdAroundInputInteger;
    }

    public void recordNeighborOnZone(Board board) {
        List<MoveObj> moveObjList = board.getMovePossiblity();
        List<Zone> zoneList = board.getZoneList();
        for (Zone zone: zoneList) {
            for (MoveObj move: moveObjList) {
                if (move.firstZoneId == zone.getzId() ) {
                    board.getZoneList().get(zone.getzId()).addNeighbor(move.secondZoneId);
                }
                if (move.secondZoneId == zone.getzId()) {
                    board.getZoneList().get(zone.getzId()).addNeighbor(move.firstZoneId);
                }
            }
        }


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

    public Set<Zone> findPodZonesList(Board board, int myId) {
        Set<Zone> podZonesList = new HashSet<>();

        // find zone of pod
        for (Zone zonePods: board.getZoneList() ) {
            if (myId == 0) {
                if (zonePods.getPodsP0() > 0) {
                    podZonesList.add(zonePods);
                }
            } else {
                if (zonePods.getPodsP1() > 0) {
                    podZonesList.add(zonePods);
                }
            }
        }
        return podZonesList;
    }

    public Zone findZoneWithId(Board board, int zoneId) {
        return board.getZoneList().stream()
                .filter(zone -> zone.getzId() == zoneId)
                .findFirst()
                .orElse(null);
    }

    public int findNbrOfMyPodOnZone(Zone inputZone, int myId) {

        if (myId == 0) {
//            System.err.println("Pod is on zone(player:0): " + inputZone.toString());
            return inputZone.getPodsP0();
        } else {
//            System.err.println("Pod is on zone(player1): " + inputZone.toString());
            return inputZone.getPodsP1();
        }
    }

    public int getRandomInt(int min, int max) {
        Random rand = new Random();
        return  rand.nextInt((max - min) + 1) + min;
    }

    public List<Integer> removeLastVisited(List<Zone> inputZoneList, List<Integer> idZoneLastVisitedList) {
        List<Integer> inputZone = new ArrayList<>();
        for (Zone zone: inputZoneList) { inputZone.add(zone.getzId()); }
        Set<Integer> finalListSet = new HashSet<>();
        List<Integer> finalList = new ArrayList<>();

        if (idZoneLastVisitedList != null) {
//            System.err.println("last visited: " + idZoneLastVisitedList.toString());
        }
//        System.err.println("input list posibility: " + inputZone.toString());

        Boolean same = false;

        for (int i = 0; i < inputZone.size(); i++) {
            for (int j = 0; j < idZoneLastVisitedList.size(); j++) {
                if (inputZone.get(i).equals(idZoneLastVisitedList.get(j))) {
                    same = true;
                    break;
                }
            }
            if (!same) {
                finalListSet.add(inputZone.get(i));
            }
            same = false;
        }

//        System.err.println("final: " + finalListSet.toString());
        for (Integer i : finalListSet) {
            finalList.add(i);
        }


        return finalList;
    }

    public Optional<Integer> checkIfOppBaseIsOnList(List<Zone> inputzoneList, int oppBase) {
        Optional<Integer> result = inputzoneList.stream()
                .filter(zone -> zone.getzId().equals(oppBase))
                .findFirst()
                .map(Zone::getzId);

        return result;
    }

    public Integer getFirstMoveToBFSPath(Integer from, Integer to, Board board) {
        List<Integer> oppPathList = new ArrayList<>();

        Node result = this.BFS(from, to, board);
        while (result != null) {
            oppPathList.add(result.getId());
            result = result.parent;
        }
        Collections.reverse(oppPathList);
        oppPathList.remove(0);
        if (!oppPathList.isEmpty()) {
            return oppPathList.get(0);
        } else {
            return NOPATH;
        }
    }

    public Node BFS(Integer from, Integer to, Board board) {
        Set<Integer> discovered = new HashSet<>();
        List<Node> queue = new ArrayList<>();
        queue.add(new Node(from, null));
        discovered.add(from);

        while (queue.size() > 0) {
            Node current = queue.get(0);
            queue.remove(0);

            if (current.getId().equals(to)) {
                return current;
            }

            List<Integer> neighbor = board.getZoneList().get(current.getId()).getNeighbor();
            for (int i = 0; i < neighbor.size(); i++) {
                if (!discovered.contains(neighbor.get(i))) {
                    discovered.add(neighbor.get(i));
                    queue.add(new Node(neighbor.get(i), current));
                }
            }
        }
        return null;
    }

    public List<Zone> getGroupOfPodForAttackOppBase(Set<Zone> podZone, int myId, int nbrMinForAttack) {
        List<Zone> result = new ArrayList<>();

        for (Zone zone: podZone ) {
            if ((myId == 0) && (zone.getPodsP0() > nbrMinForAttack)) {
//                System.err.println("add for attack");
                result.add(zone);
            }
            if ((myId == 1) && (zone.getPodsP1() > nbrMinForAttack)) {
//                System.err.println("add for attack");
                result.add(zone);
            }
        }
        return result;
    }

    public void removeOldGoalExceptBlitzZone(Board board) {
        for (int i = 0; i < board.getZoneList().size(); i++) {
            if ((board.getZoneList().get(i).getGoal() != null) && (!board.getZoneList().get(i).getBlitzAttack())) {
                Zone zoneToUpdate = board.getZoneList().get(i);
                zoneToUpdate.setGoal(null);
                board.updateZone(zoneToUpdate);
            }
        }
    }

    public Double updateScoreForZone(Board board, Integer zoneId, int myId) {
        double score = 0;
        Zone zoneToUpdate = board.getZoneList().get(zoneId);

        if ((zoneToUpdate.getPlatinum() > 0) && (zoneToUpdate.getOwnerId() != myId)) { // platinum zone Owner other than me
            for (int i = 0; i < zoneToUpdate.getPlatinum(); i++) {
                score += 15;
            }
        }

        if ((zoneToUpdate.getPlatinum() > 0) && (zoneToUpdate.getOwnerId() == myId)) { // platinum zone Owner is me
            for (int i = 0; i < zoneToUpdate.getPlatinum(); i++) {
                score += 0;
            }
        }
        if (zoneToUpdate.getOwnerId() == -1) { // for neutral zone
            score += 2;
        }
        if (myId == 0) { // zone occuped
            if (zoneToUpdate.getOwnerId() == 0) {
                score += 1;
            } else {
                score += 1.5; // opp zone
            }
        }
        if (myId == 1) { // zone occuped
            if (zoneToUpdate.getOwnerId() == 1) {
                score += 1;
            } else {
                score += 1.5; // opp zone
            }
        }
        return score;
    }

    public Integer getNbrOfPodOnZone(Zone zone, int myId) {
        int nbrOfPod;

        if (myId == 0) {
            nbrOfPod = zone.getPodsP0();
        } else {
            nbrOfPod = zone.getPodsP1();
        }
        return nbrOfPod;
    }

    public Integer getClosedZoneIdToOppBase(List<Integer> zoneId, Board board) {
        Long resultLong = 5000l;
        List<Integer> oppPathList;
        Integer forReturn = 0;

        for (int i = 0; i < zoneId.size(); i++) {
            oppPathList = new ArrayList<>();
            Node result = this.BFS(zoneId.get(i), board.getOppBAseZoneId(), board);
            while (result != null) {
                oppPathList.add(result.getId());
                result = result.parent;
            }
            Long nbr = oppPathList.stream().count();
            System.err.println("nbr onf zone between zone and opp (" + zoneId.get(i) + "): " + nbr);
            if (nbr < resultLong) {
                resultLong = nbr;
                forReturn = zoneId.get(i);
            }
        }
        return forReturn;
    }
}
