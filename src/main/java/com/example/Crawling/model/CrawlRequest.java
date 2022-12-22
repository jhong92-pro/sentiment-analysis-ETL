package com.example.Crawling.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;


@Getter
@Setter
@ToString
@Builder
public class CrawlRequest {
    private final List<Integer> sentimentPreStageIdList;

    @JsonCreator
    public CrawlRequest(@JsonProperty("sentimentPreStageIdList") List<Integer> sentimentPreStageIdList){
        this.sentimentPreStageIdList = sentimentPreStageIdList;
    }

}