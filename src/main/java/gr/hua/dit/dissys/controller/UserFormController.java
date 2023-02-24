package gr.hua.dit.dissys.controller;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import gr.hua.dit.dissys.entity.*;
import gr.hua.dit.dissys.payload.request.LeaseFormRequest;
import gr.hua.dit.dissys.repository.LeaseRepository;
import gr.hua.dit.dissys.repository.RoleRepository;
import gr.hua.dit.dissys.repository.UserRepository;
import gr.hua.dit.dissys.repository.VerificationRep;
import gr.hua.dit.dissys.service.*;

import net.bytebuddy.utility.RandomString;
import org.aspectj.apache.bcel.classfile.Utility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
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
import org.springframework.web.servlet.ModelAndView;

import javax.mail.MessagingException;
import javax.swing.text.Utilities;

@Controller
public class UserFormController {

	@Autowired
	private PasswordEncoder encoder;

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
	private VerificationRep verificationRep;

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
	public String saveLessor(@ModelAttribute("lessor") AverageUser lessor, BindingResult bindingResult) throws MessagingException, UnsupportedEncodingException {
		String s = checkRegisterErrors(lessor, bindingResult, "add-lessor");
		if (s!=null) {
			return s;
		}
		setBlankAttr(lessor);
		Random random = new Random();
		int verificationCode = 10000 + random.nextInt(90000); // Generates a random number between 10000 and 99999
		System.out.println(verificationCode);
		String randomCode = String.valueOf(verificationCode);
		lessor.setVerificationCode(randomCode);
		lessor.setEnabled(false);
		VerificationCode verification = new VerificationCode(randomCode,lessor.getFirstName(), lessor.getLastName(),lessor.getEmail(), lessor.getUsername(), encoder.encode(lessor.getPassword()), lessor.getAfm(), lessor.getPhone(),ERole.ROLE_LESSOR.name());
		try{
			lessorService.sendVerificationEmail(lessor);
			verificationRep.save(verification);
			return "verify_email";
		}
		catch(Exception e){
			System.out.println("Email failed");
		}
//		lessorService.saveLessor(lessor);
		return "redirect:/";

	}

	@GetMapping("/verify")
	public String verifyUser(@Param("code") String code) {
		if (lessorService.verify(code)) {
			return "verify_success";
		} else {
			return "verify_fail";
		}
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
	public String saveTenant(@ModelAttribute("tenant") AverageUser tenant, BindingResult bindingResult) {
		String s = checkRegisterErrors(tenant, bindingResult, "add-tenant");
		if (s!=null) {
			return s;
		}
		setBlankAttr(tenant);
		tenantService.saveTenant(tenant);
		return "redirect:/";

	}

	@PostMapping(path = "/leaseform")
	public String saveLease(@ModelAttribute("lease") LeaseFormRequest leaseFormRequest, BindingResult bindingResult) {
		// System.out.println("Start Date: "+leaseFormRequest.getStartDate());
		
		Lease l = null;
		try {
			l = leaseService.findLeaseByTitle(leaseFormRequest.getTitle());
			if(l != null) {
				bindingResult.rejectValue("title", "error.user", "Title already in use.");				
			}
		} catch(ResponseStatusException r) {
		}
		
		Contract c = null;
		try {
			c = contractService.findContractByTitle(leaseFormRequest.getTitle());
			if(c != null) {
				bindingResult.rejectValue("title", "error.user", "Title already in use.");				
			}
		} catch(ResponseStatusException r) {
		}

		AverageUser tenant = null;
		try {
			tenant = tenantService.findTenant(leaseFormRequest.getTenant_username());
		} catch(ResponseStatusException r) {
	        bindingResult.rejectValue("tenant_username", "error.user", "Tenant does not exist.");
		}		
		
	    
	    if (bindingResult.hasErrors()) {
	        return "add-lease";
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
	public String updateLease(@ModelAttribute("lease") LeaseFormRequest leaseFormRequest, BindingResult bindingResult) {
		// System.out.println("Start Date: "+leaseFormRequest.getStartDate());
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String lessor_username = auth.getName();
		AverageUser current_lessor = lessorService.findLessor(lessor_username);
		
		Lease oldLease = null;
		
		List<Lease> lessor_leases = current_lessor.getUserLeases();
		for(Lease l: lessor_leases) {
			if(l.getTitle().equals(leaseFormRequest.getTitle())) {
				oldLease=l;
				break;
			}
		}

		if(oldLease == null) {
			bindingResult.rejectValue("title", "error.user", "Title does not exist.");			
		}
		
		Lease l = null;
		try {
			l = leaseService.findLeaseByTitle(leaseFormRequest.getNew_title());
			if(l != null) {
				bindingResult.rejectValue("new_title", "error.user", "Title already in use.");
			}
		} catch(ResponseStatusException r) {
		}
		
		Contract c = null;
		try {
			c = contractService.findContractByTitle(leaseFormRequest.getNew_title());
			if(c != null) {
				bindingResult.rejectValue("new_title", "error.user", "Title already in use.");				
			}
		} catch(ResponseStatusException r) {
		}
				
		if (!checkNullEmptyBlank(String.valueOf(leaseFormRequest.getTenant_username()))) {
			try {
				AverageUser tenant = tenantService.findTenant(leaseFormRequest.getTenant_username());
			} catch(ResponseStatusException r) {
		        bindingResult.rejectValue("tenant_username", "error.user", "Tenant does not exist.");
			}
			
		}
		
		if (bindingResult.hasErrors()) {
	        return "update-lease";
	    }
		
		oldLease = leaseService.findLeaseByTitle(leaseFormRequest.getTitle());

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

	@GetMapping("/adminlist")
	public String showAdminList(Model model) {
		List<AverageUser> admins = adminService.getAdmins();
		model.addAttribute("admins", admins);
		return "list-admins";

	}

	@PostMapping(path = "/adminform")
	public String saveAdmin(@ModelAttribute("admin") AverageUser admin, BindingResult bindingResult) {
		String s = checkRegisterErrors(admin, bindingResult, "add-admin");
		if (s!=null) {
			return s;
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
	public String saveComment(@ModelAttribute("lease") LeaseFormRequest leaseFormRequest, BindingResult bindingResult) {
		// System.out.println("Start Date: "+leaseFormRequest.getStartDate());
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String logged_in_username = auth.getName();
		
		AverageUser tenant = tenantService.findTenant(logged_in_username); 
		
		List<Lease> leases = tenant.getUserLeases();
		
		for(Lease l: leases) {
			if(l.getTitle().equals(leaseFormRequest.getTitle())) {
				l.setTenantCom(leaseFormRequest.getTenant_com());
				leaseService.saveLease(l);

				return "redirect:/";
			}
		}
		
		bindingResult.rejectValue("title", "error.user", "Lease title not found.");
		return "add-lease-com";
		
	}


	private boolean userEmailExist(String email) {
		return userRepository.existsByEmail(email);
	}

	private boolean userUsernameExist(String username) {
		return userRepository.existsByUsername(username);
	}

	private boolean userAfmExist(String afm) {
		List<AverageUser> all_users = userRepository.findAll();
		for(AverageUser u: all_users) {
			if(u.getAfm()!=null && u.getAfm().equals(afm)) {
				return true;
			}
		}
		return false;
	}
	
	private boolean isValidAFM(String str) {
		// checks if input AFM is 11 digits:
	    return str.matches("\\d{11}");
	}
	
	private String checkRegisterErrors(AverageUser user,  BindingResult bindingResult, String htmlPage) {
		
		if (userEmailExist(user.getEmail())) {
	        bindingResult.rejectValue("email", "error.user", "Email already in use.");
	    }
	    
	    if(userUsernameExist(user.getUsername())) {
	        bindingResult.rejectValue("username", "error.user", "Username already in use.");	    	
	    }
	    
	    if(user.getAfm() != null) {
		    if(user.getAfm()!="" && !isValidAFM(user.getAfm())) {
		        bindingResult.rejectValue("afm", "error.user", "AFM should be exactly 11 digits.");	    	
		    } else if(user.getAfm()!="" && userAfmExist(user.getAfm())) {
		        bindingResult.rejectValue("afm", "error.user", "AFM already in use.");
		    }
	    }
	    
	    if (bindingResult.hasErrors()) {
	        return htmlPage;
	    }
		
	    return null;
	}
	
	
	private boolean checkNullEmptyBlank(String strToCheck) {
		// check whether the given string is null or empty or blank
		if (strToCheck == null || strToCheck.isEmpty() || strToCheck.isBlank()) {
			return true;
		} else {
			return false;
		}
	}
	
	@RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login(Model model, String error, String logout) {
        if (error != null)
            model.addAttribute("errorMsg", "Your username and password are invalid.");

        if (logout != null)
            model.addAttribute("msg", "You have been logged out successfully.");

        return "login";
    }

	
	
	
//    @RequestMapping(value = "/registerTenant", method = RequestMethod.GET)
//    public ModelAndView registerTenant() {
//        return new ModelAndView("registration-tenant", "user", new AverageUser());
//    }
//
//    @RequestMapping(value = "/registerTenant", method = RequestMethod.POST)
//    public ModelAndView processRegisterTenant(@ModelAttribute("user") AverageUser userRegistration) {
//    	// if user is tenant:
//    	setBlankAttr(userRegistration);
//    	tenantService.saveTenant(userRegistration);
//        return new ModelAndView("redirect:/");
//    }
//
//    @RequestMapping(value = "/registerLessor", method = RequestMethod.GET)
//    public ModelAndView registerLessor() {
//        return new ModelAndView("registration", "user", new AverageUser());
//    }
//
//    @RequestMapping(value = "/registerLessor", method = RequestMethod.POST)
//    public ModelAndView processRegisterLessor(@ModelAttribute("user") AverageUser userRegistration) {
//    	// if user is lessor:
//    	setBlankAttr(userRegistration);
//    	//System.out.println(userRegistration.getPassword());
//    	//System.out.println(userRegistration.getPhone() +", " + userRegistration.getAfm());
//    	lessorService.saveLessor(userRegistration);
//        return new ModelAndView("redirect:/");
//    }

	
}