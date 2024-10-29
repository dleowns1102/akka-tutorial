package com.example.actor;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.routing.RoundRobinPool;

public class PingActor extends UntypedActor {
    private LoggingAdapter log = Logging.getLogger(getContext().system(), this);
    private ActorRef childRouter;

    public PingActor() {
        childRouter = getContext().actorOf(
                new RoundRobinPool(5).props(Props.create(Ping1Actor.class)),
                "Ping1Actor");
    }

    @Override
    public void onReceive(Object message) throws Exception {
        if (message instanceof String) {
            for (int i = 0; i < 10; i++) {
                childRouter.tell(i, getSelf());
            }
            log.info("PingActor sent 10 messages to the router.");
        } else {
            unhandled(message);
        }
    }

}
