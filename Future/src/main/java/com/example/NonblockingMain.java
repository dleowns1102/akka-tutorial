package com.example;

import com.example.actor.NonblockingActor;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;

public class NonblockingMain {
    public static void main(String[] args) {
        ActorSystem actorSystem = ActorSystem.create("TestSystem");

        ActorRef blocking = actorSystem.actorOf(
                Props.create(NonblockingActor.class),
                "BlockingActor");

        blocking.tell(10, ActorRef.noSender());
        blocking.tell("hello", ActorRef.noSender());
    }
}