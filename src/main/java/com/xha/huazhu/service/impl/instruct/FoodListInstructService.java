package com.xha.huazhu.service.impl.instruct;

import com.xha.huazhu.dao.FoodDao;
import com.xha.huazhu.dao.SignDao;
import com.xha.huazhu.dao.UserDao;
import com.xha.huazhu.dao.UserPackageDao;
import com.xha.huazhu.entity.*;
import com.xha.huazhu.service.InstructService;
import com.xha.huazhu.utils.DateUtil;
import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.event.Event;
import net.mamoe.mirai.event.events.MessageEvent;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import net.mamoe.mirai.message.data.QuoteReply;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.*;
import java.util.stream.Collectors;

@Service("foodListInstructService")
@Slf4j
public class FoodListInstructService implements InstructService {


    private Integer roundMax;

    private Integer roundMin;


    @Override
    public Event process(MessageEvent event) {
        User user = userDao.queryByQq(event.getSender().getId());

        List<UserPackage> userPackageList = userPackageDao.getByUserId(user.getId());
        StringBuilder foodNameList = new StringBuilder();
        userPackageList.forEach(v -> {
            try {
                Optional<Food> food = foodDao.findById(v.getFoodId());
                foodNameList.append(food.get().getFoodName() + "*" + v.getCount() + ",");
            } catch (NoSuchElementException e) {
                //食物不存在
                userPackageDao.deleteByFoodId(v.getFoodId());
            }

        });
        if (foodNameList.length() == 0) {
            event.getSubject().sendMessage(new MessageChainBuilder()
                    .append(new QuoteReply(event.getMessage()))
                    .append("背包空空，猪猪饿饿")
                    .build()
            );
            return event;
        }
        event.getSubject().sendMessage(new MessageChainBuilder()
                .append(new QuoteReply(event.getMessage()))
                .append("背包包含 <" + foodNameList.substring(0, foodNameList.length() - 1) + ">")
                .build()
        );
        return event;
    }


    private FoodDao foodDao;
    private UserDao userDao;

    private SignDao signDao;

    @Autowired
    public void setFoodDao(FoodDao foodDao) {
        this.foodDao = foodDao;
    }

    @Autowired
    public void setSignDao(SignDao signDao) {
        this.signDao = signDao;
    }

    @Autowired
    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    @Value("${bot.roundMin}")
    public void setRoundMin(Integer roundMin) {
        this.roundMin = roundMin;
    }

    @Value("${bot.roundMax}")
    public void setRoundMax(Integer roundMax) {
        this.roundMax = roundMax;
    }

    private UserPackageDao userPackageDao;

    @Autowired
    public void setUserPackageDao(UserPackageDao userPackageDao) {
        this.userPackageDao = userPackageDao;
    }
}
