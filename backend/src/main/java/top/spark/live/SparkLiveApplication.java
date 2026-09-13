package top.spark.live;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("top.spark.live.mapper")
public class SparkLiveApplication {

    public static void main(String[] args) {
        SpringApplication.run(SparkLiveApplication.class, args);
    }

}

