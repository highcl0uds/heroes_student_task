package programs;

import com.battle.heroes.army.Unit;
import com.battle.heroes.army.programs.SuitableForAttackUnitsFinder;

import java.util.ArrayList;
import java.util.List;

public class SuitableForAttackUnitsFinderImpl implements SuitableForAttackUnitsFinder {

    @Override
    public List<Unit> getSuitableUnits(List<List<Unit>> unitsByRow, boolean isLeftArmyTarget) {
        List<Unit> result = new ArrayList<>();

        for (List<Unit> rowOfUnits : unitsByRow) {
            if (rowOfUnits == null || rowOfUnits.isEmpty()) continue;

            Unit unitToAdd = null;

            for (Unit unit : rowOfUnits) {
                if (unit == null || !unit.isAlive()) continue;

                if (unitToAdd == null) {
                    unitToAdd = unit;
                } else {
                    if (isLeftArmyTarget) {
                        if (unit.getyCoordinate() < unitToAdd.getyCoordinate()) {
                            unitToAdd = unit;
                        }
                    } else {
                        if (unit.getyCoordinate() > unitToAdd.getyCoordinate()) {
                            unitToAdd = unit;
                        }
                    }
                }
            }

            if (unitToAdd != null) {
                result.add(unitToAdd);
            }
        }

        return result;
    }
}