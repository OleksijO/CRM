package com.becomejavasenior.rest.impl;

import com.becomejavasenior.entity.Contact;
import com.becomejavasenior.rest.impl.wrapper.ContactWrapper;
import com.becomejavasenior.service.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping//(path="/rest") //this root uri folder set by map in web.xml
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
    public Contact get(@PathVariable int id){
        return contactService.getById(id);
    }

    @RequestMapping(path = "/contact/{id}", method = RequestMethod.PUT)
    public Contact update(@PathVariable int id, @RequestBody Contact contact, HttpServletResponse response){
        contact.setId(id);
        contactService.update(contact);
        return contact;
    }

    @RequestMapping(path = "/contact/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable int id, HttpServletResponse response){
        contactService.delete(id);
        response.setStatus(HttpServletResponse.SC_OK);
    }

    @RequestMapping(path = "/contact", method = RequestMethod.POST)
    public Contact save(@RequestBody Contact contact, HttpServletResponse response){
        contact.setId(contactService.insert(contact));
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





}
