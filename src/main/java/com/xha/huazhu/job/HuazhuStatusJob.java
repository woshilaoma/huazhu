package com.xha.huazhu.job;


import com.xha.huazhu.dao.*;
import com.xha.huazhu.entity.Food;
import com.xha.huazhu.entity.Huazhu;
import com.xha.huazhu.entity.HuazhuJob;
import com.xha.huazhu.entity.HuazhuStomach;
import com.xha.huazhu.utils.DateUtil;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.contact.ContactList;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import net.mamoe.mirai.message.data.QuoteReply;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.*;


@EnableScheduling
@Component
public class HuazhuStatusJob {


    @Scheduled(cron = "0 0/10 * * * ? ")
    public void job() {
        //删除不存在的食物关联关系
        huazhuStomachDao.removeNotInFood();
        userPackageDao.removeNotInFood();
        List<Huazhu> huazhuList = huazhuDao.findAll();
        Huazhu huazhu = huazhuList.get(0);
        //生成今日饥饿次数
        List<HuazhuJob> huazhuJobs = huazhuJobDao.findDay(DateUtil.formatDateStr(new Date(),DateUtil.YYYY_MM_DD)+"%");
        if (huazhuJobs.isEmpty()) {
            insertHuazhuJob(huazhu);
        }
        //判断是否大于执行时间
        huazhuJobs = huazhuJobDao.findAllByOrderByEventTimeAsc();
        String now = DateUtil.formatDateStr(new Date(), DateUtil.HH_MM_SS);
        boolean insertStomachFood = false;
        for (HuazhuJob huazhuJob : huazhuJobs) {
            if (huazhuJob.getEventStatus() == 1 && huazhuJob.getEventTime().compareTo(now) < 1) {
                if (huazhu.getHuazhuStatus() == Huazhu.STATUS_NORMAL) {
                    huazhu.setHuazhuStatus(Huazhu.STATUS_HUNGER);
                    huazhuDao.save(huazhu);
                    huazhuJob.setEventStatus(2);
                    huazhuJobDao.save(huazhuJob);
                    insertStomachFood = true;
                }
                break;
            }
        }
        insertStomachFoodMethon(insertStomachFood, huazhu);
        verifyStomach(huazhu);
        if (huazhu.getHuazhuStatus() == Huazhu.STATUS_HUNGER) {
            List<HuazhuStomach> huazhuStomachList = huazhuStomachDao.findAll();
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
            ContactList<Group> groupList = bot.getGroups();
            groupList.forEach(e -> {
                String msg = "姐姐姐姐，花猪饿了，花猪饿了，花猪要吃 <" + foodNameList.substring(0, foodNameList.length() - 1) + ">，求求快点喂喂猪猪";
                e.sendMessage(msg);
            });
        }
    }

    private void verifyStomach(Huazhu huazhu) {
        if (huazhuStomachDao.count() == 0) {
            huazhu.setHuazhuStatus(Huazhu.STATUS_NORMAL);
            huazhuDao.save(huazhu);
        }
    }

    private void insertStomachFoodMethon(boolean insertStomachFood, Huazhu huazhu) {
        if (!insertStomachFood) {
            return;
        }
        List<Food> foodList = foodDao.findAll();
        Random r = new Random();
        for (int i = 0; i < huazhu.getFoodCount(); i++) {
            Food food = foodList.get(r.nextInt(foodList.size()));
            HuazhuStomach huazhuStomach = huazhuStomachDao.findByFoodId(food.getId());
            if (huazhuStomach == null)
                huazhuStomach = new HuazhuStomach();
            huazhuStomach.setHuazhuId(huazhu.getId());
            huazhuStomach.setFoodId(food.getId());
            huazhuStomach.setFoodCount((huazhuStomach.getFoodCount() == null ? 0 : huazhuStomach.getFoodCount()) + 1);
            huazhuStomachDao.save(huazhuStomach);
        }
    }

    private void insertHuazhuJob(Huazhu huazhu) {
        Integer times = huazhu.getTimes();
        Date startTime = huazhu.getStartTime();
        Date endTime = huazhu.getEndTime();
        Random random = new Random();
        for (int i = 0; i < times; i++) {
            HuazhuJob huazhuJob = new HuazhuJob();
            huazhuJob.setEventStatus(1);
            Date eventTime = new Date();
            eventTime.setTime(random.nextInt((int) (endTime.getTime() - startTime.getTime())) + startTime.getTime());
            huazhuJob.setEventTime(DateUtil.formatDateStr(eventTime, DateUtil.HH_MM_SS));
            huazhuJobDao.save(huazhuJob);
        }
    }

    private HuazhuJobDao huazhuJobDao;
    private UserDao userDao;

    private FoodDao foodDao;

    private HuazhuDao huazhuDao;

    private HuazhuStomachDao huazhuStomachDao;

    private UserPackageDao userPackageDao;

    private Bot bot;

    @Autowired
    public void setBot(Bot bot) {
        this.bot = bot;
    }

    @Autowired
    public void setHuazhuJobDao(HuazhuJobDao huazhuJobDao) {
        this.huazhuJobDao = huazhuJobDao;
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
