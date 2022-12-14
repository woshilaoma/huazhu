package com.xha.huazhu.service.impl.instruct;

import com.xha.huazhu.service.InstructService;
import net.mamoe.mirai.event.Event;
import net.mamoe.mirai.event.events.MessageEvent;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import net.mamoe.mirai.message.data.QuoteReply;
import org.springframework.stereotype.Service;

@Service("errorInstructService")
public class ErrorInstructService implements InstructService {
    @Override
    public Event process(MessageEvent event) {
        event.getSubject().sendMessage(new MessageChainBuilder()
                .append(new QuoteReply(event.getMessage()))
                .append("未知命令。请输出#帮助 查看命令")
                .build()
        );
        return event;
    }
}
