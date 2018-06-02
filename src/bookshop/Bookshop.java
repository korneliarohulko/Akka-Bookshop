package bookshop;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;

public class Bookshop {

    public static void main(String[] args) throws Exception {

        // Config
        File configFile = new File("server.conf");
        Config config = ConfigFactory.parseFile(configFile);

        // Create actor system & actors
        final ActorSystem system = ActorSystem.create("bookshop_system", config);

        final ActorRef remoteManager = system.actorOf(Props.create(RemoteManager.class), "bookshop");
        
        // Interaction
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        while (true) {
            String line = br.readLine();
            if (line.equals("q")) {
                break;
            }
        }

        system.terminate();
    }
}