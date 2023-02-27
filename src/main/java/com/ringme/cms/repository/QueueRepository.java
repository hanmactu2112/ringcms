package com.ringme.cms.repository;

import com.ringme.cms.model.Queue;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface QueueRepository extends JpaRepository<Queue,Long> {
    @Override
    List<Queue> findAll();

    @Override
    List<Queue> findAll(Sort sort);

    @Override
    List<Queue> findAllById(Iterable<Long> longs);

    @Override
    <S extends Queue> S save(S entity);

    @Override
    Optional<Queue> findById(Long aLong);

    @Override
    void deleteAllById(Iterable<? extends Long> longs);
}
