package com.xha.huazhu.service.impl.instruct;

import com.xha.huazhu.service.InstructService;
import net.mamoe.mirai.event.Event;
import net.mamoe.mirai.event.events.MessageEvent;
import org.springframework.stereotype.Service;

@Service("errorInstructService")
public class ErrorInstructService implements InstructService {
    @Override
    public Event process(MessageEvent event) {
        return null;
    }
}
