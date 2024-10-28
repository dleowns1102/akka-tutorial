package com.example.actor;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class PingActor extends UntypedActor {
    private LoggingAdapter log = Logging.getLogger(getContext().system(), this);
    private ActorRef pong;

    public PingActor() {
        pong = getContext().actorOf(
                Props.create(PongActor.class, getSelf()),
                "PongActor");
    }

    @Override
    public void onReceive(Object message) throws Exception {
        if (message instanceof String) {
            log.info("PingActor received '{}'", message);
            pong.tell("ping", getSelf());
        }
    }
}
