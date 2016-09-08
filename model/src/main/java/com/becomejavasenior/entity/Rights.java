package com.becomejavasenior.entity;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

@XmlRootElement
public class Rights implements Serializable{
    private static final long serialVersionUID = 1L;

    private int id;
    private User user;
    private boolean create;
    private boolean read;
    private boolean delete; //right
    private boolean change;
    private boolean export;
    private boolean deleted; //state

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    private SubjectType subjectType;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isCreate() {
        return create;
    }

    public void setCreate(boolean create) {
        this.create = create;
    }

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }

    public boolean isDelete() {
        return delete;
    }

    public void setDelete(boolean delete) {
        this.delete = delete;
    }

    public boolean isChange() {
        return change;
    }

    public void setChange(boolean change) {
        this.change = change;
    }

    public boolean isExport() {
        return export;
    }

    public void setExport(boolean export) {
        this.export = export;
    }

    public SubjectType getSubjectType() {
        return subjectType;
    }

    public void setSubjectType(SubjectType subjectType) {
        this.subjectType = subjectType;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (!(other instanceof Rights)) return false;
        Rights rights = (Rights) other;
        return id == rights.id;
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + id;
        return result;
    }

    @Override
    public String toString() {
        return "Rights{" +
                "id=" + id +
                ", create=" + create +
                ", read=" + read +
                ", delete=" + delete +
                ", change=" + change +
                ", export=" + export +
                '}';
    }
}