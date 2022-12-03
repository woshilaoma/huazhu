package com.xha.huazhu.runner;

import net.mamoe.mirai.event.events.FriendMessageEvent;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.concurrent.LinkedTransferQueue;

@Component
public class BotFirendMsgConsumerRunner implements ApplicationRunner {

    public static final LinkedTransferQueue<FriendMessageEvent> QUEUE = new LinkedTransferQueue<>();

    @Override
    public void run(ApplicationArguments args) {

    }
}
