package telran.ashkelon2018.forum.domain;

import java.time.LocalDateTime;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@Getter
@EqualsAndHashCode(of = { "user", "dateCreated" })
@ToString
public class Comment {
	String user;
	@Setter
	String message;
	LocalDateTime dateCreated;
	int likes;

	public Comment(String user, String message) {
		this.user = user;
		this.message = message;
		dateCreated = LocalDateTime.now();
	}
	public void addLike() {
		likes++;
	}
}