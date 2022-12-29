package gr.hua.dit.dissys.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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
import gr.hua.dit.dissys.service.AdminService;
import gr.hua.dit.dissys.service.LeaseService;
import gr.hua.dit.dissys.service.LessorService;
import gr.hua.dit.dissys.service.TenantService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
import org.springframework.web.server.ResponseStatusException;

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

    @Autowired
    private AdminService adminService;
    
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
        //System.out.println("Start Date: "+leaseFormRequest.getStartDate());
    	if (checkNullEmptyBlank(leaseFormRequest.getTitle())) {
    		throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Title should not be blank.");    
    	}
        if (!startEarlierThanEnd(leaseFormRequest.getStartDate(), leaseFormRequest.getEndDate())) {
    		throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Start Date should be before End Date.");        	
        }
        String title = leaseFormRequest.getTitle();
        String dimos = leaseFormRequest.getDimos();
        String start_date = leaseFormRequest.getStartDate();
        String end_date = leaseFormRequest.getEndDate();
        String sp_con = leaseFormRequest.getSp_con();
        String reason = leaseFormRequest.getReason();
        String tk = null;
        if (!checkNullEmptyBlank(leaseFormRequest.getTk())) {
        	tk = leaseFormRequest.getTk().toString();        
        }
    	String address = leaseFormRequest.getAddress();
        double cost = leaseFormRequest.getCost();
        String dei = null;
        if (!checkNullEmptyBlank(leaseFormRequest.getDei())) {
        	dei = leaseFormRequest.getDei().toString();
        }

    	Lease lease = new Lease(title, address, tk, dimos, reason, cost, start_date, end_date, sp_con, dei);
        
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
	
	@GetMapping("/adminlist")
    public String showAdminList(Model model) {
        List<AverageUser> admins = adminService.getAdmins();
        model.addAttribute("admins", admins);
        return "list-admins";

    }
    
  	@PostMapping(path = "/adminform")
    public String saveAdmin(@ModelAttribute("admin") AverageUser admin) {
    	setBlankAttr(admin);
        adminService.saveAdmin(admin);
        return "redirect:/";

    }

    @GetMapping("/adminform")
    public String showAdminForm(Model model) {
    	AverageUser admin = new AverageUser();
        model.addAttribute("admin", admin);
        return "add-admin";
    }


    private boolean startEarlierThanEnd(String startDate, String endDate) {
    	if (checkNullEmptyBlank(startDate) || checkNullEmptyBlank(endDate)) {
    		return true;	// continues execution.
    	}
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    	try {
    		boolean bef = sdf.parse(startDate).before(sdf.parse(endDate));
    		boolean same = sdf.parse(startDate).equals(sdf.parse(endDate));
    		
			return bef || same;
		} catch (ParseException e) {
			return false;
		}
    }
}