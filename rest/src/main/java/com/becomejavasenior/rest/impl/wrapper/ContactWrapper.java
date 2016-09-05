package com.becomejavasenior.rest.impl.wrapper;


import com.becomejavasenior.entity.Contact;

import javax.xml.bind.annotation.*;
import java.util.List;

@XmlRootElement(name = "ContactList")
@XmlAccessorType(XmlAccessType.FIELD)
public class ContactWrapper {
    @XmlElement(name = "contact")
    private List<Contact> list;



    public ContactWrapper() {
    }

    public ContactWrapper(List<Contact> list) {
        this.list = list;
    }

    public void setList(List<Contact> list) {
        this.list = list;
    }

    public List<Contact> getList() {

        return list;
    }
}
