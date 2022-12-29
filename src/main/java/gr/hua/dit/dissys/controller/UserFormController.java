package gr.hua.dit.dissys.controller;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import gr.hua.dit.dissys.entity.AverageUser;
import gr.hua.dit.dissys.entity.ERole;
import gr.hua.dit.dissys.entity.Lease;
import gr.hua.dit.dissys.entity.Role;
import gr.hua.dit.dissys.payload.request.LeaseFormRequest;
import gr.hua.dit.dissys.repository.LeaseRepository;
import gr.hua.dit.dissys.repository.RoleRepository;
import gr.hua.dit.dissys.service.LeaseService;
import gr.hua.dit.dissys.service.LessorService;
import gr.hua.dit.dissys.service.TenantService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class UserFormController {

    @Autowired
    private LessorService lessorService;

    @Autowired
    private TenantService tenantService;

    @Autowired
    private LeaseService leaseService;
    
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private LeaseRepository leaseRepository;

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/teacherform")
    public String showLessorForm(Model model) {
    	AverageUser lessor = new AverageUser();
        model.addAttribute("teacher", lessor);
        return "add-teacher";
    }

    @GetMapping("/teacherlist")
    public String showLessorList(Model model) {
        List<AverageUser> teachers = lessorService.getLessors();
        model.addAttribute("teachers", teachers);
        return "list-teachers";

    }

    @GetMapping("/leaselist")
    public String showLeaseList(Model model) {
        List<Lease> leases = leaseService.getLeases();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String logged_in_username = auth.getName();
        List <Lease> userLeases = new ArrayList<>();
        for (Lease l: leases) {
        	List<AverageUser> users = l.getUsers();
        	for (AverageUser u: users) {
        		// appends lease list if req user is in it.
        		if(logged_in_username.equals(u.getUsername())){
        			userLeases.add(l);
        			break;	// stops if it finds req user.
        		}
        	}
        }        
        model.addAttribute("leases", userLeases);
        return "list-leases";
    }
    
    @PostMapping(path = "/teacherform")
    public String saveLessor(@ModelAttribute("teacher") AverageUser lessor) {
    	setBlankAttr(lessor);
        lessorService.saveLessor(lessor);
        return "redirect:/";

    }


    @GetMapping("/tenantform")
    public String showTenantForm(Model model) {
        AverageUser tenant = new AverageUser();
//        HashSet<Role> set= new HashSet();
//        set.add(new Role(ERole.ROLE_TENANT));
//        tenant.setRoles(set);
        model.addAttribute("tenant", tenant);
        return "add-tenant";
    }

    @GetMapping("/tenantlist")
    public String showTenantList(Model model) {
        List<AverageUser> tenants = tenantService.getTenants();
        model.addAttribute("tenants", tenants);
        return "list-tenants";

    }

    @PostMapping(path = "/tenantform")
    public String saveTenant(@ModelAttribute("tenant") AverageUser tenant) {
    	setBlankAttr(tenant);
		tenantService.saveTenant(tenant);
        return "redirect:/";

    }

    @PostMapping(path = "/leaseform")
    public String saveLease(@ModelAttribute("lease") LeaseFormRequest leaseFormRequest) {
        Lease lease = new Lease();
        lease.setTitle(leaseFormRequest.getTitle());
        lease.setCost(leaseFormRequest.getCost());
        lease.setAddress(leaseFormRequest.getAddress());
        lease.setDei(leaseFormRequest.getDei());
        lease.setDimos(leaseFormRequest.getDimos());
        lease.setStartDate(leaseFormRequest.getStartDate());
        lease.setEndDate(leaseFormRequest.getEndDate());
        lease.setSp_con(leaseFormRequest.getSp_con());
        lease.setReason(leaseFormRequest.getReason());
        AverageUser tenant = tenantService.findTenant(leaseFormRequest.getTenant_username());
        tenant.getUserLeases().add(lease);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String lessor_username = auth.getName();
        AverageUser lessor = lessorService.findLessor(lessor_username);
        lessor.getUserLeases().add(lease);
//        lease.setUsers(new ArrayList<AverageUser>());
//        lease.getUsers().add(lessor);
//        lease.getUsers().add(tenant);
        leaseService.saveLease(lease);

        return "redirect:/";
    }

    @GetMapping("/leaseform")
    public String showLeaseForm(Model model) {
        LeaseFormRequest leaseFormRequest = new LeaseFormRequest();
//        HashSet<Role> set= new HashSet();
//        set.add(new Role(ERole.ROLE_TENANT));
//        tenant.setRoles(set);
        model.addAttribute("lease", leaseFormRequest);
        return "add-lease";
    }
    

    private void setBlankAttr(AverageUser user) {
    	if (checkNullEmptyBlank(user.getPhone())) {
    		user.setPhone(null);
    	}
    	if (checkNullEmptyBlank(user.getAfm())) {
    		user.setAfm(null);
    	}
    	if (checkNullEmptyBlank(user.getUsername())) {
    		user.setUsername(null);
    	}
    	if (checkNullEmptyBlank(user.getEmail())) {
    		user.setEmail(null);
    	}
    	if (checkNullEmptyBlank(user.getPassword())) {
    		user.setPassword(null);
    	}
    	if (checkNullEmptyBlank(user.getFirstName())) {
    		user.setFirstName(null);
    	}
    	if (checkNullEmptyBlank(user.getLastName())) {
    		user.setLastName(null);
    	}
    }
    
    
	private boolean checkNullEmptyBlank(String strToCheck) {
		// check whether the given string is null or empty or blank
		if (strToCheck == null || strToCheck.isEmpty() || strToCheck.isBlank()) {
			return true;
		} else {
			return false;
		}
	}
    

}