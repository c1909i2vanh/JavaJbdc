/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bkap.entity;

/**
 *
 * @author GIANG
 */
public class Sinhvien {

    private int id;
    private String rollNumber;
    private String name;
    private String address;
    private String phoneNumber;
    private boolean gender;

    public Sinhvien() {
    }

    public Sinhvien(int id, String rollNumber, String name, String address, String phoneNumber, boolean gender) {
        this.id = id;
        this.rollNumber = rollNumber;
        this.name = name;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.gender = gender;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRollNumber() {
        return rollNumber;
    }

    public void setRollNumber(String rollNumber) {
        this.rollNumber = rollNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public boolean isGender() {
        return gender;
    }

    public void setGender(boolean gender) {
        this.gender = gender;
    }

    @Override
    public String toString() {
        return "Sinhvien{" + "id=" + id + ", rollNumber=" + rollNumber + ", name=" + name + ", address=" + address + ", phoneNumber=" + phoneNumber + ", gender=" + gender + '}';
    }

}
