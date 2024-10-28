package com.example.actor;

import akka.actor.ActorRef;
import akka.actor.OneForOneStrategy;
import akka.actor.Props;
import akka.actor.SupervisorStrategy;
import akka.actor.SupervisorStrategy.Directive;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.japi.Function;
import scala.concurrent.duration.Duration;

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
            if ("good".equals(message) || "bad".equals(message)) {
                log.info("Ping1Actor received '{}'", message);
                child1.tell(message, getSender());
                child2.tell(message, getSender());
            }
        } else {
            unhandled(message);
        }
    }

    private SupervisorStrategy strategy = new OneForOneStrategy(10, Duration.create("1 minute"),
            new Function<Throwable, SupervisorStrategy.Directive>() {

                @Override
                public Directive apply(Throwable t) throws Exception {
                    if (t instanceof ArithmeticException) {
                        return SupervisorStrategy.resume();
                    } else if (t instanceof NullPointerException) {
                        return SupervisorStrategy.restart();
                    } else if (t instanceof IllegalArgumentException) {
                        return SupervisorStrategy.stop();
                    } else {
                        return SupervisorStrategy.escalate();
                    }
                }

            });

    @Override
    public SupervisorStrategy supervisorStrategy() {
        return strategy;
    }
}