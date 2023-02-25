package com.ringme.cms.service;

import com.ringme.cms.model.UserRole;
import com.ringme.cms.repository.UserRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class UserRoleServiceImpl implements UserRoleService{
    @Autowired
    UserRoleRepository userRoleRepository;
    @Transactional
    @Override
    public void deleteById(Long id) throws Exception {
        try {
            userRoleRepository.deleteById(id);
        } catch (Exception e){
            throw new Exception();
        }
    }

    @Override
    public void addUserRole(UserRole userRole) {
        userRoleRepository.save(userRole);
    }

    @Override
    public List<UserRole> findAllUserRoleByIdUser(Long id) {
        return userRoleRepository.findUserRoleByUserId(id);
    }
}
