package com.ringme.cms.service;

import com.ringme.cms.model.User;
import org.springframework.data.domain.Page;

import java.util.Optional;

public interface UserService {
    void saveUser(User user);

    Page<User> pageUser(int pageNo,int pageSize,Long id,String username,String type);
    void deleteUser(Long id) throws Exception;

    Optional<User> findByIdUser(Long id);
}
