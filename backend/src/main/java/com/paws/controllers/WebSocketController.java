package com.paws.controllers;

import com.paws.dataseeders.Hello;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

@Controller
public class WebSocketController {
    @MessageMapping("/hello")
    @SendToUser("/queue/greetings")
    public Hello greeting(Hello msg) throws Exception {
        System.out.println(111);
        return new Hello("aha");
    }
}
