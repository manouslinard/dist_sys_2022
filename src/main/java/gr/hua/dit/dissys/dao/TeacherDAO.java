package gr.hua.dit.dissys.dao;

import gr.hua.dit.dissys.entity.Lease;
import gr.hua.dit.dissys.entity.Lessor;

import java.util.List;

public interface TeacherDAO {

    public List<Lessor> findAll();
    public void save(Lessor teacher);

    public Lessor findById(int id);

    public void delete(int id);
}
