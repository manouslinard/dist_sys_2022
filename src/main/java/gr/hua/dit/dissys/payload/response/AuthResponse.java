package gr.hua.dit.dissys.payload.response;

import java.util.List;
import java.util.Set;

import gr.hua.dit.dissys.entity.Role;

public class AuthResponse {
    private int id;
    private String username;
    private String email;
    private Set<Role> roles;

    public AuthResponse(int id, String username, String email, Set<Role> set) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.roles = set;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public Set<Role> getRoles() {
        return roles;
    }
}