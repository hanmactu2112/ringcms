package com.ringme.cms.repository;

import com.ringme.cms.model.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserRole,Long> {
}
