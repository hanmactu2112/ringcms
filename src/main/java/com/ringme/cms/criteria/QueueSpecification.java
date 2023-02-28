package com.ringme.cms.criteria;

import com.ringme.cms.model.Queue;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class QueueSpecification implements Specification<Queue> {
    private QueueSearchCriteria criteria;

    public QueueSpecification(QueueSearchCriteria criteria) {
        this.criteria = criteria;
    }

    @Override
    public Predicate toPredicate(Root<Queue> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        List<Predicate> predicates = new ArrayList<>();
        if (criteria.getId() != null && criteria.getId()!=0) {
            predicates.add(builder.equal(root.get("id"), criteria.getId()));
        }
        if (criteria.getQueueName() != null && !criteria.getQueueName().trim().equals("")) {
            predicates.add(builder.like(root.get("queueName"), "%" + criteria.getQueueName() + "%"));
        }
        if (criteria.getHostName() != null && !criteria.getHostName().trim().equals("")) {
            predicates.add( builder.like(root.get("hostName"), "%" + criteria.getHostName() + "%"));
        }
        if (criteria.getDisplayName() != null && !criteria.getDisplayName().trim().equals("")) {
            predicates.add(builder.like(root.get("displayName"), "%" + criteria.getDisplayName() + "%"));
        }
        if (criteria.getDepartment() != null && !criteria.getDepartment().trim().equals("")) {
            predicates.add( builder.equal(root.get("department").get("id"), criteria.getDepartment()));
        }
        if (criteria.getTypeQueue() != null && !criteria.getTypeQueue().trim().equals("")) {
            predicates.add( builder.equal(root.get("typeQueue"), criteria.getTypeQueue()));
        }
        if (criteria.getMission() != null&& !criteria.getMission().trim().equals("")) {
            predicates.add( builder.equal(root.get("mission").get("id"), criteria.getMission()));
        }
        if (criteria.getProvince() != null && !criteria.getProvince().trim().equals("")) {
            predicates.add( builder.equal(root.get("province"), criteria.getProvince()));
        }

        return builder.and(predicates.toArray(new Predicate[predicates.size()]));
    }

}
