package com.mordvinovdsw.library.models;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Member{
    private SimpleIntegerProperty Id;
    private SimpleStringProperty Name;
    private SimpleStringProperty Phone;
    private SimpleStringProperty Email;
    private SimpleStringProperty RegisterDate;
    private SimpleStringProperty ExpairDate;
    private SimpleStringProperty Status;

        public Member(int id, String name, String phone, String email, String registerDate, String expairDate, String status) {
            this.Id = new SimpleIntegerProperty(id);
            this.Name = new SimpleStringProperty(name);
            this.Phone = new SimpleStringProperty(phone);
            this.Email = new SimpleStringProperty(email);
            this.RegisterDate = new SimpleStringProperty(registerDate);
            this.ExpairDate = new SimpleStringProperty (expairDate);
            this.Status = new SimpleStringProperty (status);
        }


        public int getId() {
        return Id.get();
    }
       

        public String getName() {return Name.get();}

        public String getPhone() {return Phone.get();}

  
    public String getEmail() {
        return Email.get();
    }

    public String getRegisterDate() {
        return RegisterDate.get();
    }

    public String getExpairDate() {
        return ExpairDate.get();
    }

    public String getStatus() {
        return Status.get();
    }

    @Override
    public String toString() {
        return getId() + " - " + getName();
    }
}
