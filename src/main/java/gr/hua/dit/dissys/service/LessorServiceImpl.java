package gr.hua.dit.dissys.service;

import gr.hua.dit.dissys.entity.AverageUser;
import gr.hua.dit.dissys.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class LessorServiceImpl implements LessorService{

    @Autowired
    private UserRepository lessorRepository;

    @Override
    @Transactional
    public List<AverageUser> getLessors() {
        return lessorRepository.findAll();
    }

    @Override
    @Transactional
    public void saveLessor(AverageUser lessor) {
        lessorRepository.save(lessor);
    }

    @Override
    @Transactional
    public AverageUser findLessor(int id) {
    	return lessorRepository.findById(id).get();
    }

    @Override
    @Transactional
    public void deleteLessor(int id) {
        lessorRepository.deleteById(id);
    }
}
