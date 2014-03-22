var canvas;
var ctx;

//TODO replace by vert.x callback
var jsonObject = '{"width":11,"height":11,"felder":[["W","W","W","W","W","W","W","W","W","W","W"],["W","","","S","","S","S","","S","S","W"],["W","","W","S","W","","W","S","W","S","W"],["W","S","S","","S","S","S","S","S","","W"],["W","S","W","","W","S","W","S","W","","W"],["W","S","S","S","S","S","S","S","S","S","W"],["W","S","W","S","W","S","W","","W","S","W"],["W","S","","S","S","S","","S","S","S","W"],["W","S","W","S","W","S","W","S","W","S","W"],["W","","S","S","","S","S","S","S","S","W"],["W","W","W","W","W","W","W","W","W","W","W"]]}';
var jsonBoard = eval ("(" + jsonObject + ")");

//movement variables
var my=0;
var mx=0;
var x=75;
var y=65;

var width=50;
var height=50;

var img = new Image();

var wall = new Image();
var wood = new Image();

var board;

var lastKey;
var bombSet = false;

var actuelField;

var step = 65;

//set an image url
img.src = "static/img/bomberman_2.gif";
wall.src = "static/img/wall.png";
wood.src = "static/img/wood.png";

function init() {
    window.addEventListener("keydown", handlePressedKey, false);
    canvas = document.getElementById("canvas");
    ctx = canvas.getContext('2d');
    timer=setInterval(draw, 200);

    var boardW = jsonBoard.width;
    var boardH = jsonBoard.height;

    board = new Board(boardW, boardH);

    for(i=0; i<jsonBoard.felder.length; i++){
        for(j=0; j<jsonBoard.felder[i].length; j++){
            if(jsonBoard.felder[i][j] == "W")
                board.tiles[i][j].image = wall;
            if(jsonBoard.felder[i][j] == "S")
                board.tiles[i][j].image = wood;
        }
    }

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
  //var movePossible = stepIsPossible();
  //if (stepIsPossible) {
    if (event.keyCode == 37 && x > 10 && stepIsPossible(-1, 0)) {
      x -= step;
      lastKey = "W";
      img.sprite = createSprite(0, [10, 11, 9], true)
    }
    //up arrow
    else if (event.keyCode == 38 && y > 10 && stepIsPossible(0, -1)) {
      y -= step;
      lastKey = "U";
      img.sprite = createSprite(0, [1, 2, 0], true)

    }
    //right_arrow
    else if (event.keyCode == 39 && x < 640 && stepIsPossible(1, 0)) {
      x += step;
      lastKey = "E";
      img.sprite = createSprite(0, [4, 5, 3], true)

    }
    //down_arrow
    else if (event.keyCode == 40 && y < 640 && stepIsPossible(0, 1)) {
      y += step;
      lastKey = "D";
      img.sprite = createSprite(0, [7, 8, 6], true)
    }
  //enter
    else if (event.keyCode == 13) {
    bombSet = true;
    img.sprite = createSprite(5, [12, 13, 14], true)
  }
}

function createSprite(row, frames, playOnce){
     return new Sprite(img.src, [0, calcRow(row)], [17.55, 30], 1, frames, "horizontal", playOnce, img);
}

function calcRow(r){
    return r*30;
}

function stepIsPossible(stepX, stepY) {
  actuelField = getActuelField();
  if (jsonBoard.felder[actuelField.xt + stepX][actuelField.yt + stepY] == "W")
    return false;
  else return true;
}

function getActuelField() {
  var xt = Math.floor(x/step);
  var yt = Math.floor(y/step);
  return new Field(xt, yt)
}

var Field = function(xt, yt) {
  this.xt = xt;
  this.yt = yt;
}

