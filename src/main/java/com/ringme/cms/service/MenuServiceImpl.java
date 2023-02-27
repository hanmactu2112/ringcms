package com.ringme.cms.service;

import com.ringme.cms.model.Menu;
import com.ringme.cms.repository.MenuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MenuServiceImpl implements MenuService{

    @Autowired
    MenuRepository menuRepository;

    @Override
    public void saveMenu(Menu menu) {
        menuRepository.save(menu);
    }

    @Override
    public Optional<Menu> findMenuById(Long id) {

        return menuRepository.findById(id);
    }

    @Override
    public List<Menu> findAllMenu() {
        return menuRepository.findAll();
    }

    @Override
    public void deleteById(Long id) {
        menuRepository.deleteById(id);
    }

    @Override
    public Page<Menu> findMenuPage(int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo - 1,pageSize);
        return menuRepository.findAll(pageable);
    }
}
