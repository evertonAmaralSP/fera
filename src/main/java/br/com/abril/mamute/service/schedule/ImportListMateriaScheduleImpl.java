package br.com.abril.mamute.service.schedule;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.mamute.service.pooling.ManagerPooling;
import br.com.abril.mamute.support.aws.AwsConfig;

@Service
public class ImportListMateriaScheduleImpl implements ImportListMateriaSchedule {

	@Autowired
	ManagerPooling managerPooling;
	
	@Autowired
	AwsConfig awsConfig;

  @Transactional(propagation = Propagation.REQUIRED)
  @Scheduled(cron = "0/30 * * * * ?")
	public void executeRotina(){
		managerPooling.processPoolingSources();
	}
  
  @Transactional(propagation = Propagation.REQUIRED)
  @Scheduled(cron = "0/30 * * * * ?")
	public void executeRotinaExportFileS3(){
		managerPooling.processPoolingExports();
	}
}
