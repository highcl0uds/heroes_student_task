package programs;

import com.battle.heroes.army.Army;
import com.battle.heroes.army.Unit;
import com.battle.heroes.army.programs.PrintBattleLog;
import com.battle.heroes.army.programs.SimulateBattle;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class SimulateBattleImpl implements SimulateBattle {

    private PrintBattleLog printBattleLog;  // Позволяет логировать. Использовать после каждой атаки юнита

    public void setPrintBattleLog(PrintBattleLog printBattleLog) {
        this.printBattleLog = printBattleLog;
    }

    @Override
    public void simulate(Army playerArmy, Army computerArmy) throws InterruptedException {
        int alivePlayerCount = countAlive(playerArmy);
        int aliveComputerCount = countAlive(computerArmy);

        while (alivePlayerCount > 0 && aliveComputerCount > 0) {
            List<Unit> allUnits = new ArrayList<>();

            addAliveUnits(allUnits, playerArmy);
            addAliveUnits(allUnits, computerArmy);

            allUnits.sort(Comparator.comparingInt(Unit::getBaseAttack).reversed());

            for (Unit unit : allUnits) {
                if (!unit.isAlive() || unit.getProgram() == null) continue;

                Unit target = unit.getProgram().attack();
                if (printBattleLog != null) {
                    printBattleLog.printBattleLog(unit, target);
                }

                if (target != null && !target.isAlive()) {
                    if (playerArmy.getUnits().contains(target)) {
                        alivePlayerCount--;
                    } else {
                        aliveComputerCount--;
                    }
                }

                if (alivePlayerCount == 0 || aliveComputerCount == 0) {
                    break;
                }
            }
        }
    }

    private int countAlive(Army army) {
        int count = 0;
        for (Unit u : army.getUnits()) {
            if (u != null && u.isAlive()) count++;
        }
        return count;
    }

    private void addAliveUnits(List<Unit> units, Army army) {
        if (army == null || army.getUnits() == null) return;
        for (Unit u : army.getUnits()) {
            if (u != null && u.isAlive()) {
                units.add(u);
            }
        }
    }
}