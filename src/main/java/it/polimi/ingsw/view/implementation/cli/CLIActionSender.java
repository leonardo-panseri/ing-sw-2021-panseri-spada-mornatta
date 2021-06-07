package it.polimi.ingsw.view.implementation.cli;

import it.polimi.ingsw.constant.ViewString;
import it.polimi.ingsw.model.Resource;
import it.polimi.ingsw.model.card.DevelopmentCard;
import it.polimi.ingsw.model.card.LeaderCard;
import it.polimi.ingsw.view.ActionSender;
import it.polimi.ingsw.view.View;

import java.util.List;

public class CLIActionSender extends ActionSender {
    public CLIActionSender(View view) {
        super(view);
    }

    @Override
    public void useLeaderProduction(LeaderCard leaderCard, Resource desiredResource) {
        try {
            super.useLeaderProduction(leaderCard, desiredResource);
        } catch (IllegalArgumentException e) {
            getView().getRenderer().showErrorMessage(e.getMessage());
        }
        getView().getRenderer().showGameMessage(ViewString.PRODUCTION_QUEUED);
    }

    @Override
    public void useDevelopmentProduction(DevelopmentCard developmentCard) {
        try {
            super.useDevelopmentProduction(developmentCard);
        } catch (IllegalArgumentException e) {
            getView().getRenderer().showErrorMessage("This development card slot is empty");
        }
        getView().getRenderer().showGameMessage(ViewString.PRODUCTION_QUEUED);
    }

    @Override
    public void useBaseProduction(List<Resource> inputResource, List<Resource> outputResource) {
        super.useBaseProduction(inputResource, outputResource);
        getView().getRenderer().showGameMessage(ViewString.PRODUCTION_QUEUED);
    }


    @Override
    public void executeProductions() {
        try {
            super.executeProductions();
        } catch (IllegalArgumentException e) {
            getView().getRenderer().showErrorMessage("There are no productions in the queue");
        }
    }
}

