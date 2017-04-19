package com.divanstudio.spaceTrouble;

/**
 * Created by Lik on 19.04.2017.
 */
public class globalBitmapInfo {
    private static volatile globalBitmapInfo instance;

    public static final int PLAYER_SPEED_COEFFICIENT = 4;
    public static final int PLAYER_IMG_SIZE_COEFFICIENT = 20;
    public static final int PLAYER_BMP_ROWS = 1;
    public static final int PLAYER_BMP_COLUMNS = 1;

    public static final int ENEMY_BMP_ROWS = 4;
    public static final int ENEMY_BMP_COLUMNS = 1;
    public static final int ENEMY_IMG_SIZE_COEFFICIENT = 25;

    public static final int NAV_CONTROLS_BMP_ROWS = 1;
    public static final int NAV_CONTROLS_BMP_COLUMNS = 4;

    public static globalBitmapInfo getInstance() {
        globalBitmapInfo localInstance = instance;
        if (localInstance == null) {
            synchronized (globalBitmapInfo.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new globalBitmapInfo();
                }
            }
        }
        return  localInstance;
    }

    private globalBitmapInfo () {}

}

