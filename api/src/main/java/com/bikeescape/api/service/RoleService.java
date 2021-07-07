package com.bikeescape.api.service;


import com.bikeescape.api.model.Authority;
import javassist.tools.rmi.ObjectNotFoundException;

import java.util.List;

public interface RoleService {

    List<Authority> findAll() throws ObjectNotFoundException;
}
