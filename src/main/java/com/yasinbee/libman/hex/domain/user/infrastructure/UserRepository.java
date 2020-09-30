package com.yasinbee.libman.hex.domain.user.infrastructure;

import com.yasinbee.libman.hex.domain.user.core.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
}
