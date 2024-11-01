package com.example.actor;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.pattern.Patterns;
import akka.util.Timeout;
import scala.concurrent.Await;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;

public class BlockingActor extends UntypedActor {
    private LoggingAdapter log = Logging.getLogger(getContext().system(), this);
    private ActorRef child;
    private Timeout timeout = new Timeout(Duration.create(5, "seconds"));

    public BlockingActor() {
        child = getContext().actorOf(
                Props.create(CalculationActor.class),
                "CalculationActor");
    }

    @Override
    public void onReceive(Object message) throws Exception {
        if (message instanceof Integer) {
            Future<Object> future = Patterns.ask(child, message, timeout);
            Integer result = (Integer) Await.result(future, timeout.duration());
            log.info("Final result is {}", result + 1);
        } else if (message instanceof String) {
            log.info("BlockingActor received a message: {}", message);
        }
    }
}
