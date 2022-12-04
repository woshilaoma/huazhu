package com.xha.huazhu.service.impl.instruct;

import com.xha.huazhu.dao.*;
import com.xha.huazhu.entity.*;
import com.xha.huazhu.service.InstructService;
import net.mamoe.mirai.event.Event;
import net.mamoe.mirai.event.events.MessageEvent;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import net.mamoe.mirai.message.data.PlainText;
import net.mamoe.mirai.message.data.QuoteReply;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service("feedInstructService")
public class FeedInstructService implements InstructService {


    @Override
    public Event process(MessageEvent event) {
        List<Huazhu> huazhuList = huazhuDao.findAll();
        Huazhu huazhu = huazhuList.get(0);
        if (huazhu.getHuazhuStatus() == Huazhu.STATUS_NORMAL) {
            event.getSubject().sendMessage(new MessageChainBuilder()
                    .append(new QuoteReply(event.getMessage()))
                    .append("花猪不饿啦，谢谢漂亮姐姐们的投喂，姐姐们看看相册在售素材，有小人也有花花，有现风也有古风，可以线下啦")
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
            String feedFood = plainText.getContent().substring(plainText.getContent().lastIndexOf(' ') + 1);
            String foodName;
            int count = 0;
            try {
                if (!feedFood.contains("*")) {
                    foodName = feedFood;
                    count = 1;
                } else {
                    foodName = feedFood.split("\\*")[0];
                    count = Integer.parseInt(feedFood.split("\\*")[1]);
                }
            } catch (Exception e) {
                event.getSubject().sendMessage(new MessageChainBuilder()
                        .append(new QuoteReply(event.getMessage()))
                        .append("命令错误，请重新编辑")
                        .build());
                return event;
            }
            Food food = foodDao.getByFoodName(foodName);
            if (food == null) {
                event.getSubject().sendMessage(new MessageChainBuilder()
                        .append(new QuoteReply(event.getMessage()))
                        .append("食物不存在")
                        .build());
                return event;
            }
            //判断背包是否存在食物
            User user = userDao.queryByQq(event.getSender().getId());
            UserPackage userPackage = userPackageDao.getByUserIdAndFoodId(user.getId(), food.getId());
            if (userPackage == null) {
                event.getSubject().sendMessage(new MessageChainBuilder()
                        .append(new QuoteReply(event.getMessage()))
                        .append("姐姐骗猪猪，姐姐包里根本没有<" + food.getFoodName() + ">，猪猪生气")
                        .build());
                return event;
            }
            if (userPackage.getCount() < count) {
                event.getSubject().sendMessage(new MessageChainBuilder()
                        .append(new QuoteReply(event.getMessage())).append("背包 <").append(food.getFoodName()).append("> 数量不足")
                        .build());
                return event;
            }
            if (huazhuStomachDao.count() == 0) {
                huazhu.setHuazhuStatus(Huazhu.STATUS_NORMAL);
                huazhuDao.save(huazhu);
                event.getSubject().sendMessage(new MessageChainBuilder()
                        .append(new QuoteReply(event.getMessage()))
                        .append("花猪不饿啦，谢谢漂亮姐姐们的投喂，姐姐们看看相册在售素材，有小人也有花花，有现风也有古风，可以线下啦")
                        .build()
                );
            }
            List<HuazhuStomach> huazhuStomachList = huazhuStomachDao.findAll();
            boolean b = true;
            for (HuazhuStomach huazhuStomach : huazhuStomachList) {
                if (Objects.equals(huazhuStomach.getFoodId(), food.getId())) {
                    b = false;
                }
            }
            if (b) {
                event.getSubject().sendMessage(new MessageChainBuilder()
                        .append(new QuoteReply(event.getMessage()))
                        .append("花猪不吃<" + food.getFoodName() + ">，花猪是姐姐一个人的小猪猪")
                        .build());
                return event;
            }
            for (HuazhuStomach huazhuStomach : huazhuStomachList) {
                if (Objects.equals(huazhuStomach.getFoodId(), food.getId())) {
                    if (count < huazhuStomach.getFoodCount()) {
                        if (count != userPackage.getCount()) {
                            userPackage.setCount(userPackage.getCount() - count);
                            userPackageDao.save(userPackage);
                        } else {
                            userPackageDao.deleteByFoodId(food.getId());
                        }
                        Record record = new Record();
                        record.setFoodId(food.getId());
                        record.setHuazhuId(huazhu.getId());
                        record.setFoodCount(count);
                        record.setUserId(user.getId());
                        recordDao.save(record);
                        event.getSubject().sendMessage(new MessageChainBuilder()
                                .append(new QuoteReply(event.getMessage())).append("谢谢小姐姐，花猪吃了你的<").append(food.getFoodName()).append(">").append(String.valueOf(count)).append("个，希望小姐姐越来越漂亮")
                                .build());
                        huazhuStomach.setFoodCount(huazhuStomach.getFoodCount() - count);
                        huazhuStomachDao.save(huazhuStomach);
                    } else {
                        if (!Objects.equals(huazhuStomach.getFoodCount(), userPackage.getCount())) {
                            userPackage.setCount(userPackage.getCount() - huazhuStomach.getFoodCount());
                            Record record = new Record();
                            record.setFoodId(food.getId());
                            record.setHuazhuId(huazhu.getId());
                            record.setFoodCount(userPackage.getCount() - huazhuStomach.getFoodCount());
                            record.setUserId(user.getId());
                            recordDao.save(record);
                            userPackageDao.save(userPackage);
                        } else {
                            Record record = new Record();
                            record.setFoodId(food.getId());
                            record.setHuazhuId(huazhu.getId());
                            record.setFoodCount(count);
                            record.setUserId(user.getId());
                            recordDao.save(record);
                            userPackageDao.deleteByFoodId(food.getId());
                        }
                        event.getSubject().sendMessage(new MessageChainBuilder()
                                .append(new QuoteReply(event.getMessage())).append("谢谢小姐姐，花猪吃了你的<").append(food.getFoodName()).append(">").append(String.valueOf(huazhuStomach.getFoodCount())).append("个，希望小姐姐越来越漂亮")
                                .build());
                        huazhuStomachDao.deleteById(huazhuStomach.getId());
                    }
                }
            }
            if (huazhuStomachDao.count() == 0) {
                huazhu.setHuazhuStatus(Huazhu.STATUS_NORMAL);
                huazhuDao.save(huazhu);
            }
        }
        return event;
    }


    private UserDao userDao;

    private FoodDao foodDao;

    private HuazhuDao huazhuDao;

    private HuazhuStomachDao huazhuStomachDao;

    private UserPackageDao userPackageDao;


    private RecordDao recordDao;

    @Autowired
    public void setRecordDao(RecordDao recordDao) {
        this.recordDao = recordDao;
    }

    @Autowired
    public void setUserPackageDao(UserPackageDao userPackageDao) {
        this.userPackageDao = userPackageDao;
    }

    @Autowired
    public void setHuazhuDao(HuazhuDao huazhuDao) {
        this.huazhuDao = huazhuDao;
    }

    @Autowired
    public void setHuazhuStomachDao(HuazhuStomachDao huazhuStomachDao) {
        this.huazhuStomachDao = huazhuStomachDao;
    }

    @Autowired
    public void setFoodDao(FoodDao foodDao) {
        this.foodDao = foodDao;
    }

    @Autowired
    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

}
