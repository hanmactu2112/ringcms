package com.ringme.cms.repository;

import com.ringme.cms.model.Queue;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface QueueRepository extends JpaRepository<Queue, Long> , JpaSpecificationExecutor<Queue> {
    @Override
    <S extends Queue> Page<S> findAll(Example<S> example, Pageable pageable);

    @Override
    Page<Queue> findAll(Specification<Queue> spec, Pageable pageable);

    @Query(value = "Select * from queue q where q.next_queue_id =: nextQueueId", nativeQuery = true)
    List<Queue> findAllByNextQueueId(Long nextQueueId);

    @Query(value = "select q.type_queue from queue q group by q.type_queue", nativeQuery = true)
    List<String> findAllTypeQueue();

    @Query(value = "select q.province from queue q group by q.province", nativeQuery = true)
    List<String> findAllProvince();


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
