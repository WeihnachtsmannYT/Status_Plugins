package de.weihnachtsmannyt.afkapi.Tasks;

import de.weihnachtsmannyt.afkapi.Managers.AfkManager;

public class MovementChecker implements Runnable {

    private final AfkManager afkManager;

    public MovementChecker(AfkManager afkManager) {
        this.afkManager = afkManager;
    }

    @Override
    public void run() {

        //System.out.println("AFK Status for each player is being checked");
        afkManager.checkAllPlayersAFKStatus();

    }

}
