package gr.hua.dit.dissys.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import gr.hua.dit.dissys.entity.Contract;

@Service
public class ContractDelServ {

	@Autowired
	private ContractService contractService;

    @Scheduled(fixedRate = 240000)
    public void delOutdatedContracts() {
    	Date currDate = new Date();
    	List<Contract> contracts = contractService.getContracts();
    	for(Contract c: contracts) {
    		Date endDate = parseDate(c.getEndDate());
    		if(currDate.after(endDate)) {
    			// Delete the outdated contract:
    			contractService.deleteContract(c.getId());
    		}
    	}
    	System.out.println("Outdated Contracts auto-deleted.");
    }
    
    private Date parseDate(String dateString) {
        String[] formats = {"yyyy-MM-dd", "dd/MM/yyyy", "MM/dd/yyyy"};
        for (String format : formats) {
            try {
                return new SimpleDateFormat(format).parse(dateString);
            } catch (ParseException e) {
                // ignore and try the next format
            }
        }
        return null;
    }
}
