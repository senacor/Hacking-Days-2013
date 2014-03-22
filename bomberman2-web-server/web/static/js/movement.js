var canvas;
var ctx;

var drawIntervalInMs = 20;
var turnLengthInMs = 40;

var drawsSinceLastUpdate = 0;
var drawsRequiredForUpdate = turnLengthInMs/drawIntervalInMs;

//movement variables
var x=64;
var y=64;

var width=50;
var height=50;

var img = new Image();
var wall = new Image();
var wood = new Image();

var board;
var lastKey;
var bombSet = false;
var playerName;
var gameStarted = false;
var gameId;
var actuelField;
var step = 65;

//set an image url
img.src = "static/img/bomberman_2.gif";
wall.src = "static/img/wall.png";
wood.src = "static/img/wood.png";

function init(reply) {
    canvas = document.getElementById("canvas");
    ctx = canvas.getContext('2d');
    canvaswrapper = document.getElementById("canvaswrapper");
    canvaswrapper.addEventListener("keydown", handlePressedKey, false);
    canvas.addEventListener("click", focusCanvas, false);
    timer = setInterval(draw, drawIntervalInMs);
    //log the reply from gameworld
    var jsonString = JSON.stringify(reply);
    console.log(jsonString);

    var jsonBoard = reply.map;
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

    var players = reply.player;
    x *= players[0].position.x;
    y *= players[0].position.y;

    playerName = players[0].name;

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
    drawsSinceLastUpdate += 1;

    if(gameStarted && (drawsSinceLastUpdate === drawsRequiredForUpdate + 1)){
        bus.send("game." + playerId + ".move", new PlayerState(lastKey, bombSet));
    }
}

function drawBoard(ctx){
    for(i=0; i<board.tiles.length; i++){

        for(j=0; j<board.tiles[i].length; j++){
            tile = board.tiles[i][j];
            if(tile.image && tile.image.src )
                ctx.drawImage(tile.image, i*step, j*step4);
        }
    }
}

function handlePressedKey(event) {
	event.preventDefault();
    if(!img.sprite.done)
        return false;
    if(!gameStarted)
        return false;

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
  return false;
}

function focusCanvas() {
	canvaswrapper.focus();
}

function createSprite(row, frames, playOnce){
     return new Sprite(img.src, [0, calcRow(row)], [17.55, 30], 1, frames, "horizontal", playOnce, img);
}

function calcRow(r){
    return r*30;
}

function stepIsPossible(stepX, stepY) {
  actuelField = getActuellField();
  if (jsonBoard.felder[actuelField.xt + stepX][actuelField.yt + stepY] == "W")
    return false;
  else return true;
}

function getActuellField() {
  var xt = Math.floor(x/step);
  var yt = Math.floor(y/step);
  return new Field(xt, yt)
}

var Field = function(xt, yt) {
  this.xt = xt;
  this.yt = yt;
}

