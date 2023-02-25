package com.ringme.cms.service;

import com.ringme.cms.model.User;
import com.ringme.cms.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService{
    @Autowired
    UserRepository userRepository;
    @Override
    public void saveUser(User user) {
        userRepository.save(user);
    }
    @Override
    public void deleteUser(Long id) throws Exception{
        try{
            userRepository.deleteById(id);
        }
        catch (Exception e){
            throw new Exception();
        }
    }
    @Override
    public Optional<User> findByIdUser(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public Page<User> pageUser(int pageNo, int pageSize, Long id, String username, String userType) {
        Pageable pageable = PageRequest.of(pageNo-1,pageSize);
        if (id==0 && username.equals("") && userType.equals("")){
            return userRepository.findAll(pageable);
        }
        else if (id!=0&&username.equals("")&& userType.equals("")){
            return userRepository.findAllUserById(id,pageable);
        }else if (id==0 && !username.equals("") && userType.equals("")){
            return userRepository.findAllUserByUsername(username,pageable);
        }else if (id==0 && username.equals("") && !userType.equals("")){
            return userRepository.findAllUserByType(userType,pageable);
        }else if (id!=0&& !username.equals("") && userType.equals("")){
            return userRepository.findAllUserByIdAndUsername(id,username,pageable);
        }else if (id!=0 && username.equals("") && !userType.equals("")){
            return userRepository.findAllUserByIdAndType(id,userType,pageable);
        }else if (id==0 && !username.equals("") && !userType.equals("")) {
            return userRepository.findAllUserBy(username, userType, pageable);
        }
        else {
            return userRepository.findAllUserBy(id,username,userType,pageable);
        }
    }
}
