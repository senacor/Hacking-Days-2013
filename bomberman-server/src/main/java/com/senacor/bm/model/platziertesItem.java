package com.senacor.bm.model;

/**
 * Created by mmenzel on 21.03.2014.
 */
public class PlatziertesItem {
    private Position position;

    private Itemtyp itemtyp;

    public PlatziertesItem(Position position, Itemtyp itemtyp) {
        this.position = position;
        this.itemtyp = itemtyp;
    }
}
