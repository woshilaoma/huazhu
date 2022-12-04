package com.xha.huazhu.service.impl.instruct;

import com.xha.huazhu.dao.FoodDao;
import com.xha.huazhu.dao.SignDao;
import com.xha.huazhu.dao.UserDao;
import com.xha.huazhu.dao.UserPackageDao;
import com.xha.huazhu.entity.*;
import com.xha.huazhu.service.InstructService;
import com.xha.huazhu.utils.DateUtil;
import net.mamoe.mirai.event.Event;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.event.events.MessageEvent;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import net.mamoe.mirai.message.data.QuoteReply;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service("signInstructService")
public class SignInstructService implements InstructService {


    private Integer roundMax;

    private Integer roundMin;


    @Override
    public Event process(MessageEvent event) {
        User user = userDao.queryByQq(event.getSender().getId());
        Date date = new Date();
        int day = Integer.parseInt(DateUtil.formatDateStr(date, DateUtil.YYYYMMDD));
        int count = signDao.countByUserIdAndDay(user.getId(), day);
        if (count > 0) {
            event.getSubject().sendMessage(new MessageChainBuilder()
                    .append(new QuoteReply(event.getMessage()))
                    .append("姐姐今天已经看过小花猪啦，明天别忘了来看看猪猪呀")
                    .build()
            );
        } else {
            Sign sign = new Sign();
            sign.setUserId(user.getId());
            sign.setDay(day);
            signDao.save(sign);
            //获取食物列表大小
            List<Food> foodList = foodDao.findAll();
            //获取随机数
            Random random = new Random();
            int r = random.nextInt(roundMax - roundMin) + roundMin;
            List<Food> addPackageFoodList = new ArrayList<>(r + 1);
            for (int i = 0; i < r; i++) {
                addPackageFoodList.add(foodList.get(random.nextInt(foodList.size())));
            }
            addPackageFoodList = addPackageFoodList.stream().sorted(Comparator.comparingInt(BaseEntity::getId)).collect(Collectors.toList());
            for (Food food : addPackageFoodList) {

            }
            Map<String, List<Food>> foodMap = addPackageFoodList.stream().collect(Collectors.groupingBy(Food::getFoodName));
            StringBuilder foodNameList = new StringBuilder();
            foodMap.forEach((k, v) -> {
                UserPackage userPackage = userPackageDao.getByUserIdAndFoodId(user.getId(), v.get(0).getId());
                if (userPackage == null)
                    userPackage = new UserPackage();
                userPackage.setUserId(user.getId());
                userPackage.setFoodId(v.get(0).getId());
                userPackage.setCount((userPackage.getCount() == null ? 0 : userPackage.getCount()) + v.size());
                userPackageDao.save(userPackage);
                foodNameList.append(v.get(0).getFoodName() + "*" + v.size() + ",");
            });
            event.getSubject().sendMessage(new MessageChainBuilder()
                    .append(new QuoteReply(event.getMessage()))
                    .append("姐姐又来签到看小花猪啦，祝姐姐今天万事顺利呀。\r\n获得食物<" + foodNameList.substring(0, foodNameList.length() - 1) + ">")
                    .build()
            );


        }
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
