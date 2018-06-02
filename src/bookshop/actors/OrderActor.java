package bookshop.actors;

import akka.actor.AbstractActor;
import akka.actor.AllForOneStrategy;
import akka.actor.SupervisorStrategy;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.japi.pf.DeciderBuilder;
import scala.concurrent.duration.Duration;
import shared.Request;
import shared.ResponseType;
import shared.Response;

import java.io.*;

import static akka.actor.SupervisorStrategy.restart;
import static shared.RequestType.FIND;
import static shared.RequestType.ORDER;

public class OrderActor extends AbstractActor {
    private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(Request.class, request -> {
                    Writer output;
                    output = new BufferedWriter(new FileWriter("database/orders.txt", true));
                    output.append(request.getTitle() + "\n");
                    output.close();
                    getSender().tell(new Response(ResponseType.ORDER, "Successfully ordered"), getSelf());
                })
                .matchAny(o -> log.info("Received unknown message"))
                .build();
    }
}

