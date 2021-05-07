package it.polimi.ingsw.view.messages.production;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.model.Resource;
import it.polimi.ingsw.model.player.Player;

import java.io.Serial;
import java.util.List;
import java.util.Map;

public class BaseProduction extends Production {
    @Serial
    private static final long serialVersionUID = 348693205829788376L;

    private final List<Resource> baseProductionInput;
    private final List<Resource> baseProductionOutput;

    public BaseProduction(List<Resource> baseProductionInput, List<Resource> baseProductionOutput) {
        this.baseProductionInput = baseProductionInput;
        this.baseProductionOutput = baseProductionOutput;
    }

    @Override
    public Map<Resource, Integer> use(GameController controller, Player player) {
        return controller.getPlayerController().useBaseProduction(player, baseProductionInput, baseProductionOutput);
    }

    @Override
    public String toString() {
        return "base production (" +
                "input=" + baseProductionInput +
                ", output=" + baseProductionOutput +
                ')';
    }
}
