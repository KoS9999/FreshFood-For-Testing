package utevn.ff.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import utevn.ff.entities.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

}
