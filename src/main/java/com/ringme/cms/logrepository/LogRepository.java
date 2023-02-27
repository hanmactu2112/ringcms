package com.ringme.cms.logrepository;

import com.ringme.cms.model.Log;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface LogRepository extends JpaRepository<Log,Long> {
    @Query(value = "SELECT * from log l order by l.time",
            countQuery = "SELECT Count(*) from log l order by l.time",nativeQuery = true)
    Page<Log> findAllLogPage(Pageable pageable);
}
