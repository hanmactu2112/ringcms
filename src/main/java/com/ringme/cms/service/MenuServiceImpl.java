package com.ringme.cms.service;

import com.ringme.cms.model.Menu;
import com.ringme.cms.repository.MenuRepository;
import org.hibernate.mapping.Collection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

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

    @Override
    public List<Menu> getListMenuNoParent() {
        List<Menu> menus = menuRepository.findAll();
        Map<String,List<Menu>> map = menus.stream().filter(e->!e.getParentName().equals("")).collect(Collectors.groupingBy(Menu::getParentName));
        List<Menu> list = menus.stream().filter(e->e.getParentName().equals("")).collect(Collectors.toList());
        list.sort(Comparator.comparing(Menu::getOrder_num));
        return list;
    }

    @Override
    public Map<String, List<Menu>> getMapMenuParent() {
        List<Menu> menus = menuRepository.findAll();
        Map<String,List<Menu>> map = menus.stream().filter(e->!e.getParentName().equals("")).collect(Collectors.groupingBy(Menu::getParentName));
        Map<String, List<Menu>> sortedMap = map.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue((o1, o2) -> {
                    int size1 = o1.size();
                    int size2 = o2.size();
                    for (int i = 0; i < size1 && i < size2; i++) {
                        int compare = Integer.compare(o1.get(i).getOrder_num(), o2.get(i).getOrder_num());
                        if (compare != 0) {
                            return compare;
                        }
                    }
                    return Integer.compare(size1, size2);
                }))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue,
                        LinkedHashMap::new));
        return sortedMap;
    }
}
