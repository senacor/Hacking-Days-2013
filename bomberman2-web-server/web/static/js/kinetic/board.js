
function initBoard(){
    var stage = new Kinetic.Stage({
        container: 'board',
        width: 804,
        height: 804
    });

    var layer = new Kinetic.Layer();

    var rect = new Kinetic.Rect({
        x: 10,
        y: 10,
        width: 802,
        height: 802,
        fill: 'brown',
        stroke: 'black',
        strokeWidth: 2
    });

    // add the shape to the layer
    layer.add(rect);
    // add the layer to the stage
    stage.add(layer);

}

function drawLine(coords){
    return new Kinetic.Line({
        points: coords,
        stroke: 'black',
        strokeWidth: 1
    })
}