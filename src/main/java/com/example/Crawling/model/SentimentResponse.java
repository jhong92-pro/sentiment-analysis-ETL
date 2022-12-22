package com.example.Crawling.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class SentimentResponse {
    public int Neutral;
    public int Positive;
    public int Negative;
}