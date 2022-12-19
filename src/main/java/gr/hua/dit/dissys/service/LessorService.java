package gr.hua.dit.dissys.service;

import gr.hua.dit.dissys.entity.UserRegistration;

import java.util.List;

public interface LessorService {

    public List<UserRegistration> getLessors();
    public void saveLessor(UserRegistration lessor);

    public UserRegistration findLessor(String username);

    public void deleteLessor(String username);
}
