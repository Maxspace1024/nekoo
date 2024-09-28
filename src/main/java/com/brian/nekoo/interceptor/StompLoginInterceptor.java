package com.brian.nekoo.interceptor;

import lombok.extern.log4j.Log4j2;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.stereotype.Component;

import java.security.Principal;
import java.util.UUID;

@Component
@Log4j2
public class StompLoginInterceptor implements ChannelInterceptor {

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        String authorizationHeader = accessor.getFirstNativeHeader("Authorization");
        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
            Principal principal = new Principal() {
                @Override
                public String getName() {
                    return UUID.randomUUID().toString();
                }
            };
            accessor.setUser(principal);
        }
//        } else if (StompCommand.SUBSCRIBE.equals(accessor.getCommand())) {
//            String dst = accessor.getDestination();
//            log.info(dst);
//        } else if (StompCommand.SEND.equals(accessor.getCommand())) {
//            String dst = accessor.getDestination();
//            log.info(dst);
//        }
        return message;
    }
}
