package com.auzmor.restapi;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import com.auzmor.restapi.entity.Account;
import com.auzmor.restapi.entity.Phone;
import com.auzmor.restapi.repo.AccountRepository;
import com.auzmor.restapi.repo.PhoneRepository;

@SpringBootApplication
@EnableCaching
public class RestApiPocApplication {

	public static void main(String[] args) {
		SpringApplication.run(RestApiPocApplication.class, args);
	}

	@Bean
	public MessageSource messageSource() {
	    ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();	     
	    messageSource.setBasename("classpath:messages");
	    messageSource.setDefaultEncoding("UTF-8");
	    return messageSource;
	}
	
	@Bean
	public LocalValidatorFactoryBean getValidator() {
	    LocalValidatorFactoryBean bean = new LocalValidatorFactoryBean();
	    bean.setValidationMessageSource(messageSource());
	    return bean;
	}
	
	@Bean
	public CommandLineRunner init(AccountRepository accountRepository, PhoneRepository phoneRepository) {
		
		return (args) -> {

			Account account1 = new Account();
			account1.setUsername("acc1");
			account1.setAuthId("acc1");
			
			Account account2 = new Account();
			account2.setUsername("acc2");
			account2.setAuthId("acc2");
			
			Account account3 = new Account();
			account3.setUsername("acc3");
			account3.setAuthId("acc3");
			
			Account account4 = new Account();
			account4.setUsername("acc4");
			account4.setAuthId("acc4");
			
			accountRepository.save(account1);
			accountRepository.save(account2);
			accountRepository.save(account3);
			accountRepository.save(account4);
			
			Phone acc1Phone1 = new Phone();
			acc1Phone1.setNumber("9966000001");
			acc1Phone1.setAccount(account1);			
			phoneRepository.save(acc1Phone1);
			
			Phone acc2Phone1 = new Phone();
			acc2Phone1.setNumber("9966000001");
			acc2Phone1.setAccount(account2);
			phoneRepository.save(acc2Phone1);
			
			Phone acc2Phone2 = new Phone();
			acc2Phone2.setNumber("9966000002");
			acc2Phone2.setAccount(account2);
			phoneRepository.save(acc2Phone2);
		};
	}
}
