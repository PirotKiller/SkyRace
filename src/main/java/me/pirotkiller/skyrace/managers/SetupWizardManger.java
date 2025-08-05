package me.pirotkiller.skyrace.managers;

import me.pirotkiller.skyrace.states.SetupState;
import org.bukkit.World;

public class SetupWizardManger {

    public SetupState setupState = SetupState.DISABLED;
    public World playerWorldBefore;
    public World playerWorldAfter;

    public World getPlayerWorldAfter() {
        return playerWorldAfter;
    }

    public void setPlayerWorldAfter(World playerWorldAfter) {
        this.playerWorldAfter = playerWorldAfter;
    }

    public SetupState getSetupState() {
        return setupState;
    }

    public void setSetupState(SetupState setupState) {
        if (setupState == this.setupState) {
            return;
        }
        this.setupState = setupState;
    }
}
