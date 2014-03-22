function Tile(x1, y1){
    this.x = x1;
    this.y = y1;
    this.image = new Image();
}

function Board(x, y){

    this.tiles = new Array();

    for(i=0; i<x; i++){
        column = new Array();
        this.tiles[i] = column;

        for(j=0; j<y; j++){
            column[j] = new Tile(i, j);
        }
    }
}
