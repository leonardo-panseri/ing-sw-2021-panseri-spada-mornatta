package it.polimi.ingsw.view.messages.production;

import it.polimi.ingsw.model.Resource;

import java.io.Serial;
import java.util.List;

public class BaseProduction extends Production {
    @Serial
    private static final long serialVersionUID = 348693205829788376L;

    private final List<Resource> baseProductionInput;
    private final Resource baseProductionOutput;

    public BaseProduction(List<Resource> baseProductionInput, Resource baseProductionOutput) {
        this.baseProductionInput = baseProductionInput;
        this.baseProductionOutput = baseProductionOutput;
    }

    public List<Resource> getBaseProductionInput() {
        return baseProductionInput;
    }

    public Resource getBaseProductionOutput() {
        return baseProductionOutput;
    }
}
