package com.bikeescape.api.service;

import com.bikeescape.api.model.Authority;
import com.bikeescape.api.repository.RoleRepository;
import javassist.tools.rmi.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service("roleService")
@Transactional
public class RoleServiceImpl implements RoleService {

	private final RoleRepository roleRepository;

	@Autowired
	public RoleServiceImpl(RoleRepository roleRepository) {
		this.roleRepository = roleRepository;
	}

	@Override
	@Transactional(readOnly = true)
	public List<Authority> findAll() throws ObjectNotFoundException {
		
			List<Authority> authorities = roleRepository.findAll();
			if(authorities.isEmpty()){
				throw new ObjectNotFoundException("Authority list is empty");
			}
			return authorities;

	}

}
