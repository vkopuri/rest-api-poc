package com.auzmor.restapi.repo;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.auzmor.restapi.entity.Account;

@Repository
public interface AccountRepository extends CrudRepository<Account, Long> {
	Optional<Account> findByUsername(String username);
}
