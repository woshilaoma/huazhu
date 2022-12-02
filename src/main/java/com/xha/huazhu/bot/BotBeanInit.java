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

    @Value("${cacheDir")
    private String cacheDir;
    @Value("${workingDir")
    private String workingDir;

    @Bean
    public Bot botInit() throws IOException {
        long qq = Long.parseLong(System.getProperty("qq"));
        String pwd = System.getProperty("pwd");
        System.out.println(qq);
        System.out.println(pwd);
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
