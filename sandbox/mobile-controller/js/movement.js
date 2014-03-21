var canvas;
var ctx;

//movement variables
var my=0;
var mx=0;
var x=64;
var y=64;

var width=50;
var height=50;

var img = new Image();

var wall = new Image();
var wood = new Image();

var board;

//set an image url
img.src = "img/bomberman_2.gif";
wall.src = "img/wall.png";
wood.src = "img/wood.png";

function init() {
    window.addEventListener("keydown", handlePressedKey, false);
    canvas = document.getElementById("canvas");
    ctx = canvas.getContext('2d');
    timer=setInterval(draw, 200);

    board = new Board(10, 10);
    board.tiles[0][0].image = wall;
    board.tiles[0][1].image = wall;
    board.tiles[1][0].image = wall;

    img.sprite = createSprite(1, [0], true);

    return timer;
}

function draw(){
    ctx.clearRect(0, 0, canvas.width, canvas.height);
    ctx.fillStyle = "#A37547";
    ctx.fillRect(0, 0, canvas.width, canvas.height);

    drawBoard(ctx);

    img.sprite.update(1);
    img.sprite.canvasPos = [x, y];
    img.sprite.render(ctx);
    img.sprite.done = true;
}

function drawBoard(ctx){
    for(i=0; i<board.tiles.length; i++){

        for(j=0; j<board.tiles[i].length; j++){
            tile = board.tiles[i][j];
            if(tile.image && tile.image.src )
                ctx.drawImage(tile.image, i*64, j*64);
        }
    }
}

function handlePressedKey(event) {
    if(!img.sprite.done)
        return;

    //left arrow
    if (event.keyCode == 37 && x > 10) {
        x-=20;
        img.sprite = createSprite(0, [10, 11, 9], true)
    }
    //up arrow
    else if (event.keyCode == 38 && y > 10) {
        y-=20;
        img.sprite = createSprite(0, [1, 2, 0], true)

    }
    //right_arrow
    else if (event.keyCode == 39 && x < 640) {
        x+=20;
        img.sprite = createSprite(0, [4, 5, 3], true)

    }
    //down_arrow
    else if (event.keyCode == 40 && y < 640) {
        y+=20;
        img.sprite = createSprite(0, [7, 8, 6], true)
    }
    //enter
    else if (event.keyCode == 13 ) {
        img.sprite = createSprite(5, [12, 13, 14], true)
    }
}

function createSprite(row, frames, playOnce){
     return new Sprite("img/bomberman_2.gif", [0, calcRow(row)], [17.55, 30], 1, frames, "horizontal", playOnce);
}

function calcRow(r){
    return r*30;
}
