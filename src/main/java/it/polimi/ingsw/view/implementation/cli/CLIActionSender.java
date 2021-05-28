package it.polimi.ingsw.view.implementation.cli;

import it.polimi.ingsw.constant.ViewString;
import it.polimi.ingsw.model.Resource;
import it.polimi.ingsw.model.card.CardColor;
import it.polimi.ingsw.model.card.DevelopmentCard;
import it.polimi.ingsw.model.card.LeaderCard;
import it.polimi.ingsw.model.card.SpecialAbilityType;
import it.polimi.ingsw.view.ActionSender;
import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.beans.MockDeposit;
import it.polimi.ingsw.view.messages.*;
import it.polimi.ingsw.view.messages.production.BaseProduction;
import it.polimi.ingsw.view.messages.production.DevelopmentProduction;
import it.polimi.ingsw.view.messages.production.LeaderProduction;

import java.util.*;

public class CLIActionSender extends ActionSender {
    public CLIActionSender(View view) {
        super(view);
    }

    @Override
    public void useLeaderProduction(int cardIndex, Resource desiredResource) {
        try {
            super.useLeaderProduction(cardIndex, desiredResource);
        } catch (IllegalArgumentException e) {
            if(e.getMessage().equals("not_active")) {
                getView().getRenderer().showErrorMessage("This leader card is not active");
            } else {
                getView().getRenderer().showErrorMessage("This leader card does not have a production ability");
            }
        }
        getView().getRenderer().showGameMessage(ViewString.PRODUCTION_QUEUED);
    }

    @Override
    public void useDevelopmentProduction(int cardIndex) {
        try {
            super.useDevelopmentProduction(cardIndex);
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
