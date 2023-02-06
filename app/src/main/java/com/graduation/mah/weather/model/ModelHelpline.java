package com.graduation.mah.weather.model;

public class ModelHelpline {

   String Contact,Department;

    public ModelHelpline(String department, String contact) {
        Contact = contact;
        Department = department;
    }

    public String getContact() {
        return Contact;
    }

    public void setContact(String contact) {
        Contact = contact;
    }

    public String getDepartment() {
        return Department;
    }

    public void setDepartment(String department) {
        Department = department;
    }
}
