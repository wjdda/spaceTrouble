package com.divanstudio.spaceTrouble;

/**
 * Created by aaivanov on 12/22/15.
 */

// Класс, который определяет состояние игры
public class State {
    private static volatile State instance;

    private String state = new String();

    public static State getInstance() {
        State localInstance = instance;
        if (localInstance == null) {
            synchronized (State.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new State();
                }
            }
        }
        return  localInstance;
    }

    private State () {
        this.state = "Menu";
    }

    public void setState ( String state ) {
        this.state = state;
    }

    public String getState () {
        return this.state;
    }

}
