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
    canvas = document.getElementById("canvas");
    ctx = canvas.getContext('2d');
    timer=setInterval(draw, 10);
    return timer;
}

function draw(){
    ctx.clearRect(0, 0, canvas.width, canvas.height);
    ctx.fillStyle = "#773333";
    ctx.fillRect(0, 0, canvas.width, canvas.height);
    ctx.drawImage(img, x, y, width, height);
}

function handlePressedKey(event) {
    event = event || window.event;

    //left arrow
    if (event.keyCode == 37 && x > 10) {
        x-=20;
    }
    //up arrow
    else if (event.keyCode == 38 && y > 10) {
        y-=20;
    }
    //right_arrow
    else if (event.keyCode == 39 && x < 441) {
        x+=20;
    }
    //down_arrow
    else if (event.keyCode == 40 && y < 421) {
        y+=20;
    }
}
