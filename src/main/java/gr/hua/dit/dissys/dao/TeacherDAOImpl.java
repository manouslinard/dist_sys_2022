package gr.hua.dit.dissys.dao;

import gr.hua.dit.dissys.entity.Lease;
import gr.hua.dit.dissys.entity.Lessor;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.util.List;

@Repository
public class TeacherDAOImpl implements TeacherDAO{
    @Autowired
    private EntityManager entityManager;

    @Override
    public List<Lessor> findAll() {
        Session session = entityManager.unwrap(Session.class);
        Query query = session.createQuery("from Teacher", Lessor.class);
        List<Lessor> teachers = query.getResultList();
        return teachers;
    }

    @Override
    public void save(Lessor teacher) {
        Lessor ateacher = entityManager.merge(teacher);
    }

    @Override
    public Lessor findById(int id) {
        return entityManager.find(Lessor.class, id);
    }

    @Override
    public void delete(int id) {
        Lessor teacher = entityManager.find(Lessor.class, id);
        entityManager.remove(teacher);
    }


}
