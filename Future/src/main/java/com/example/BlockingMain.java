package com.example;

import com.example.actor.BlockingActor;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;

public class BlockingMain {
    public static void main(String[] args) {
        ActorSystem actorSystem = ActorSystem.create("TestSystem");

        ActorRef blocking = actorSystem.actorOf(
                Props.create(BlockingActor.class),
                "BlockingActor");

        blocking.tell(10, ActorRef.noSender());
        blocking.tell("hello", ActorRef.noSender());
    }
}