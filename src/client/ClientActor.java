package client;

import akka.actor.AbstractActor;
import akka.actor.AllForOneStrategy;
import akka.actor.SupervisorStrategy;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.japi.pf.DeciderBuilder;
import scala.concurrent.duration.Duration;
import shared.Request;
import shared.RequestType;
import shared.ResponseType;
import shared.Response;

import java.util.Arrays;

import static akka.actor.SupervisorStrategy.restart;

public class ClientActor extends AbstractActor {

    private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);
    private String path = "akka.tcp://bookshop_system@127.0.0.1:3552/user/bookshop";

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(String.class, s -> {
                    Request request;
                    if (s.startsWith("find")) {
                        RequestType requestType = RequestType.FIND;
                        request = new Request(getTitle(s), requestType);
                    }
                    else if (s.startsWith("order")) {
                        RequestType requestType = RequestType.ORDER;
                        request = new Request(getTitle(s), requestType);
                    } else {
                        return;
                    }
                    getContext().actorSelection(path).tell(request, getSelf());
                })
                .match(Response.class, response -> {
                    if (response.getType() == ResponseType.FIND) {
                        if (response.getResult().equals("")) {
                            System.out.println("Book not found");
                        }
                        else {
                            System.out.println("Price: " + response.getResult());
                        }
                    }
                    else if (response.getType() == ResponseType.ORDER) {
                        System.out.println("Result: " + response.getResult());
                    }
                })
                .matchAny(o -> {
                    System.out.println(o);
                    log.info("Received unknown message");
                })
                .build();
    }

    public static String getTitle(String line) {
        String[] splited = line.split(" ");
        return String.join(" ", Arrays.copyOfRange(splited, 1, splited.length));
    }

    private static SupervisorStrategy strategy
            = new AllForOneStrategy(10, Duration.create("1 minute"), DeciderBuilder.
            matchAny(o -> restart()).
            build());

    @Override
    public SupervisorStrategy supervisorStrategy() {
        return strategy;
    }
}
