package top.spark.live.dto;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Data
public class ScheduleDayDTO {
    private long id;
    private LocalDateTime day;
}
