package com.divanstudio.spaceTrouble;

/**
 * Created by aaivanov on 12/22/15.
 */

// Класс, который определяет состояние игры
public class StateManager {
    private static volatile StateManager instance;


    public enum States { MENU,  PLAY, PAUSE, GAMEOVER };

    private States state;
    private String musicState = new String();

    public static StateManager getInstance() {
        StateManager localInstance = instance;
        if (localInstance == null) {
            synchronized (StateManager.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new StateManager();
                }
            }
        }
        return  localInstance;
    }

    private StateManager () {
        this.state = States.MENU;
    }

    public void setState ( States state ) {
        this.state = state;
    }

    public States getState () { return this.state; }

}
