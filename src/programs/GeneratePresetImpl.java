package programs;

import com.battle.heroes.army.Army;
import com.battle.heroes.army.Unit;
import com.battle.heroes.army.programs.GeneratePreset;

import java.util.*;

public class GeneratePresetImpl implements GeneratePreset {

    private static final int MAX_UNITS_AMOUNT_BY_TYPE = 11;
    private static final int WIDTH = 3;
    private static final int HEIGHT = 21;

    @Override
    public Army generate(List<Unit> unitList, int maxPoints) {
        Map<String, Integer> collectedUnitsAmountByType = new HashMap<>();
        int collectedPoints = 0;

        List<int[]> positions = new ArrayList<>();
        int posIndex = 0;

        Army army = new Army();
        List<Unit> calculatedUnits = new ArrayList<>();
        List<Unit> inputUnits = new ArrayList<>(unitList);

        if (unitList == null || unitList.isEmpty()) {
            army.setUnits(calculatedUnits);
            army.setPoints(0);
            return army;
        }

        inputUnits.sort((u1, u2) -> {
            double eff1 = efficiency(u1);
            double eff2 = efficiency(u2);
            return Double.compare(eff2, eff1);
        });

        for (Unit u : inputUnits) {
            int amountCanAdd = (maxPoints - collectedPoints) / u.getCost();
            int amountToAdd = Math.min(MAX_UNITS_AMOUNT_BY_TYPE, amountCanAdd);

            if (amountToAdd <= 0) continue;

            collectedUnitsAmountByType.put(u.getUnitType(), amountToAdd);
            collectedPoints += amountToAdd * u.getCost();
        }


        for (int y = 0; y < HEIGHT; y++) {
            for (int x = 0; x < WIDTH; x++) {
                positions.add(new int[]{x, y});
            }
        }
        Collections.shuffle(positions);


        for (Unit u : inputUnits) {
            int amount = collectedUnitsAmountByType.getOrDefault(u.getUnitType(), 0);

            for (int i = 1; i <= amount && posIndex < positions.size(); i++) {
                int[] pos = positions.get(posIndex++);

                Unit unit = new Unit(
                        u.getUnitType() + " " + i,
                        u.getUnitType(),
                        u.getHealth(),
                        u.getBaseAttack(),
                        u.getCost(),
                        u.getAttackType(),
                        u.getAttackBonuses(),
                        u.getDefenceBonuses(),
                        pos[0],
                        pos[1]
                );

                unit.setAlive(true);
                unit.setProgram(u.getProgram());

                calculatedUnits.add(unit);
            }
        }

        army.setUnits(calculatedUnits);
        army.setPoints(collectedPoints);
        return army;
    }

    private double efficiency(Unit u) {
        return ((double) u.getBaseAttack() / u.getCost()) * 2
                + ((double) u.getHealth() / u.getCost());
    }
}