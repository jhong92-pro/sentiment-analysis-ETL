package com.example.Crawling.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.HashMap;
import java.util.List;


@Getter
@Setter
@ToString
@Builder
public class SentimentRequest {
    private final HashMap<Integer, List<String>> sentenceListById;

    @JsonCreator
    public SentimentRequest(@JsonProperty("sentenceListByDate") HashMap<Integer, List<String>> sentenceListById){
        this.sentenceListById = sentenceListById;
    }

}