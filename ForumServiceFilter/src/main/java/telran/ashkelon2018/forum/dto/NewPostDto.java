package telran.ashkelon2018.forum.dto;

import java.util.Set;

import com.mongodb.lang.NonNull;

import lombok.Getter;
@Getter
public class NewPostDto {
	
	@NonNull String title;
	String content;
	String author;
	Set<String> tags;
}
