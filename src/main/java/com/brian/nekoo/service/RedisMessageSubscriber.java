package com.brian.nekoo.service;

import org.springframework.stereotype.Service;

@Service
public class RedisMessageSubscriber {

    public void onMessage(String message, String channel) {
        System.out.println("接收到消息: " + message + " from channel: " + channel);
    }
}
