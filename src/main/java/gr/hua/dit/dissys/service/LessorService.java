package gr.hua.dit.dissys.service;

import gr.hua.dit.dissys.entity.AverageUser;
import gr.hua.dit.dissys.entity.VerificationCode;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import java.util.List;

public interface LessorService {

    public List<AverageUser> getLessors();
    public void saveLessor(AverageUser lessor);

    public AverageUser findLessorById(int id);

    public AverageUser findLessor(String username);
    
    public void deleteLessorById(int id);
    
    public void deleteLessor(String username);

    public void sendVerificationEmail(VerificationCode user);

    public boolean verify(String verificationCode);
}
