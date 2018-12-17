package telran.ashkelon2018.forum.controller;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import telran.ashkelon2018.forum.domain.Post;
import telran.ashkelon2018.forum.dto.DatePeriodDto;
import telran.ashkelon2018.forum.dto.NewCommentDto;
import telran.ashkelon2018.forum.dto.NewPostDto;
import telran.ashkelon2018.forum.dto.PostUpdateDto;
import telran.ashkelon2018.forum.service.ForumService;

@RestController
@RequestMapping("/forum")
public class ForumController {
	@Autowired
	ForumService forumService;

	@PostMapping("/post")
	public Post addNewPost(@RequestBody NewPostDto newPost) {
		return forumService.addNewPost(newPost);
	}

	@GetMapping("/post/{id}")
	public Post getPost(@PathVariable String id) {
		return forumService.getPost(id);
	}

	@DeleteMapping("/post/{id}")
	public Post removePost(@PathVariable String id, @RequestHeader("Authorization") String token) {
		return forumService.removePost(id, token);
	}

	@PutMapping("/post")
	public Post updatePost(@RequestBody PostUpdateDto postUpdate, @RequestHeader("Authorization") String token) {
		return forumService.updatePost(postUpdate, token);
	}

	@PutMapping("/post/like/{id}")
	public boolean addLike(@PathVariable String id) {
		return forumService.addLike(id);
	}

	@PutMapping("/post/{id}/comment")
	public Post addComment(@PathVariable String id, @RequestBody NewCommentDto newComment) {
		return forumService.addComment(id, newComment);
	}

	@PostMapping("/posts/tags")
	public Iterable<Post> findPostByTags(@RequestBody Set<String> tags) {
		return forumService.findPostByTags(tags);
	}

	@GetMapping("/posts/author/{author}")
	public Iterable<Post> findPostByAuthor(@PathVariable String author) {
		return forumService.findPostByAuthor(author);
	}

	@PostMapping("/posts/dates")
	public Iterable<Post> findPostByDates(@RequestBody DatePeriodDto datePeriod) {
		return forumService.findPostByDates(datePeriod);
	}

}
