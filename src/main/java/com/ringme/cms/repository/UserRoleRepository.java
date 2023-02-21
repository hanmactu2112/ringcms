package com.ringme.cms.repository;

import com.ringme.cms.model.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole,Long> {

    @Query(value = "Select * from user_role ur inner join role r on r.id = ur.role_id " +
            "inner join user u on u.id = ur.user_id where ur.user_id =?1",nativeQuery = true)
    List<UserRole> findUserRoleByUserId(Long id);
}
