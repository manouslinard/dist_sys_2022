package gr.hua.dit.dissys.service;

import gr.hua.dit.dissys.entity.AverageUser;

import java.util.List;

public interface LessorService {

    public List<AverageUser> getLessors();
    public void saveLessor(AverageUser lessor);

    public AverageUser findLessorById(int id);

    public AverageUser findLessor(String username);
    
    public void deleteLessorById(int id);
    
    public void deleteLessor(String username);
}
