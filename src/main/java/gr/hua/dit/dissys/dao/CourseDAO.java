package gr.hua.dit.dissys.dao;

import gr.hua.dit.dissys.entity.Lease;

import java.util.List;

public interface CourseDAO {

    public List<Lease> findAll();
    public void save(Lease course);

    public Lease findById(int id);
}
