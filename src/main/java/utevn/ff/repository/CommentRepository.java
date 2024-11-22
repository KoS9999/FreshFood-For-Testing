package utevn.ff.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import utevn.ff.entities.Comment;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long>{

}
