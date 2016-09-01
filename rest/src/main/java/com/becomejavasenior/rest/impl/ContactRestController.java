package com.becomejavasenior.rest.impl;

import com.becomejavasenior.entity.Contact;
import com.becomejavasenior.service.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController("/rest")
public class ContactRestController {

    @Autowired
    ContactService contactService;

    @RequestMapping(path = "/contact", method = RequestMethod.GET)
    @ResponseBody
    public List<Contact> getAll(HttpServletResponse response){
        return contactService.getAll();
    }

    @RequestMapping(path = "/contact/{id}", method = RequestMethod.GET)
    @ResponseBody
    public Contact get(@PathVariable int id){
        return contactService.getById(id);
    }

    @RequestMapping(path = "/contact/{id}", method = RequestMethod.POST)
    @ResponseBody
    public Contact update(@PathVariable int id, @RequestBody Contact contact, HttpServletResponse response){
        contact.setId(id);
        contactService.update(contact);
        return contact;
    }

    @RequestMapping(path = "/contact/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public void delete(@PathVariable int id, HttpServletResponse response){
        contactService.delete(id);
        response.setStatus(HttpServletResponse.SC_OK);
    }

    @RequestMapping(path = "/contact/{id}", method = RequestMethod.PUT)
    @ResponseBody
    public Contact save(@RequestBody Contact contact, HttpServletResponse response){
        contact.setId(contactService.insert(contact));
        return contact;
    }

    @RequestMapping(path = "/contact", method = RequestMethod.HEAD)
    @ResponseBody
    public void ping(HttpServletResponse response){
        response.setStatus(HttpServletResponse.SC_OK);
    }

    @RequestMapping(path = "/contact", method = RequestMethod.OPTIONS)
    @ResponseBody
    public void options(HttpServletResponse response){
        //TODO
        response.setStatus(HttpServletResponse.SC_OK);
    }





}
