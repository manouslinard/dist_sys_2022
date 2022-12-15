package gr.hua.dit.dissys.service;

import gr.hua.dit.dissys.entity.Lessor;
import gr.hua.dit.dissys.repository.LessorRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class LessorServiceImpl implements LessorService{

    @Autowired
    private LessorRepository lessorRepository;

    @Override
    @Transactional
    public List<Lessor> getLessors() {
        return lessorRepository.findAll();
    }

    @Override
    @Transactional
    public void saveLessor(Lessor lessor) {
        lessorRepository.save(lessor);
    }

    @Override
    @Transactional
    public Lessor findLessor(int id) {
    	return lessorRepository.findById(id).get();
    }

    @Override
    @Transactional
    public void deleteLessor(int id) {
        lessorRepository.deleteById(id);
    }
}
