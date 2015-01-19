package br.com.abril.mamute.service.schedule;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.mamute.service.pooling.ManagerPooling;

@Service
public class ImportListMateriaScheduleImpl implements ImportListMateriaSchedule {

	@Autowired
	ManagerPooling managerPooling;

  @Transactional(propagation = Propagation.REQUIRED)
  @Scheduled(cron = "0/10 * * * * ?")
	public void executeRotina(){
		managerPooling.processPoolings();
		System.out.println("The time is now : " + new Date());
	}
}
