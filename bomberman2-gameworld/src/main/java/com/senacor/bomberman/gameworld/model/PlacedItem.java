package com.senacor.bomberman.gameworld.model;

/**
 * Created by mmenzel on 21.03.2014.
 */
public class PlacedItem {
    private Position position;

    private Itemtyp itemtyp;

    public PlacedItem(Position position, Itemtyp itemtyp) {
        this.position = position;
        this.itemtyp = itemtyp;
    }
}
