package com.auzmor.restapi.repo;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.auzmor.restapi.entity.Phone;

@Repository
public interface PhoneRepository extends CrudRepository<Phone, Long> {
	public boolean existsByAccountIdAndNumber(Long accountId, String number);
}
