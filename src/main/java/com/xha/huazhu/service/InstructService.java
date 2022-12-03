package com.xha.huazhu.service;

import net.mamoe.mirai.event.Event;
import net.mamoe.mirai.event.events.MessageEvent;

public interface InstructService {

    Event process(MessageEvent event);
}
