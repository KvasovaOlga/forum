package telran.ashkelon2018.forum.dao;

import java.time.LocalDate;
import java.util.Set;

import org.springframework.data.mongodb.repository.MongoRepository;

import telran.ashkelon2018.forum.domain.Post;

public interface ForumRepository extends MongoRepository<Post, String> {
	Iterable<Post> findByAuthor(String author);

	// @Query("{tags:{'$in':?0}}")
	Iterable<Post> findByTagsIn(Set<String> tags);

	Iterable<Post> findByDateCreatedBetween(LocalDate from, LocalDate to);

}
