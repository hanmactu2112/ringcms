package com.ringme.cms.repository;

import com.ringme.cms.model.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MenuRepository extends JpaRepository<Menu,Long> {
    @Override
    List<Menu> findAllById(Iterable<Long> longs);

    @Override
    <S extends Menu> S save(S entity);

    @Override
    void deleteById(Long aLong);

    @Override
    void delete(Menu entity);
}
