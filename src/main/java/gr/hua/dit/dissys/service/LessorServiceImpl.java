package gr.hua.dit.dissys.service;

import gr.hua.dit.dissys.entity.Lessor;
import gr.hua.dit.dissys.entity.UserRegistration;
import gr.hua.dit.dissys.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class LessorServiceImpl implements LessorService{

    @Autowired
    private UserRepository lessorRepository;

    @Override
    @Transactional
    public List<UserRegistration> getLessors() {
        return lessorRepository.findAll();
    }

    @Override
    @Transactional
    public void saveLessor(UserRegistration lessor) {
        lessorRepository.save(lessor);
    }

    @Override
    @Transactional
    public UserRegistration findLessor(String username) {
    	return lessorRepository.findByUsername(username).get();
    }

    @Override
    @Transactional
    public void deleteLessor(String username) {
    	if (lessorRepository.existsByUsername(username)) {
    		UserRegistration u = lessorRepository.findByUsername(username).get();
    		lessorRepository.delete(u);
    	}
    }
}
