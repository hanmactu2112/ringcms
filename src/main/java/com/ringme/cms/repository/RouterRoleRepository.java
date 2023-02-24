package com.ringme.cms.repository;

import com.ringme.cms.model.RouterRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RouterRoleRepository extends JpaRepository<RouterRole,Long> {
    @Query(value = "SELECT * From router_role rr inner join router r on r.id = rr.router_id inner join role rl on rl.id = rr.role_id where rr.role_id=?1",nativeQuery = true)
    List<RouterRole> findAllRouterRoleByRoleId(Long roleId);
    @Query(value = "Select * from router_role rr where rr.role_id in (?1)",nativeQuery = true)
    List<RouterRole> findAllRouterRoleByListRoleId(List<Long> roleIds);
//    @Query(value = "SELECT * from router_role rr  inner join router r on r.id = rr.router_id where rr.role_id =?1",nativeQuery = true)
//    List<RouterRole> findAllRouterByIdRole(Long roleIds);
}
