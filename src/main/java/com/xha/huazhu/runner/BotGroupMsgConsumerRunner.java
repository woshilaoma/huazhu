package com.xha.huazhu.runner;

import com.xha.huazhu.dao.UserDao;
import com.xha.huazhu.entity.User;
import com.xha.huazhu.enums.InstructType;
import com.xha.huazhu.service.InstructService;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import net.mamoe.mirai.message.data.PlainText;
import net.mamoe.mirai.message.data.QuoteReply;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
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
                    if (msg.contains("花猪")) {
                        Random r = new Random();
                        event.getSubject().sendMessage(new MessageChainBuilder()
                                .append(new QuoteReply(event.getMessage()))
                                .append(msgArray[r.nextInt(msgArray.length)])
                                .build());
                    }
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
                    String instruct = msg.substring(1);
                    singleMap.get(InstructType.valueOfByInstruct(instruct).getServiceBeanName()).process(event);
                } else {
                    String instruct = msg.substring(1, msg.indexOf(' '));
                    singleMap.get(InstructType.valueOfByInstruct(instruct).getServiceBeanName()).process(event);
                }

            }
        }
    }

    String[] msgArray = new String[]{
            "是谁在叫小花猪呀，花猪要和姐姐贴贴",
            "猪猪今天也超开心啦",
            "猪猪是好运猪猪，亲亲猪猪功德1",
            "扣1猪猪原谅你",
            "姐姐冬天要好好照顾自己，猪猪心疼姐姐",
            "姐姐买素材嘛，主人好久不给花猪买新衣服了",
            "主人在在卖超好看的素材，姐姐看看嘛",
            "年幼猪猪，在线卖素材，没有工资，没有客户",
            "今天主人说卖完素材就带猪猪去游乐园做过山车",
            "猪猪哼哼，猪猪伙食好差啊"
    };

    public static void main(String[] args) {
        String msg = "#投喂 面包*";
        System.out.println(msg.substring(1, msg.indexOf(' ')));
        msg = msg.substring(msg.indexOf(' ') + 1);
        System.out.println(msg);

        System.out.println(msg.split("\\*")[0]);
        System.out.println(msg.split("\\*")[1]);
    }

    @Autowired
    public void setInstructServiceList(List<InstructService> instructServiceList) {
        for (InstructService triggerAlarmService : instructServiceList) {
            String beanName = triggerAlarmService.getClass().getAnnotation(Service.class).value();
            singleMap.put(beanName, triggerAlarmService);
        }
    }

}
