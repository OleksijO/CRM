package com.becomejavasenior.service;

import com.becomejavasenior.entity.Language;
import com.becomejavasenior.entity.User;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Transactional
public interface UserService  {
    @Transactional(readOnly = true)
    Map<String, User> getUserMap();
    @Transactional
    String createNewUser(String name, String password, String email, int langId);
    @Transactional(readOnly = true)
    List<Language> getLanguageList();

}
