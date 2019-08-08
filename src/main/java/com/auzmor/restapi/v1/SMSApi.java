/**
 * 
 */
package com.auzmor.restapi.v1;

import static com.auzmor.restapi.common.AppConstants.CONST_CRLF_LF;
import static com.auzmor.restapi.common.AppConstants.CONST_FROM;
import static com.auzmor.restapi.common.AppConstants.CONST_STOP;
import static com.auzmor.restapi.common.AppConstants.CONST_TO;
import static com.auzmor.restapi.common.AppConstants.MSG_KEY_SMSAPI_INBOUND_OK;
import static com.auzmor.restapi.common.AppConstants.MSG_KEY_SMSAPI_OUTBOUND_BLOCKED;
import static com.auzmor.restapi.common.AppConstants.MSG_KEY_SMSAPI_OUTBOUND_LIMIT;
import static com.auzmor.restapi.common.AppConstants.MSG_KEY_SMSAPI_OUTBOUND_OK;

import java.time.Duration;

import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.auzmor.restapi.dto.ApiResponse;
import com.auzmor.restapi.dto.SMSData;
import com.auzmor.restapi.entity.Account;
import com.auzmor.restapi.exception.ParameterNotFoundException;
import com.auzmor.restapi.repo.AccountRepository;
import com.auzmor.restapi.repo.PhoneRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author KVK
 * Api for SMS Operations
 */
@RestController
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@RequestMapping("/api/v1")
public class SMSApi {

	private final Logger log = LoggerFactory.getLogger(getClass());

	@Autowired
	private PhoneRepository phoneRepository;
	
	@Autowired
	private AccountRepository accountRepository;

	@Autowired
	private MessageSource messageSource;

	@Autowired
	private RedisTemplate<String, String> redisTemplate;

	private ObjectMapper mapper = new ObjectMapper();

	@PostMapping(path="/inbound/sms")
	public ResponseEntity<ApiResponse> inboundSms(@RequestBody @Valid SMSData smsInput){
		try {
			log.debug(mapper.writeValueAsString(smsInput));
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		Account account = getLoginUserAccount();
		if(!phoneRepository.existsByAccountIdAndNumber(account.getId(), smsInput.getTo())) {
			throw new ParameterNotFoundException(CONST_TO);
		}
		String text = smsInput.getText().replaceAll(CONST_CRLF_LF, "");
		if(CONST_STOP.equals(text)) {
			redisTemplate.opsForValue().set(smsInput.getFrom()+":"+smsInput.getTo(), CONST_STOP, Duration.ofHours(4));
		}
		return ResponseEntity.ok(new ApiResponse(messageSource.getMessage(MSG_KEY_SMSAPI_INBOUND_OK, null, LocaleContextHolder.getLocale()), null));		
	}

	@PostMapping(path="/outbound/sms")
	public ResponseEntity<ApiResponse> outboundSms(@RequestBody @Valid SMSData smsInput){
		try {
			log.debug(mapper.writeValueAsString(smsInput));
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}

		String toFromKey = smsInput.getTo()+":"+smsInput.getFrom();
		if(redisTemplate.opsForValue().get(toFromKey) != null ) {
			return ResponseEntity.badRequest().body(new ApiResponse(null, messageSource.getMessage(MSG_KEY_SMSAPI_OUTBOUND_BLOCKED, null, LocaleContextHolder.getLocale())));
		}
		Account account = getLoginUserAccount();
		if(!phoneRepository.existsByAccountIdAndNumber(account.getId(), smsInput.getFrom())) {
			throw new ParameterNotFoundException(CONST_FROM);
		}

		String fromCounterKey = smsInput.getFrom()+":COUNTER";
		String fromCounter = redisTemplate.opsForValue().get(fromCounterKey);
		if( fromCounter == null ) {
			redisTemplate.opsForValue().set(fromCounterKey, "0", Duration.ofHours(24));
			/*
			 * Line for local testing purpose
			 */
			//redisTemplate.opsForValue().set(fromCounterKey, "0", Duration.ofMinutes(1));
		}else {
			if(Integer.valueOf(fromCounter) >= 5 ) {
				return ResponseEntity.badRequest().body(new ApiResponse(null, messageSource.getMessage(MSG_KEY_SMSAPI_OUTBOUND_LIMIT, new String[] {smsInput.getFrom()}, LocaleContextHolder.getLocale())));
			}
		}
		redisTemplate.opsForValue().increment(fromCounterKey);
		return ResponseEntity.ok(new ApiResponse(messageSource.getMessage(MSG_KEY_SMSAPI_OUTBOUND_OK, null, LocaleContextHolder.getLocale()), null));		
	}
	
	private Account getLoginUserAccount() {
		String username = (String) SecurityContextHolder.getContext().getAuthentication().getName();
		Account account = accountRepository.findByUsername(username).orElseThrow(
				() -> new UsernameNotFoundException("User Not Found with -> username or email : " + username));
		System.out.println("888888888888888: "+account);
		return account;
	}
}
