package com.xha.huazhu.runner;

import com.xha.huazhu.dao.UserDao;
import com.xha.huazhu.entity.User;
import com.xha.huazhu.enums.InstructType;
import com.xha.huazhu.service.InstructService;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.message.data.PlainText;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.LinkedTransferQueue;

@Component
public class BotGroupMsgConsumerRunner implements ApplicationRunner {


    private static final Map<String, InstructService> singleMap = new HashMap<>();
    public static final LinkedTransferQueue<GroupMessageEvent> QUEUE = new LinkedTransferQueue<>();

    private UserDao userDao;

    @Autowired
    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public void run(ApplicationArguments args) throws InterruptedException {
        while (true) {
            GroupMessageEvent event = QUEUE.take();
            if (event.getMessage().get(1) instanceof PlainText) {
                PlainText plainText = (PlainText) event.getMessage().get(1);
                String msg = plainText.getContent();
                //只识别#号开头命令
                if (msg.indexOf('#') != 0) {
                    continue;
                }
                int count = userDao.countByQq(event.getSender().getId());
                if (count == 0) {
                    User user = new User();
                    user.setQq(event.getSender().getId());
                    user.setUserType(User.USER);
                    user.setUserName(event.getSender().getNick());
                    userDao.save(user);
                }
                //#命令 参数
                if (msg.indexOf(' ') < 1) {
                    singleMap.get(InstructType.error.getServiceBeanName()).process(event);
                    String instruct = msg.substring(1);
                    singleMap.get(InstructType.valueOfByInstruct(instruct).getServiceBeanName()).process(event);
                }else{
                    singleMap.get(InstructType.error.getServiceBeanName()).process(event);
                    String instruct = msg.substring(1,msg.indexOf(' '));
                    singleMap.get(InstructType.valueOfByInstruct(instruct).getServiceBeanName()).process(event);
                }

            }
        }
    }

    public static void main(String[] args) {
        String msg = "#签到 测试一下";
        System.out.println(msg.substring(1, msg.indexOf(' ')));
    }

    @Autowired
    public void setInstructServiceList(List<InstructService> instructServiceList) {
        for (InstructService triggerAlarmService : instructServiceList) {
            String beanName = triggerAlarmService.getClass().getAnnotation(Service.class).value();
            singleMap.put(beanName, triggerAlarmService);
        }
    }

}
