package com.senacor.bomberman.gameworld.model;

/**
 * Created by abremm on 21.03.14.
 */
public enum Feldart {

    LEER ("", false, true, true),
    WAND ("W", true, false, false),
    STEIN ("S", false, false, true),
    PORTAL ("P", false, false, false);

    private String id;
    private boolean isDestroyable;
    private boolean isAccessible;
    private boolean positionForPlayerPlacement;

    Feldart(String id, boolean destroyable, boolean accessible, boolean playerPlacement) {
        this.id = id;
        this.isDestroyable = destroyable;
        this.isAccessible = accessible;
        this.positionForPlayerPlacement = playerPlacement;
    }

    public static Feldart fromString(String feldId) {
        if(feldId.equals("W")) {
           return Feldart.WAND;
        } else if(feldId.equals("S")) {
            return Feldart.STEIN;
        } else if (feldId.equals("P")) {
            return Feldart.PORTAL;
        } else {
            return Feldart.LEER;
        }
    }

    @Override
    public String toString() {
        return id;
    }

    public boolean isDestroyable() {
        return isDestroyable;
    }

    public boolean isAccessible() {
        return isAccessible;
    }

    public boolean isPositionForPlayerPlacement() {
        return positionForPlayerPlacement;
    }
}
