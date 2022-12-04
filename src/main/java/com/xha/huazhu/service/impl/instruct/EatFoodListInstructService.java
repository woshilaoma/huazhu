package com.xha.huazhu.service.impl.instruct;

import com.xha.huazhu.dao.FoodDao;
import com.xha.huazhu.dao.HuazhuDao;
import com.xha.huazhu.dao.HuazhuStomachDao;
import com.xha.huazhu.dao.UserDao;
import com.xha.huazhu.entity.Food;
import com.xha.huazhu.entity.Huazhu;
import com.xha.huazhu.entity.HuazhuStomach;
import com.xha.huazhu.entity.User;
import com.xha.huazhu.service.InstructService;
import net.mamoe.mirai.event.Event;
import net.mamoe.mirai.event.events.MessageEvent;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import net.mamoe.mirai.message.data.PlainText;
import net.mamoe.mirai.message.data.QuoteReply;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service("eatFoodListInstructService")
public class EatFoodListInstructService implements InstructService {


    @Override
    public Event process(MessageEvent event) {
        List<HuazhuStomach> huazhuStomachList = huazhuStomachDao.findAll();
        if(huazhuStomachList.isEmpty()){
            event.getSubject().sendMessage(new MessageChainBuilder()
                    .append(new QuoteReply(event.getMessage()))
                    .append("花猪不饿")
                    .build()
            );
            return event;
        }
        StringBuilder foodNameList = new StringBuilder();
        huazhuStomachList.forEach(v -> {
            try {
                Optional<Food> food = foodDao.findById(v.getFoodId());
                foodNameList.append(food.get().getFoodName() + "*" + v.getFoodCount() + ",");
            } catch (NoSuchElementException e) {
                //食物不存在
                huazhuStomachDao.deleteByFoodId(v.getFoodId());
            }

        });
        if (foodNameList.length() == 0) {
            List<Huazhu> huazhuList = huazhuDao.findAll();
            Huazhu huazhu = huazhuList.get(0);
            huazhu.setHuazhuStatus(Huazhu.STATUS_NORMAL);
            huazhuDao.save(huazhu);
            if(huazhuStomachList.isEmpty()){
                event.getSubject().sendMessage(new MessageChainBuilder()
                        .append(new QuoteReply(event.getMessage()))
                        .append("花猪不饿")
                        .build()
                );
            }
            return event;
        }
        event.getSubject().sendMessage(new MessageChainBuilder()
                .append(new QuoteReply(event.getMessage()))
                .append("花猪想吃 <" + foodNameList.substring(0, foodNameList.length() - 1) + ">")
                .build()
        );
        return event;
    }


    private HuazhuDao huazhuDao;


    private FoodDao foodDao;

    private HuazhuStomachDao huazhuStomachDao;

    @Autowired
    public void setHuazhuStomachDao(HuazhuStomachDao huazhuStomachDao) {
        this.huazhuStomachDao = huazhuStomachDao;
    }

    @Autowired
    public void setFoodDao(FoodDao foodDao) {
        this.foodDao = foodDao;
    }
    @Autowired
    public void setHuazhuDao(HuazhuDao huazhuDao) {
        this.huazhuDao = huazhuDao;
    }
}
