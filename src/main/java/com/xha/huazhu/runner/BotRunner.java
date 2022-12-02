package com.xha.huazhu.runner;

import net.mamoe.mirai.Bot;
import net.mamoe.mirai.BotFactory;
import net.mamoe.mirai.utils.BotConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;


@Component
public class BotRunner implements ApplicationRunner {

    @Autowired
    private Bot bot;

    @Override
    public void run(ApplicationArguments args) {

    }
}
