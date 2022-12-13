package gr.hua.dit.dissys.service;

import gr.hua.dit.dissys.entity.Lessor;

import java.util.List;

public interface LessorService {

    public List<Lessor> getLessors();
    public void saveLessor(Lessor lessor);

    public Lessor findLessor(int id);

    public void deleteLessor(int id);
}
