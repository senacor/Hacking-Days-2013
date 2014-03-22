import org.vertx.java.core.Handler;
import org.vertx.groovy.core.eventbus.EventBus;
import org.vertx.groovy.core.eventbus.Message;
import org.vertx.java.core.json.JsonObject;
import org.vertx.groovy.platform.Verticle;
import org.vertx.groovy.core.Vertx;


class EventLogger extends Verticle {

  def start() {
      println("Starting Groovy-based EventLogger ...");

      def host = container.env["OPENSHIFT_MONGODB_DB_HOST"];
      def port = container.env["OPENSHIFT_MONGODB_DB_PORT"];
      def username = container.env["OPENSHIFT_MONGODB_DB_USERNAME"];
      def password = container.env["OPENSHIFT_MONGODB_DB_PASSWORD"];

      // If Verticle is not deployed in Openshift...
      host = host == null ? "192.168.1.137" : host;
      port = port == null ? 27017 : port;

      def config = [host: host, port: port, db_name: "server", username: username, password: password];
      container.deployModule("io.vertx~mod-mongo-persistor~2.1.0", config);


      def eb = vertx.getEventBus();

      def myHandler = { message -> 
          println("I received a message " + message.body);
          def event = message.body()
          event.put("timestamp", new Date())
          def saveMessage = [action: "save", collection: "access", document: event];

          eb.send("vertx.mongopersistor", saveMessage);
          eb.publish("event.dashboard", event);
      }

      def captureReplayHandler = { message ->
          println("Received game state infomration: " + message.body)
          def saveMessage = [action: "save", collection: "capture", document: message.body]

          eb.send("vertx.mongopersistor", saveMessage)
      }

      eb.registerHandler("hd13.eventlogger", myHandler);
      eb.registerHandler("game.capture.state", captureReplayHandler)
  }

}