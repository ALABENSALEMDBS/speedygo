package org.example.paiment;


public class UserDTO {

    private Long id;
    private String email;
    private String firstName;
    private String lastName;
    private String password;
    private String phone;
    private String address;
    private String city;
    private String zipCode;
    private Boolean active = true;


    public UserDTO(Long id, Boolean active, String zipCode, String city, String address, String phone, String password, String lastName, String firstName, String email) {
        this.id = id;
        this.active = active;
        this.zipCode = zipCode;
        this.city = city;
        this.address = address;
        this.phone = phone;
        this.password = password;
        this.lastName = lastName;
        this.firstName = firstName;
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public Boolean getActive() {
        return active;
    }

    public String getZipCode() {
        return zipCode;
    }

    public String getCity() {
        return city;
    }

    public String getAddress() {
        return address;
    }

    public String getPhone() {
        return phone;
    }

    public String getPassword() {
        return password;
    }

    public String getLastName() {
        return lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getEmail() {
        return email;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setEmail(String email) {
        this.email = email;
    }


}
