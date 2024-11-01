package com.example.actor;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.dispatch.OnComplete;
import akka.dispatch.OnFailure;
import akka.dispatch.OnSuccess;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.pattern.Patterns;
import akka.util.Timeout;
import scala.concurrent.ExecutionContext;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;

public class NonblockingActor extends UntypedActor {
    private LoggingAdapter log = Logging.getLogger(getContext().system(), this);
    private ActorRef child;
    private Timeout timeout = new Timeout(Duration.create(5, "seconds"));
    private final ExecutionContext ec;

    public NonblockingActor() {
        child = getContext().actorOf(
                Props.create(CalculationActor.class),
                "CalculationActor");

        ec = getContext().system().dispatcher();
    }

    @Override
    public void onReceive(Object message) throws Exception {
        if (message instanceof Integer) {
            Future<Object> future = Patterns.ask(child, message, timeout);

            future.onSuccess(new SaySuccess<Object>(), ec);
            future.onComplete(new SayComplete<Object>(), ec);
            future.onFailure(new SayFailure<Object>(), ec);
        } else if (message instanceof String) {
            log.info("NonblockingActor received a message: {}", message);
        }
    }

    public final class SaySuccess<T> extends OnSuccess<T> {

        @Override
        public void onSuccess(T result) throws Throwable {
            log.info("Succeeded with {}", result);
        }
    }

    public final class SayComplete<T> extends OnComplete<T> {

        @Override
        public void onComplete(Throwable failure, T success) throws Throwable {
            log.info("Completed.");
        }
    }

    public final class SayFailure<T> extends OnFailure {

        @Override
        public void onFailure(Throwable failure) throws Throwable {
            log.info("Failed with {}", failure);
        }
    }
}
