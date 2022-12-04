package com.xha.huazhu;

import com.xha.huazhu.job.HuazhuStatusJob;
import com.xha.huazhu.service.FoodService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

//@SpringBootTest
class HuazhuApplicationTests {

    @Autowired
    private FoodService foodService;

    @Test
    void contextLoads() {
        System.out.println(foodService.addFood("大苹果"));
        ;
    }

    @Autowired
    private HuazhuStatusJob huazhuStatusJob;

    @Test
    void test() {
        huazhuStatusJob.job();
    }

}
