package com.example.Crawling.dao;

import com.example.Crawling.entity.Keyword;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface KeywordDao extends JpaRepository<Keyword, Integer> {
    Optional<Keyword> findByKeyword(String keyword);
    Optional<Keyword> findById(int keywordId);

}

