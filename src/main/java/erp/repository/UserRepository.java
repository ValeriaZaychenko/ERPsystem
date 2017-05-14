package erp.repository;

import erp.domain.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends CrudRepository<User, String> {

    User findByEmail(String email);
    User findFirstByEmail(String email);
    List<User> findAll();
    List<User> findAllByOrderByNameAsc();

    @Query("SELECT e.name FROM employees e WHERE e.id = :id")
    String findUserNameById(@Param("id")String id);
}

