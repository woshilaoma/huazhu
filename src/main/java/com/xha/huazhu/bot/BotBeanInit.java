package com.xha.huazhu.bot;

import net.mamoe.mirai.Bot;
import net.mamoe.mirai.BotFactory;
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

    @Value("${bot.qq}")
    private Long qq;
    @Value("${bot.pwd}")
    private String pwd;
    @Value("${cacheDir")
    private String cacheDir;
    @Value("${workingDir")
    private String workingDir;
    @Bean
    public Bot botInit() throws IOException {
        ClassPathResource classPathResource = new ClassPathResource("device.json");
        Bot bot = BotFactory.INSTANCE.newBot(qq, pwd, new BotConfiguration() {{
            fileBasedDeviceInfo();
            setProtocol(MiraiProtocol.ANDROID_PAD);
            setHeartbeatStrategy(BotConfiguration.HeartbeatStrategy.STAT_HB);
            setWorkingDir(new File(workingDir));
            setCacheDir(new File(cacheDir));
            fileBasedDeviceInfo(classPathResource.getURL().toString());
        }});
        bot.login();
        return bot;
    }
}
