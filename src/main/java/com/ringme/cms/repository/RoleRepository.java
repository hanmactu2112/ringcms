package com.ringme.cms.repository;

import com.ringme.cms.model.Role;
import com.ringme.cms.model.RouterRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role,Long> {
    @Query(value = "Select r.* from role r where r.id not in (?1)",nativeQuery = true)
    List<Role> findAllRoleNotInListIdRole(List<Long> id);

    @Override
    List<Role> findAllById(Iterable<Long> longs);

    @Override
    <S extends Role> S save(S entity);

    @Override
    Optional<Role> findById(Long aLong);

    @Override
    void delete(Role entity);

    @Override
    void deleteById(Long aLong);
}
