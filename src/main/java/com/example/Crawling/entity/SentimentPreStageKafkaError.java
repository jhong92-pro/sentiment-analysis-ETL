package com.example.Crawling.entity;

import com.example.Crawling.model.SentimentCode;
import com.example.Crawling.model.YnCode;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name="sentiment_pre_stage_kafka_error")
@Builder
@ToString
public class SentimentPreStageKafkaError {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int kafkaErrorId;
    private int sentimentPreStageId;
    private LocalDateTime errorDate;
    @Enumerated(value=EnumType.STRING)
    private YnCode completeYn;

    @PrePersist
    public void errorDate() {
        this.errorDate = LocalDateTime.now();
    }
}