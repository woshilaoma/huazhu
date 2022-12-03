package com.xha.huazhu.service.impl.instruct;

import com.xha.huazhu.dao.FoodDao;
import com.xha.huazhu.dao.UserDao;
import com.xha.huazhu.entity.Food;
import com.xha.huazhu.entity.User;
import com.xha.huazhu.job.HuazhuStatusJob;
import com.xha.huazhu.service.InstructService;
import net.mamoe.mirai.event.Event;
import net.mamoe.mirai.event.events.MessageEvent;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import net.mamoe.mirai.message.data.PlainText;
import net.mamoe.mirai.message.data.QuoteReply;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("testInstructService")
public class TestInstructService implements InstructService {




    @Override
    public Event process(MessageEvent event) {
        huazhuStatusJob.job();
        return event;
    }

    private HuazhuStatusJob huazhuStatusJob;

    @Autowired
    public void setHuazhuStatusJob(HuazhuStatusJob huazhuStatusJob) {
        this.huazhuStatusJob = huazhuStatusJob;
    }
}
