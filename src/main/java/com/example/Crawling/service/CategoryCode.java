package com.example.Crawling.service;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CategoryCode {
    HUMOR_HUMOR("유머_유머"),
    HUMOR_MEME("유머_움짤"),
    HUMOR_ISSUE("유머_이슈"),
    POLITICS_GENERAL("정치_자유"),
    POLITICS_COLOSSEUM("정치_콜로세움"),
    POLITICS_COLUMN("정치_컬럼"),
    POLITICS_HUMOR("정치_유머"),
    POLITICS_EVENT("정치_이벤트"),
    POLITICS_POLL("정치_투표인증"),

    // 일베
    POLITICS("정치/시사");
    private final String categoryName;
}
