package com.example.actor;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActorWithStash;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.japi.Procedure;

public class PingActor extends UntypedActorWithStash {
    private LoggingAdapter log = Logging.getLogger(getContext().system(), this);
    private ActorRef child;
    private int count = 0;

    public PingActor() {
        child = getContext().actorOf(
                Props.create(Ping1Actor.class),
                "Ping1Actor");

        getContext().become(initial);
    }

    @Override
    public void onReceive(Object message) throws Exception {
        if (message instanceof String) {
            if ("work".equals(message)) {
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

    private Procedure<Object> initial = new Procedure<Object>() {

        @Override
        public void apply(Object message) throws Exception {
            if ("work".equals(message)) {
                child.tell(message, getSelf());
                getContext().become(zeroDone);
            } else {
                stash();
            }
        }
    };

    private Procedure<Object> zeroDone = new Procedure<Object>() {

        @Override
        public void apply(Object message) throws Exception {
            if ("done".equals(message)) {
                log.info("Received the first done");
                getContext().become(oneDone);
            } else {
                stash();
            }
        }
    };

    private Procedure<Object> oneDone = new Procedure<Object>() {

        @Override
        public void apply(Object message) throws Exception {
            if ("done".equals(message)) {
                log.info("Received the second done");
                getContext().become(allDone);
                unstashAll();
            } else {
                stash();
            }
        }
    };

    private Procedure<Object> allDone = new Procedure<Object>() {

        @Override
        public void apply(Object message) throws Exception {
            if ("reset".equals(message)) {
                log.info("Received a reset");
                getContext().become(initial);
                unstashAll();
            } else {
                stash();
            }
        }
    };
}
