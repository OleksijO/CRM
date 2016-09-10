package com.becomejavasenior.service;

import com.becomejavasenior.entity.*;

import javax.servlet.http.Part;
import java.util.List;
import java.util.Map;

public interface ContactService extends GenericService<Contact> {

    List<User> getUserList();

    List<Company> getCompanyList();

    List<Stage> getStageList();

    TypeOfPhone[] getPhoneTypes();

    TypeOfPeriod[] getPeriodTypes();

    List<String> getTaskTypesList();

    List<String> getTaskTimeList();

    List<Tag> getTagList();

    boolean createByParameters(Map<String, String[]> parameterMap, Part filePart);

}
