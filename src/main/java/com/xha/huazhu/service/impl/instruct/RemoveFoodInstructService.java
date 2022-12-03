package com.xha.huazhu.service.impl.instruct;

import com.xha.huazhu.dao.FoodDao;
import com.xha.huazhu.dao.UserDao;
import com.xha.huazhu.entity.Food;
import com.xha.huazhu.entity.User;
import com.xha.huazhu.service.InstructService;
import net.mamoe.mirai.event.Event;
import net.mamoe.mirai.event.events.MessageEvent;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import net.mamoe.mirai.message.data.PlainText;
import net.mamoe.mirai.message.data.QuoteReply;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("removeFoodInstructService")
public class RemoveFoodInstructService implements InstructService {


    @Override
    public Event process(MessageEvent event) {
        User user = userDao.queryByQq(event.getSender().getId());
        if (user.getUserType() == User.USER) {
            event.getSubject().sendMessage(new MessageChainBuilder()
                    .append(new QuoteReply(event.getMessage()))
                    .append("暂无权限")
                    .build()
            );
        } else {
            PlainText plainText = (PlainText) event.getMessage().get(1);
            if (plainText.getContent().indexOf(' ') < 0 || plainText.getContent().indexOf(' ') + 1 == plainText.getContent().length()) {
                event.getSubject().sendMessage(new MessageChainBuilder()
                        .append(new QuoteReply(event.getMessage()))
                        .append("食物不存在")
                        .build());
                return event;
            }
            String foodName = plainText.getContent().substring(plainText.getContent().indexOf(' ') + 1);
            int count = foodDao.countByFoodName(foodName);
            if (count > 0) {
                foodDao.deleteByFoodName(foodName);
                event.getSubject().sendMessage(new MessageChainBuilder()
                        .append(new QuoteReply(event.getMessage()))
                        .append("删除食物<" + foodName + ">成功")
                        .build());
                return event;
            } else {
                event.getSubject().sendMessage(new MessageChainBuilder()
                        .append(new QuoteReply(event.getMessage()))
                        .append("食物不存在")
                        .build());
                return event;
            }
        }
        return event;
    }


    private UserDao userDao;


    private FoodDao foodDao;

    @Autowired
    public void setFoodDao(FoodDao foodDao) {
        this.foodDao = foodDao;
    }

    @Autowired
    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

}
