package com.xha.huazhu.bot;

import com.xha.huazhu.runner.BotFirendMsgConsumerRunner;
import com.xha.huazhu.runner.BotGroupMsgConsumerRunner;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.BotFactory;
import net.mamoe.mirai.event.events.FriendMessageEvent;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.utils.BotConfiguration;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.IOException;


@Configuration
public class BotBeanInit {

    @Value("${bot.cacheDir")
    private String cacheDir;
    @Value("${bot.device")
    private String device;

    @Bean
    public Bot botInit() throws IOException {
        long qq = Long.parseLong(System.getProperty("qq"));
        String pwd = System.getProperty("pwd");
        ClassPathResource classPathResource = new ClassPathResource("device.json");
        Bot bot = BotFactory.INSTANCE.newBot(qq, pwd, new BotConfiguration() {{
            setProtocol(MiraiProtocol.ANDROID_PAD);
            setHeartbeatStrategy(BotConfiguration.HeartbeatStrategy.STAT_HB);
            setCacheDir(new File(cacheDir));
            fileBasedDeviceInfo(device);
        }});
        bot.login();
        bot.getEventChannel().subscribeAlways(FriendMessageEvent.class, BotFirendMsgConsumerRunner.QUEUE::add);
        bot.getEventChannel().subscribeAlways(GroupMessageEvent.class, BotGroupMsgConsumerRunner.QUEUE::add);
        return bot;
    }
}
