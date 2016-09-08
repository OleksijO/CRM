package com.becomejavasenior.entity;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.Date;
@XmlRootElement
public class Note implements Serializable{
    private static final long serialVersionUID = 1L;

    private int id;
    private String note;
    private User creator;
    private Date dateCreate;
    private boolean delete;
    private Contact contact;
    private Company company;
    private Deal deal;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    public Date getDateCreate() {
        return dateCreate;
    }

    public void setDateCreate(Date dateCreate) {
        this.dateCreate = dateCreate;
    }

    public boolean isDelete() {
        return delete;
    }

    public void setDelete(boolean delete) {
        this.delete = delete;
    }

    public Contact getContact() {
        return contact;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public Deal getDeal() {
        return deal;
    }

    public void setDeal(Deal deal) {
        this.deal = deal;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (!(other instanceof Note)) return false;
        Note note = (Note) other;
        if (id != note.id) return false;
        return this.note != null ? this.note.equals(note.note) : note.note == null;
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + id;
        result = 31 * result + (note != null ? note.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Note{" +
                "id=" + id +
                ", note='" + note + '\'' +
                '}';
    }
}