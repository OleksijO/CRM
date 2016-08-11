package com.becomejavasenior.service.impl;

import com.becomejavasenior.entity.Language;
import com.becomejavasenior.entity.User;
import com.becomejavasenior.jdbc.entity.UserDAO;
import com.becomejavasenior.jdbc.exceptions.DatabaseException;
import com.becomejavasenior.jdbc.impl.LanguageDAOImpl;
import com.becomejavasenior.jdbc.impl.UserDAOImpl;
import com.becomejavasenior.service.UserService;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserServiceImpl implements UserService {

    private UserDAO userDAO;

    public UserServiceImpl() {
        this.userDAO = new UserDAOImpl();
    }

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
        Language language = new LanguageDAOImpl().getById(langId);
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
}
