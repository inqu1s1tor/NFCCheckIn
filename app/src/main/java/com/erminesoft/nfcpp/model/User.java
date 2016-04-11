package com.erminesoft.nfcpp.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import java.util.Date;

@Table(name = "USER")
public class User extends Model {

    @Column(name = "object_id")
    private String objectId;

    @Column(name = "name")
    private String name;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "created")
    private Date created;

    @Column(name = "user_roles")
    private String userRoles;

    @Column(name = "user_total_time_today")
    private String userTotalTimeToday;

    @Column(name = "email", unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
    private String email;

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public String getUserRoles() {
        return userRoles;
    }

    public void setUserRoles(String userRoles) {
        this.userRoles = userRoles;
    }

    public String getUserTotalTimeToday() {
        return userTotalTimeToday;
    }

    public void setUserTotalTimeToday(String userTotalTimeToday) {
        this.userTotalTimeToday = userTotalTimeToday;
    }
}
