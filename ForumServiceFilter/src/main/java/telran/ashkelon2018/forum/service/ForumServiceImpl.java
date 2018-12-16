package telran.ashkelon2018.forum.service;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import telran.ashkelon2018.forum.dao.ForumRepository;
import telran.ashkelon2018.forum.domain.Comment;
import telran.ashkelon2018.forum.domain.Post;
import telran.ashkelon2018.forum.dto.DatePeriodDto;
import telran.ashkelon2018.forum.dto.NewCommentDto;
import telran.ashkelon2018.forum.dto.NewPostDto;
import telran.ashkelon2018.forum.dto.PostUpdateDto;
import telran.ashkelon2018.forum.exceptions.PostNotFoundException;

@Service
public class ForumServiceImpl implements ForumService {
	@Autowired
	ForumRepository repository;
	

	@Override
	public Post addNewPost(NewPostDto newPost) {
		Post post = new Post(newPost.getTitle(), newPost.getContent(), newPost.getAuthor(), newPost.getTags());
		return repository.save(post);
	}

	@Override
	public Post getPost(String id) {
		return repository.findById(id).orElseThrow(PostNotFoundException::new);
	}

	@Override
	public Post removePost(String id) {
		Post post = repository.findById(id).orElseThrow(PostNotFoundException::new);
		repository.deleteById(id);
		return post;
	}

	@Override
	public Post updatePost(PostUpdateDto postUpdate) {
		Post post = repository.findById(postUpdate.getId()).orElseThrow(PostNotFoundException::new);
		if (postUpdate.getContent() != null) {
			post.setContent(postUpdate.getContent());
		}
		if (postUpdate.getTitle() != null) {
			post.setTitle(postUpdate.getTitle());
		}
		if (postUpdate.getTags() != null) {
			post.setTags(postUpdate.getTags());
		}
		repository.save(post);
		return post;
	}

	@Override
	public boolean addLike(String id) {
		if (!repository.existsById(id)) {
			return false;
		}
		Post post = repository.findById(id).orElseThrow(PostNotFoundException::new);
		post.addLike();
		repository.save(post);
		return true;
	}

	@Override
	public Post addComment(String id, NewCommentDto newComment) {
		Post post = repository.findById(id).orElseThrow(PostNotFoundException::new);
		post.addComment(new Comment(newComment.getUser(), newComment.getMessage()));
		return post;
	}

	@Override
	public Iterable<Post> findPostByTags(Set<String> tags) {
		return repository.findByTagsIn(tags);
	}

	@Override
	public Iterable<Post> findPostByAuthor(String author) {
		return repository.findByAuthor(author);
	}

	@Override
	public Iterable<Post> findPostByDates(DatePeriodDto datePeriod) {

		return repository.findByDateCreatedBetween(datePeriod.getFrom(), datePeriod.getTo());
	}

}
