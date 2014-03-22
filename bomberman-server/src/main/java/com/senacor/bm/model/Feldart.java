package com.senacor.bm.model;

/**
 * Created by abremm on 21.03.14.
 */
public enum Feldart {

    LEER (""),
    WAND ("W"),
    STEIN ("S"),
    PORTAL ("P");

    private String id;

    Feldart(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return id;
    }
}
