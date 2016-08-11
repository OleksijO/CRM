package com.becomejavasenior.service;

import com.becomejavasenior.entity.Language;
import com.becomejavasenior.entity.User;

import java.util.List;
import java.util.Map;

public interface UserService {
    Map<String, User> getUserMap();

    String createNewUser(String name, String password, String email, int langId);

    List<Language> getLanguageList();
}
