
package ca.qc.johnabbott.cs616.server.controller;

import ca.qc.johnabbott.cs616.server.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;


@RepositoryRestResource(path = "user")
public interface UserRepository extends CrudRepository<User, String> {

    User findUserByUsername(@Param(value = "username") String username);
    User findUserByEmail(@Param(value = "email") String email);
}
