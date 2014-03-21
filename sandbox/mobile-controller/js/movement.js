var canvas;
var ctx;

//movement variables
var my=0;
var mx=0;
var x=10;
var y=20;

var width=50;
var height=50;

var img = new Image();

//set an image url
img.src = "img/Bomberman.gif"

function init() {
    window.addEventListener("keydown", handlePressedKey, false);
    canvas = document.getElementById("canvas");
    ctx = canvas.getContext('2d');
    timer=setInterval(draw, 200);
    img.sprite = createSprite(4, [0], false);
    return timer;
}

function draw(){
    ctx.clearRect(0, 0, canvas.width, canvas.height);
    ctx.fillStyle = "#773333";
    ctx.fillRect(0, 0, canvas.width, canvas.height);
    img.sprite.update(1 );
    img.sprite.canvasPos = [x, y];
    img.sprite.render(ctx);
}

function handlePressedKey(event) {
    //left arrow
    if (event.keyCode == 37 && x > 10) {
        x-=20;
        img.sprite = createSprite(4, [8, 9, 8, 7], true)
    }
    //up arrow
    else if (event.keyCode == 38 && y > 10) {
        y-=20;
        img.sprite = createSprite(5, [0, 1, 2], true)

    }
    //right_arrow
    else if (event.keyCode == 39 && x < 441) {
        x+=20;
        img.sprite = createSprite(5, [7, 8, 9], true)

    }
    //down_arrow
    else if (event.keyCode == 40 && y < 421) {
        y+=20;
        img.sprite = createSprite(4, [0, 1, 2], true)
    }
    //enter
    else if (event.keyCode == 13 ) {
        img.sprite = createSprite(6, [12, 13, 14], true)
    }
}

function createSprite(row, frames, playOnce){
     return new Sprite("img/bomber.png", [0, calcRow(row)], [26, 29], 1, frames, "horizontal", playOnce);
}

function calcRow(r){
    return r*29;
}
