package com.ruoyi.gen;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.ruoyi.common.security.annotation.EnableCustomConfig;
import com.ruoyi.common.security.annotation.EnableRyFeignClients;

/**
 * 代码生成
 * 
 * @author ruoyi
 */
@EnableCustomConfig
@EnableRyFeignClients
@SpringBootApplication
public class RuoYiGenApplication
{
    public static void main(String[] args)
    {
        SpringApplication.run(RuoYiGenApplication.class, args);
        System.out.println("代码生成模块启动成功");
    }
}
