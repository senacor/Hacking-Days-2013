require(["vertxbus","sockjs","jquery"], function() {
    var eb = new vertx.EventBus('http://localhost:9000/eventbus');

    eb.onopen = function() {
        eb.send('web.fullmap', {}, function(message) {
            console.log('received a message: ' + message);
            $("#replaceable").replaceWith(message)
        });

        eb.send('ping', {}, function(message) {
            console.log('PING')
            console.log(message.body)
            console.log('PING')
        });

    }
});