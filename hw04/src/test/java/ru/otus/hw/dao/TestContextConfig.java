package ru.otus.hw.dao;

import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;


@SpringBootConfiguration
@EnableConfigurationProperties
@ComponentScan({"ru.otus.hw.dao"})
public class TestContextConfig {
}
