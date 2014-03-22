package com.senacor.bm.model;

/**
 * Created by abremm on 21.03.14.
 */
public enum Feldart {

    LEER ("", false, true, true),
    WAND ("W", true, false, true),
    STEIN ("S", false, false, false),
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
        switch (feldId) {
            case "W": return Feldart.WAND;
            case "S": return Feldart.STEIN;
            case "P": return Feldart.PORTAL;
            case " ":
            default: return Feldart.LEER;
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
