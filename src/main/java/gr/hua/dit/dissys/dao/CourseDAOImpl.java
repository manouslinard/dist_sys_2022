package gr.hua.dit.dissys.dao;

import gr.hua.dit.dissys.entity.Lease;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.util.List;

@Repository
public class CourseDAOImpl implements CourseDAO{

    @Autowired
    private EntityManager entityManager;


    @Override
    @Transactional
    public List<Lease> findAll() {
        Session session = entityManager.unwrap(Session.class);
        Query query = session.createQuery("from Course", Lease.class);
        List<Lease> courses = query.getResultList();
        return courses;
    }

    @Override
    @Transactional
    public void save(Lease course) {
        Lease acourse = entityManager.merge(course);
    }

    @Override
    @Transactional
    public Lease findById(int id) {
        return entityManager.find(Lease.class, id);
    }
}
