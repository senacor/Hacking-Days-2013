var messages = [];

function initBus() {
     console.log("bus connection is now open - registering handler for test.address ...");

     bus.registerHandler("test.address", function(message) {
        var msg = JSON.stringify(message);
        console.log("received a message: " + msg);
        messages = [ msg ].concat(messages);
        messages = messages.slice(0, 5);
        for (i=0; i<messages.length; i++) {
            var entryName = "#entry"+(i+1);
            var entryValue = messages[i];
            $(entryName).html(entryValue);
     } });
 }