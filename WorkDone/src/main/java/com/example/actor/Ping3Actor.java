package com.example.actor;

import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class Ping3Actor extends UntypedActor {
    private LoggingAdapter log = Logging.getLogger(getContext().system(), this);

    @Override
    public void onReceive(Object message) throws Exception {
        if (message instanceof String) {
            if ("work".equals(message)) {
                log.info("Ping3 received '{}'", message);
                work();
                getSender().tell("done", getSelf());
            }
        }
    }

    private void work() throws Exception {
        Thread.sleep(1000);
        log.info("Ping3 working...");
    }
}
