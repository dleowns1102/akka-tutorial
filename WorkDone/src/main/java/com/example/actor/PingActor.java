package com.example.actor;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class PingActor extends UntypedActor {
    private LoggingAdapter log = Logging.getLogger(getContext().system(), this);
    private ActorRef child;
    private int count = 0;

    public PingActor() {
        child = getContext().actorOf(
                Props.create(Ping1Actor.class),
                "Ping1Actor");
    }

    @Override
    public void onReceive(Object message) throws Exception {
        if (message instanceof String) {
            if ("work".equals(message)) {
                child.tell(message, getSelf());
            } else if ("done".equals(message)) {
                if (count == 0) {
                    count++;
                } else {
                    log.info("all works are completed.");
                    count = 0;
                }
            }
        }
    }

}
