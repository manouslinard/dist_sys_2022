package gr.hua.dit.dissys.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(	name = "verification")
public class VerificationCode {

    public VerificationCode(String verificationCode, String firstName, String lastName, String email, String username, String password, String afm, String phone, String roles) {
        this.verificationCode = verificationCode;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.username = username;
        this.password = password;
        this.afm = afm;
        this.phone = phone;
        this.roles = roles;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "verification_code")
    private String verificationCode;

    @Column(name = "first_name")
    //@NotBlank(message = "Please enter the first name")
    //@Size(max = 30, message = "Name should not be greater than 30 characters")
    private String firstName;

    @Column(name = "last_name")
    //@NotBlank(message = "Please enter the last name")
    //@Size(max = 30, message = "Name should not be greater than 30 characters")
    private String lastName;

    @Column(name = "email", unique = true)

    //@Size(max = 50)
    private String email;

   @Column(name = "username")
    private String username;

    //@NotBlank
    @Column(name = "password")
    private String password;



    @Column(name = "afm", unique = true)
    //@NotBlank(message = "Please enter your AFM")

    private String afm;


    @Column(name = "phone")
    private String phone;

    @Column(name = "user_roles")
    private String roles ;

    public VerificationCode() {

    }

    public String getVerificationCode() {
        return verificationCode;
    }

    public void setVerificationCode(String verificationCode) {
        this.verificationCode = verificationCode;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAfm() {
        return afm;
    }

    public void setAfm(String afm) {
        this.afm = afm;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getRoles() {
        return roles;
    }

    public void setRoles(String roles) {
        this.roles = roles;
    }


}
