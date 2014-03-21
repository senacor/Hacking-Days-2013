package com.senacor.bm.model;

public class Spielfeld {

    private int width = 10;
    private int height = 10;
    private Feldart[][] felder;

    public Spielfeld(int width, int height) {
        this.width = width;
        this.height = height;
        this.felder = new Feldart[width][height];
    }

    /**
     *
     * @param x verticale Position.
     * @param y horizontale Position.
     * @param feldart Die Art des Feldes.
     */
    public void setFeld(int x, int y, Feldart feldart) {
        felder[x][y] = feldart;
    }

    /**
     * @param x verticale Position.
     * @param y horizontale Position.
     * @return Die Art des Feldes.
     */
    public Feldart getFeld(int x, int y) {
        return felder[x][y];
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}




