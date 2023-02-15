package gr.hua.dit.dissys.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import gr.hua.dit.dissys.entity.AverageUser;
import gr.hua.dit.dissys.entity.Contract;
import gr.hua.dit.dissys.entity.ERole;
import gr.hua.dit.dissys.entity.Lease;
import gr.hua.dit.dissys.entity.Role;
import gr.hua.dit.dissys.payload.request.LeaseFormRequest;
import gr.hua.dit.dissys.repository.LeaseRepository;
import gr.hua.dit.dissys.repository.RoleRepository;
import gr.hua.dit.dissys.repository.UserRepository;
import gr.hua.dit.dissys.service.AdminService;
import gr.hua.dit.dissys.service.ContractService;
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
import org.springframework.validation.BindingResult;
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
	private ContractService contractService;

	@Autowired
	private RoleRepository roleRepository;
	@Autowired
	private LeaseRepository leaseRepository;

	@Autowired
	private AdminService adminService;

	@Autowired
	private UserRepository userRepository;
	
	@GetMapping("/")
	public String index() {
		return "index";
	}

	@GetMapping("/lessorform")
	public String showLessorForm(Model model) {
		AverageUser lessor = new AverageUser();
		model.addAttribute("lessor", lessor);
		return "add-lessor";
	}

	@GetMapping("/lessorlist")
	public String showLessorList(Model model) {
		List<AverageUser> lessors = lessorService.getLessors();
		model.addAttribute("lessors", lessors);
		return "list-lessors";

	}

	@GetMapping("/leaselist")
	public String showLeaseList(Model model) {
		List<Lease> leases = leaseService.getLeases();
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String logged_in_username = auth.getName();
		List<Lease> userLeases = new ArrayList<>();
		for (Lease l : leases) {
			List<AverageUser> users = l.getUsers();
			for (AverageUser u : users) {
				// appends lease list if req user is in it.
				if (logged_in_username.equals(u.getUsername())) {
					userLeases.add(l);
					break; // stops if it finds req user.
				}
			}
		}
		model.addAttribute("leases", userLeases);
		return "list-leases";
	}

	@GetMapping("/contractlist")
	public String showContractList(Model model) {
		List<Contract> contracts = contractService.getContracts();
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String logged_in_username = auth.getName();
		List<LeaseFormRequest> userContracts = new ArrayList<>();
		for (Contract c : contracts) {
			List<AverageUser> users = c.getUsers();
			for (AverageUser u : users) {
				// appends lease list if req user is in it.
				if (logged_in_username.equals(u.getUsername())) {
					// TODO: assign lessor & tenant.
					LeaseFormRequest lR = assignContract(c);
					userContracts.add(lR);
					break; // stops if it finds req user.
				}
			}
		}
		model.addAttribute("contracts", userContracts);
		return "list-contracts";
	}

	private LeaseFormRequest assignContract(Contract c) {
		List<AverageUser> tar_users = c.getUsers();
		String lessorUsername = null;
		String tenantUsername = null;
		for (AverageUser u : tar_users) {
			if (lessorUsername == null) {
				lessorUsername = u.getUsername();
			}
			else if (tenantUsername == null) {
				tenantUsername = u.getUsername();
			}
		}
		return new LeaseFormRequest(c.getTitle(), c.getAddress(), c.getTk(), c.getDimos(), c.getReason(), String.valueOf(c.getCost()),
				c.getStartDate(), c.getEndDate(), c.getSp_con(), c.getDei(), tenantUsername, lessorUsername);
	}

	@PostMapping(path = "/lessorform")
	public String saveLessor(@ModelAttribute("lessor") AverageUser lessor) {
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
		// System.out.println("Start Date: "+leaseFormRequest.getStartDate());
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
		double cost = 0;
		if (!checkNullEmptyBlank(leaseFormRequest.getCost())) {
			cost = Double.valueOf(leaseFormRequest.getCost());
		}
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

	@GetMapping("/leaseupdate")
	public String showLeaseUpdateForm(Model model) {
		LeaseFormRequest leaseFormRequest = new LeaseFormRequest();
		model.addAttribute("lease", leaseFormRequest);
		return "update-lease";
	}

	@PostMapping(path = "/leaseupdate")
	public String updateLease(@ModelAttribute("lease") LeaseFormRequest leaseFormRequest) {
		// System.out.println("Start Date: "+leaseFormRequest.getStartDate());
		if (checkNullEmptyBlank(leaseFormRequest.getTitle())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Title should not be blank.");
		}
		if (!startEarlierThanEnd(leaseFormRequest.getStartDate(), leaseFormRequest.getEndDate())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Start Date should be before End Date.");
		}
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String lessor_username = auth.getName();
		AverageUser current_lessor = lessorService.findLessor(lessor_username);
		Lease oldLease = leaseService.findLeaseByTitle(leaseFormRequest.getTitle());

		if (!checkNullEmptyBlank(leaseFormRequest.getAddress())) {
			oldLease.setAddress(leaseFormRequest.getAddress());
		}
		if (!checkNullEmptyBlank(leaseFormRequest.getDei())) {
			oldLease.setDei(leaseFormRequest.getDei());
		}
		if (!checkNullEmptyBlank(leaseFormRequest.getDimos())) {
			oldLease.setDimos(leaseFormRequest.getDimos());
		}
		if (!checkNullEmptyBlank(leaseFormRequest.getEndDate())) {
			oldLease.setEndDate(leaseFormRequest.getEndDate());
		}
		if (!checkNullEmptyBlank(leaseFormRequest.getStartDate())) {
			oldLease.setStartDate(leaseFormRequest.getStartDate());
		}
		if (!checkNullEmptyBlank(leaseFormRequest.getReason())) {
			oldLease.setReason(leaseFormRequest.getReason());
		}
		if (!checkNullEmptyBlank(leaseFormRequest.getSp_con())) {
			oldLease.setSp_con(leaseFormRequest.getSp_con());
		}
		if (!checkNullEmptyBlank(leaseFormRequest.getNew_title())) {
			oldLease.setTitle(leaseFormRequest.getNew_title());
		}
		if (!checkNullEmptyBlank(leaseFormRequest.getTk())) {
			oldLease.setTk(leaseFormRequest.getTk());
		}
		if (!checkNullEmptyBlank(leaseFormRequest.getCost())) {
			oldLease.setCost(Double.valueOf(leaseFormRequest.getCost()));
		}
		if (!checkNullEmptyBlank(String.valueOf(leaseFormRequest.getTenant_username()))) {
			for(AverageUser users:oldLease.getUsers()){
				if(!users.getUsername().equals(lessor_username)){
					oldLease.getUsers().remove(users);
				}
			}
			AverageUser tenant = tenantService.findTenant(leaseFormRequest.getTenant_username());
			tenant.getUserLeases().add(oldLease);
		}

		leaseService.saveLease(oldLease);
	//	lessor.getUserLeases().add(lease);

//        lease.setUsers(new ArrayList<AverageUser>());
//        lease.getUsers().add(lessor);
//        lease.getUsers().add(tenant);
		//leaseService.saveLease(lease);

		return "redirect:/";
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
	public String saveAdmin(@ModelAttribute("admin") AverageUser admin, BindingResult bindingResult) {
	    if (userEmailExist(admin.getEmail())) {
	        bindingResult.rejectValue("email", "error.user", "Email already in use.");
	    }
	    
	    if(userUsernameExist(admin.getUsername())) {
	        bindingResult.rejectValue("username", "error.user", "Username already in use.");	    	
	    }
	    
	    if(admin.getAfm() != null) {
		    if(admin.getAfm()!="" && !isValidAFM(admin.getAfm())) {
		        bindingResult.rejectValue("afm", "error.user", "AFM should be exactly 11 digits.");	    	
		    }
	    }
	    
	    if (bindingResult.hasErrors()) {
	        return "add-admin";
	    }
		
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
			return true; // continues execution.
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			boolean bef = sdf.parse(startDate).before(sdf.parse(endDate));
			boolean same = sdf.parse(startDate).equals(sdf.parse(endDate));

			return bef || same;
		} catch (ParseException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Input Date should be in format 'yyyy-mm-dd'.");
		}
	}
	
	@GetMapping("/leasecom")
	public String showCommentForm(Model model) {
		LeaseFormRequest leaseFormRequest = new LeaseFormRequest();
		
		model.addAttribute("lease", leaseFormRequest);
		return "add-lease-com";
	}

	
	@PostMapping(path = "/leasecom")
	public String saveComment(@ModelAttribute("lease") LeaseFormRequest leaseFormRequest) {
		// System.out.println("Start Date: "+leaseFormRequest.getStartDate());
		if (checkNullEmptyBlank(leaseFormRequest.getTitle())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Title should not be blank.");
		}
		Lease lease= leaseService.findLeaseByTitle(leaseFormRequest.getTitle());
	
		lease.setTenantCom(leaseFormRequest.getTenant_com());
		leaseService.saveLease(lease);

		return "redirect:/";
	}


	private boolean userEmailExist(String email) {
		return userRepository.existsByEmail(email);
	}

	private boolean userUsernameExist(String username) {
		return userRepository.existsByUsername(username);
	}


	private boolean isValidAFM(String str) {
		// checks if input AFM is 11 digits:
	    return str.matches("\\d{11}");
	}
	
}