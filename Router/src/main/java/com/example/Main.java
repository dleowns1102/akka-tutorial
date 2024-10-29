package com.example;

import com.example.actor.PingActor;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;

public class Main {
    public static void main(String[] args) {
        ActorSystem actorSystem = ActorSystem.create("TestSystem");

        ActorRef ping = actorSystem.actorOf(
                Props.create(PingActor.class),
                "PingActor");

        ping.tell("start", ActorRef.noSender());
    }
}