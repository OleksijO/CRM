package com.becomejavasenior.rest.impl;

import com.becomejavasenior.entity.Contact;
import com.becomejavasenior.rest.impl.wrappers.ContactWrapper;
import com.becomejavasenior.service.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping     //root controller's uri '/rest/'  folder set by mapping in web.xml
public class ContactRestController {

    @Autowired
    ContactService contactService;

    @RequestMapping(value = "/contact", method = RequestMethod.GET)
    public ContactWrapper getAll(){
        List<Contact> contactList=contactService.getAll();
        ContactWrapper list=new ContactWrapper(contactList);
        return list;
    }

    @RequestMapping(path = "/contact/{id}", method = RequestMethod.GET)
    public Contact getById(@PathVariable int id){
        return contactService.getById(id);
    }

    @RequestMapping(path = "/contact/{id}", method = RequestMethod.DELETE)
    public void deleteById(@PathVariable int id, HttpServletResponse response){
        contactService.delete(id);
        response.setStatus(HttpServletResponse.SC_OK);
    }

    @RequestMapping(path = "/contact", method = RequestMethod.POST)
    public Contact saveOrUpdate(@RequestBody Contact contact, HttpServletResponse response){
        if (contact.getId()>0) {
            contactService.update(contact);
        } else {
            contact.setId(contactService.insert(contact));
        }
        return contact;
    }

    @RequestMapping(path = "/contact", method = RequestMethod.HEAD)
    public void ping(HttpServletResponse response){
        response.setStatus(HttpServletResponse.SC_OK);
    }

    @RequestMapping(path = "/contact", method = RequestMethod.OPTIONS)
    public void options(HttpServletResponse response){
        //TODO
        response.setStatus(HttpServletResponse.SC_OK);
    }

    public void setContactService(ContactService contactService) {
        this.contactService = contactService;
    }
}
