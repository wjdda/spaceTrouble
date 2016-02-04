package com.divanstudio.firsttry.ST;

/**
 * Created by aaivanov on 12/22/15.
 */
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
