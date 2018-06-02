package bookshop;

import akka.actor.AbstractActor;
import akka.actor.OneForOneStrategy;
import akka.actor.Props;
import akka.actor.SupervisorStrategy;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.japi.pf.DeciderBuilder;
import bookshop.actors.FindActor;
import bookshop.actors.OrderActor;
import scala.concurrent.duration.Duration;
import shared.Request;
import shared.RequestType;
import shared.Response;
import shared.ResponseType;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.Writer;

public class RemoteManager extends AbstractActor {

    private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(Request.class, request -> {
                        if (request.getRequestType() == RequestType.ORDER) {
                            context().child("order").get().tell(request, getSender());
                        } else if (request.getRequestType() == RequestType.FIND) {
                            context().child("find").get().tell(request, getSender());
                        }})
                .matchAny(o -> log.info("Received unknown message"))
                .build();
    }

    @Override
    public void preStart() {
        context().actorOf(Props.create(FindActor.class), "find");
        context().actorOf(Props.create(OrderActor.class), "order");
    }

    private static SupervisorStrategy strategy
            = new OneForOneStrategy(10, Duration.create("1 minute"), DeciderBuilder
            .matchAny(o -> SupervisorStrategy.restart())
            .build());
    @Override
    public SupervisorStrategy supervisorStrategy() {
        return strategy;
    }
}
