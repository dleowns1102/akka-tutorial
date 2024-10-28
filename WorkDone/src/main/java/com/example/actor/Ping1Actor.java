package com.example.actor;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class Ping1Actor extends UntypedActor {
    private LoggingAdapter log = Logging.getLogger(getContext().system(), this);
    private ActorRef child1, child2;

    public Ping1Actor() {
        child1 = getContext().actorOf(
                Props.create(Ping2Actor.class),
                "Ping2Actor");
        child2 = getContext().actorOf(
                Props.create(Ping3Actor.class),
                "Ping3Actor");
    }

    @Override
    public void onReceive(Object message) throws Exception {
        if (message instanceof String) {
            if ("work".equals(message)) {
                log.info("Ping1 received '{}'", message);
                child1.tell(message, getSender());
                child2.tell(message, getSender());
            }
        }
    }
}
