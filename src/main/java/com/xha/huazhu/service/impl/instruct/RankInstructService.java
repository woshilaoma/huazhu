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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service("rankInstructService")
public class RankInstructService implements InstructService {


    @Override
    public Event process(MessageEvent event) {
        List<Record> records = recordDao.findRank(10);
        List<String> userNameList = new ArrayList<>();
        records.forEach(e -> {
            Optional<User> user = userDao.findById(e.getUserId());
            userNameList.add(user.get().getUserName());
        });
        StringBuilder sb = new StringBuilder("排行榜 : \r\n");
        for (int i = 0; i < userNameList.size(); i++) {
            sb.append((i + 1) + "." + userNameList.get(i) + "\r\n");
        }
        event.getSubject().sendMessage(new MessageChainBuilder()
                .append(new QuoteReply(event.getMessage()))
                .append(sb)
                .build());
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
