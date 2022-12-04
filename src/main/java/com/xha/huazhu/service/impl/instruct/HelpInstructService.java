package com.xha.huazhu.service.impl.instruct;

import com.xha.huazhu.enums.InstructType;
import com.xha.huazhu.service.InstructService;
import net.mamoe.mirai.event.Event;
import net.mamoe.mirai.event.events.MessageEvent;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import net.mamoe.mirai.message.data.QuoteReply;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service("helpInstructService")
public class HelpInstructService implements InstructService {
    @Override
    public Event process(MessageEvent event) {
        List<String> list = InstructType.valuesList().stream().map(InstructType::getInstruct).collect(Collectors.toList());
        event.getSubject().sendMessage(new MessageChainBuilder()
                .append(new QuoteReply(event.getMessage()))
                .append("命令列表 <" + list.toString().substring(0, list.toString().length() - 1) + ">")
                .build()
        );
        return event;
    }
}
