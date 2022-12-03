package com.xha.huazhu.service.impl.instruct;

import com.xha.huazhu.dao.SignDao;
import com.xha.huazhu.dao.UserDao;
import com.xha.huazhu.entity.Sign;
import com.xha.huazhu.entity.User;
import com.xha.huazhu.service.InstructService;
import com.xha.huazhu.utils.DateUtil;
import net.mamoe.mirai.event.Event;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.event.events.MessageEvent;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import net.mamoe.mirai.message.data.QuoteReply;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service("signInstructService")
public class SignInstructService implements InstructService {


    @Override
    public Event process(MessageEvent event) {
        User user = userDao.queryByQq(event.getSender().getId());
        Date date = new Date();
        int day = Integer.parseInt(DateUtil.formatDateStr(date, DateUtil.YYYYMMDD));
        int count = signDao.countByUserIdAndDay(user.getId(), day);
        if (count > 0) {
            event.getSubject().sendMessage(new MessageChainBuilder()
                    .append(new QuoteReply(event.getMessage()))
                    .append("今日已签到")
                    .build()
            );
        } else {
            Sign sign = new Sign();
            sign.setUserId(user.getId());
            sign.setDay(day);
            signDao.save(sign);
            event.getSubject().sendMessage(new MessageChainBuilder()
                    .append(new QuoteReply(event.getMessage()))
                    .append("签到成功")
                    .build()
            );
        }
        return event;
    }


    private UserDao userDao;

    private SignDao signDao;

    @Autowired
    public void setSignDao(SignDao signDao) {
        this.signDao = signDao;
    }

    @Autowired
    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

}
