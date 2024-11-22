package utevn.ff.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import utevn.ff.entities.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	User findByEmail(String email);

}
