package telran.ashkelon2018.forum.dto;

import java.util.Set;

import lombok.Getter;

@Getter
public class PostUpdateDto {
	String id;
	String title;
	String content;
	Set<String> tags;
}
