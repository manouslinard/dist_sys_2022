package gr.hua.dit.dissys.service;

import gr.hua.dit.dissys.entity.AverageUser;
import gr.hua.dit.dissys.entity.ERole;
import gr.hua.dit.dissys.entity.Role;
import gr.hua.dit.dissys.entity.VerificationCode;
import gr.hua.dit.dissys.repository.RoleRepository;
import gr.hua.dit.dissys.repository.UserRepository;

import gr.hua.dit.dissys.repository.VerificationRep;
import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.transaction.Transactional;

import java.io.UnsupportedEncodingException;
import java.util.*;

@Service
public class LessorServiceImpl implements LessorService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private JdbcUserDetailsManager jdbcUserDetailsManager;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private JavaMailSender mailSender;
	@Autowired
	private VerificationRep verificationRep;

	@SuppressWarnings("unlikely-arg-type")
	private boolean isLessor(Set<Role> userRoles) {
		for (Role r : userRoles) {
			if (r.getName().name().equals("ROLE_LESSOR")) {
				return true;
			}
		}
		return false;
	}


	@Override
	@Transactional
	public List<AverageUser> getLessors() {
		List<AverageUser> users = userRepository.findAll();
		List<AverageUser> lessors = new ArrayList<>();

		for (AverageUser u : users) {
			if (isLessor(u.getRoles())) {
				lessors.add(u);
			}
		}

		return lessors;
	}

	@Override
	@Transactional
	public void saveLessor(AverageUser lessor) {
		Set<Role> roles = new HashSet<>();

		Role userRole = roleRepository.findByName(ERole.ROLE_LESSOR)
				.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
		roles.add(userRole);
		lessor.setRoles(roles);
//		if (lessor.getPassword() != null) {
//			lessor.setPassword(passwordEncoder.encode(lessor.getPassword()));
//		}

//		sendVerificationEmail(lessor);
		userRepository.save(lessor);
		registerLessor(lessor);
	}

	private void registerLessor(AverageUser lessor) {
		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		Set<Role> auth = lessor.getRoles();
		for (Role a : auth) {
			authorities.add(new SimpleGrantedAuthority(a.getName().name()));
		}

		User user = new User(lessor.getUsername(), lessor.getPassword(), authorities);
		//System.out.println(userRegistrationObject.getRole());
		jdbcUserDetailsManager.createUser(user);
	}


	@Override
	@Transactional
	public AverageUser findLessorById(int id) {
		List<AverageUser> lessors = getLessors();
		for (AverageUser l : lessors) {
			if (l.getId() == id) {
				return l;
			}
		}
		throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Lessor not found");
	}

	@Override
	@Transactional
	public void deleteLessorById(int id) {
		String l_username = findLessorById(id).getUsername();
		jdbcUserDetailsManager.deleteUser(l_username);
		userRepository.deleteById(id);
	}


	@Override
	@Transactional
	public AverageUser findLessor(String username) {
		List<AverageUser> lessors = getLessors();
		for (AverageUser l : lessors) {
			if (l.getUsername().equals(username)) {
				return l;
			}
		}
		throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Lessor not found");
	}


	@Override
	public void deleteLessor(String username) {
		AverageUser lessor = findLessor(username);
		jdbcUserDetailsManager.deleteUser(username);
		userRepository.delete(lessor);
	}

	public void sendVerificationEmail(VerificationCode user) {
		// Generate a verification token for the user
		String token = UUID.randomUUID().toString();
		//userRepository.save(user);

		// Construct the verification email
		String subject = "Please verify your email address";
		String text = "Please click the following link to verify your email address: "
				+ user.getVerificationCode();
		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(user.getEmail());
		message.setSubject(subject);
		message.setText(text);

		// Send the verification email
		mailSender.send(message);

	}

	public boolean verify(String verificationCode) {
		//System.out.println("VERIFICATION CODE: "+ verificationCode);
		List<VerificationCode> users = verificationRep.findAll();
		for (VerificationCode user : users) {
			//System.out.println("VERIFY: "+user.getVerificationCode());
			if (user.getVerificationCode().equals(verificationCode)) {
				AverageUser lessor = new AverageUser(user.getUsername(), user.getEmail(),user.getPassword(), user.getFirstName(), user.getLastName(),user.getAfm(),user.getPhone());
				saveLessor(lessor);
				verificationRep.delete(user);
				return true;
			}
		}
		return false;
	}
}

