package com.example.Crawling.dao;

import com.example.Crawling.entity.CrawlResult;
import com.example.Crawling.entity.Keyword;
import com.example.Crawling.service.DomainCode;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CrawlResultDao extends JpaRepository<CrawlResult, Integer> {
    List<CrawlResult> findAllByDomainAndYmd(DomainCode domain, String ymd);

    List<CrawlResult> findAll(Specification<CrawlResult> spec);
}

