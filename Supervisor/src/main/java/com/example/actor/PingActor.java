package com.example.actor;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class PingActor extends UntypedActor {
    private LoggingAdapter log = Logging.getLogger(getContext().system(), this);
    private ActorRef child;

    public PingActor() {
        child = getContext().actorOf(
                Props.create(Ping1Actor.class),
                "Ping1Actor");
    }

    @Override
    public void onReceive(Object message) throws Exception {
        if (message instanceof String) {
            if ("good".equals(message) || "bad".equals(message)) {
                child.tell(message, getSelf());
            } else if ("done".equals(message)) {
                log.info("all works are successfully completed.");
            }
        } else {
            unhandled(message);
        }
    }

}
