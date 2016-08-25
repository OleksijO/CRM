package com.becomejavasenior.service.impl;

import com.becomejavasenior.entity.Language;
import com.becomejavasenior.entity.User;
import com.becomejavasenior.jdbc.entity.LanguageDAO;
import com.becomejavasenior.jdbc.entity.UserDAO;
import com.becomejavasenior.jdbc.exceptions.DatabaseException;
import com.becomejavasenior.jdbc.impl.LanguageDAOImpl;
import com.becomejavasenior.service.UserService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {
    @Autowired(required = true)
    private UserDAO userDAO;
    @Autowired(required = true)
    private LanguageDAO languageDAO;

    @Override
    public List<Language> getLanguageList() {
        return new LanguageDAOImpl().getAll();
    }

    @Override
    public Map<String, User> getUserMap() {
        List<User> userList = userDAO.getAll();
        Map<String, User> userMap = new HashMap<>();
        for (User user : userList) {
            userMap.put(user.getEmail(), user);
        }
        return userMap;
    }

    @Override
    public String createNewUser(String name, String password, String email, int langId) {

        String errorString = "";

        Map<String, User> userMap = getUserMap();
        if (userMap.containsKey(email)) {
            errorString = "учетная запись '".concat(email).concat("' уже существует");
        }
        Language language = languageDAO.getById(langId);
        if (language == null) {
            errorString = errorString.concat(errorString.isEmpty() ? "" : ", ")
                    .concat("выбранный язык не используется");
        }
        if ("".equals(errorString)) {
            User user = new User();
            user.setName(name);
            user.setEmail(email);
            user.setPassword(password);
            user.setLanguage(language);
            user.setAdmin(userMap.isEmpty());
            try {
                userDAO.insert(user);
            } catch (DatabaseException e) {
                Logger.getRootLogger().error("Service: UserDAO insert record error", e);
                errorString = e.getMessage();
            }
        }
        return errorString;
    }

    public static UserService createInstance() {
        return new UserServiceImpl();
    }
}
