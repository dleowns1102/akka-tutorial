package com.example.actor;

import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import scala.Option;

public class Ping2Actor extends UntypedActor {
    private LoggingAdapter log = Logging.getLogger(getContext().system(), this);

    public Ping2Actor() {
        log.info("Ping2Actor constructor..");
    }

    @Override
    public void preRestart(Throwable reason, Option<Object> message) throws Exception {
        log.info("Ping2Actor preRestart..");
    }

    @Override
    public void postRestart(Throwable reason) throws Exception {
        log.info("Ping2Actor postRestart..");
    }

    @Override
    public void postStop() throws Exception {
        log.info("Ping2Actor postStop..");
    }

    @Override
    public void onReceive(Object message) throws Exception {
        if (message instanceof String) {
            if ("good".equals(message)) {
                goodWork();
                getSender().tell("done", getSelf());
            } else if ("bad".equals(message)) {
                badWork();
            }
        }
    }

    private void goodWork() throws Exception {
        log.info("Ping2Actor is good.");
    }

    private void badWork() throws Exception {
        int a = 1 / 0;
    }
}
