package bookshop.others;

import akka.actor.ActorRef;
import shared.ResponseType;

import java.io.Serializable;

public class FindResult implements Serializable{
    private ResponseType type;
    private String result;
    private ActorRef sender;

    public FindResult(ResponseType type, String result, ActorRef sender) {
        this.type = type;
        this.result = result;
        this.sender = sender;
    }

    public ResponseType getType() {
        return type;
    }

    public String getResult() {
        return result;
    }

    public ActorRef getSender() {
        return sender;
    }
}
