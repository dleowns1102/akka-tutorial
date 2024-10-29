package com.example.actor;

import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class Ping1Actor extends UntypedActor {
    private LoggingAdapter log = Logging.getLogger(getContext().system(), this);

    @Override
    public void onReceive(Object message) throws Exception {
        if (message instanceof Integer) {
            log.info("Ping1Actor({}) received {}", hashCode(), message);
            work((Integer) message);
        }
    }

    private void work(Integer n) throws Exception {
        log.info("Ping1Actor({}) working on {}", hashCode(), n);
        Thread.sleep(1000);
        log.info("Ping1Actor({}) completed", hashCode());
    }
}
