package programs;

import com.battle.heroes.army.Unit;
import com.battle.heroes.army.programs.Edge;
import com.battle.heroes.army.programs.UnitTargetPathFinder;

import java.util.*;

public class UnitTargetPathFinderImpl implements UnitTargetPathFinder {

    private static final int WIDTH = 27;
    private static final int HEIGHT = 21;
    private static final int[] DX = {0, -1, 1, 0, -1, -1, 1, 1};
    private static final int[] DY = {-1, 0, 0, 1, -1, 1, -1, 1};


    @Override
    public List<Edge> getTargetPath(Unit attackUnit, Unit targetUnit, List<Unit> existingUnitList) {
        Edge start = new Edge(attackUnit.getxCoordinate(), attackUnit.getyCoordinate());
        Edge target = new Edge(targetUnit.getxCoordinate(), targetUnit.getyCoordinate());

        Set<String> occupiedEdges = new HashSet<>();
        for (Unit u : existingUnitList) {
            if (u != null && u.isAlive() &&
                    !(u.getxCoordinate() == start.getX() && u.getyCoordinate() == start.getY()) &&
                    !(u.getxCoordinate() == target.getX() && u.getyCoordinate() == target.getY())) {
                occupiedEdges.add(u.getxCoordinate() + ":" + u.getyCoordinate());
            }
        }

        Queue<Edge> queue = new LinkedList<>();
        Map<String, Edge> fromCoordsWithEdges = new HashMap<>();
        Set<String> checkedCoords = new HashSet<>();

        queue.add(start);
        checkedCoords.add(coordToString(start));

        while (!queue.isEmpty()) {
            Edge current = queue.poll();

            if (current.getX() == target.getX() && current.getY() == target.getY()) return buildPath(fromCoordsWithEdges, start, target);

            for (int i = 0; i < DX.length; i++) {
                int nx = current.getX() + DX[i];
                int ny = current.getY() + DY[i];

                if (nx < 0 || nx >= WIDTH || ny < 0 || ny >= HEIGHT) continue;

                String key = nx + ":" + ny;
                if (!checkedCoords.contains(key) && !occupiedEdges.contains(key)) {
                    checkedCoords.add(key);
                    Edge neighbor = new Edge(nx, ny);
                    queue.add(neighbor);
                    fromCoordsWithEdges.put(key, current);
                }
            }
        }

        return new ArrayList<>();
    }

    private List<Edge> buildPath(Map<String, Edge> fromCoordsWithEdges, Edge start, Edge target) {
        List<Edge> path = new ArrayList<>();
        Edge current = target;
        while (current != null && !(current.getX() == start.getX() && current.getY() == start.getY())) {
            path.add(current);
            current = fromCoordsWithEdges.get(coordToString(current));
        }
        Collections.reverse(path);
        return path;
    }

    private String coordToString(Edge e) {
        return e.getX() + ":" + e.getY();
    }
}