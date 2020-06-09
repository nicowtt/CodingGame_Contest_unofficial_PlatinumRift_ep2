import java.util.*;

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

    public List<Integer> getIdZonesAroundZone(List<MoveObj> moveObjList, int inputZoneId, Board board) {
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

        return zoneIdAroundInputInteger;
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

    public List<Zone> findPodZonesList(Board board, int myId) {
        List<Zone> podZonesList = new ArrayList<>();

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
            System.err.println("Pod is on zone(player:0): " + inputZone.toString());
            return inputZone.getPodsP0();
        } else {
            System.err.println("Pod is on zone(player1): " + inputZone.toString());
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

    public Node BFS(Integer from, Integer to, Board board) {
        Set<Integer> discovered = new HashSet<>();
        List<Node> queue = new ArrayList<>();
        queue.add(new Node(from, null));
        discovered.add(from);

        while (queue.size() > 0) {
            Node current = queue.get(0);
            queue.remove(0);

            if (current.getId() == to) {
                return current;
            }

            List<Integer> neighbor = this.getIdZonesAroundZone(board.getMovePossiblity(), current.getId(), board);
            for (int i = 0; i < neighbor.size(); i++) {
                if (!discovered.contains(neighbor)) {
                    discovered.add(neighbor.get(i));
                    Node node = new Node(neighbor.get(i), current);
                    queue.add(node);
                }
            }
        }
        return null;
    }
}
