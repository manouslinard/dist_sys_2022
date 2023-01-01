package gr.hua.dit.dissys.controller;

import gr.hua.dit.dissys.entity.AverageUser;
import gr.hua.dit.dissys.entity.ERole;
import gr.hua.dit.dissys.entity.Role;
import gr.hua.dit.dissys.entity.UserRegistration;
import gr.hua.dit.dissys.payload.request.LoginRequest;
import gr.hua.dit.dissys.payload.request.SignupRequest;
import gr.hua.dit.dissys.payload.response.MessageResponse;
import gr.hua.dit.dissys.repository.RoleRepository;
import gr.hua.dit.dissys.repository.UserRepository;
import gr.hua.dit.dissys.service.AdminService;
import gr.hua.dit.dissys.service.LessorService;
import gr.hua.dit.dissys.service.TenantService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {
	
    @Autowired
    private UserRepository userRepository;
    
	
    @Autowired
    private JdbcUserDetailsManager jdbcUserDetailsManager;
    
    @Autowired
    private TenantService tenantServ;
    
    @Autowired
    private LessorService lessorServ;
    
    @Autowired
    private AdminService adminServ;
    
    @Autowired
    RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

//    @PostMapping("/signin")
//    public void authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
// 	
//    	UsernamePasswordAuthenticationToken authReq = new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword());
//
//    	Authentication auth = authReq.authenticated(loginRequest, authReq, null);
//        SecurityContextHolder.getContext().setAuthentication(auth);
//
////        UserDetailsImpl userDetails = (UserDetailsImpl) auth.getPrincipal();
////        List<String> roles = userDetails.getAuthorities().stream()
////                .map(item -> item.getAuthority())
////                .collect(Collectors.toList());
//        System.out.println("DET: "+auth.toString());
//    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Username is already taken!"));
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Email is already in use!"));
        }

        // Create new user's account
        AverageUser user = new AverageUser(signUpRequest.getUsername(),
                signUpRequest.getEmail(),
                passwordEncoder.encode(signUpRequest.getPassword()), signUpRequest.getFirstName(), signUpRequest.getLastName(),
                signUpRequest.getAfm(), signUpRequest.getPhone());

        Set<String> strRoles = signUpRequest.getRole();
        Set<Role> roles = new HashSet<>();

        if (strRoles == null) {
            Role userRole = roleRepository.findByName(ERole.ROLE_TENANT)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin":
                        Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(adminRole);

                        break;
                    case "lessor":
                        Role modRole = roleRepository.findByName(ERole.ROLE_LESSOR)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(modRole);

                        break;
                    default:
                        Role userRole = roleRepository.findByName(ERole.ROLE_TENANT)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(userRole);
                }
            });
        }

        user.setRoles(roles);
        userRepository.save(user);
        registerUser(user);
        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }
    

	private void registerUser(AverageUser new_user) {
    	List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
    	Set<Role> auth = new_user.getRoles();
    	for(Role a: auth) {
            authorities.add(new SimpleGrantedAuthority(a.getName().name()));    		
    	}
        
        User user = new User(new_user.getUsername(), new_user.getPassword(), authorities);
        //System.out.println(userRegistrationObject.getRole());
        jdbcUserDetailsManager.createUser(user);
    }

	
}