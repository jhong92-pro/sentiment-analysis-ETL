package com.example.Crawling.dao;

import com.example.Crawling.entity.CrawlResult;
import com.example.Crawling.service.DomainCode;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Predicate;
import java.util.List;

public class CrawlResultSpecification {
    public static Specification<CrawlResult> likeTitleAndEqualDomain(List<String> titles, DomainCode domainCode) {
        return (root, query, criteriaBuilder) -> {
            Predicate predicateForDomainCode = criteriaBuilder.equal(root.get("domain"),domainCode);
            Predicate predicateForTitle = criteriaBuilder.disjunction();
            for(String title:titles){
                predicateForTitle = criteriaBuilder.or(predicateForTitle, criteriaBuilder.like(root.get("title"), "%"+title+"%"));
            }
            return criteriaBuilder.and(predicateForDomainCode, predicateForTitle);
        };
    }

    public static Specification<CrawlResult> equalDomain(DomainCode domainCode) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("domain"), domainCode);
    }

    public static Specification<CrawlResult> likeTitle(String title) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("title"), "%"+title+"%");
    }
}
