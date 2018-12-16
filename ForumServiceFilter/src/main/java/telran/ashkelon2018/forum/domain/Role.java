package telran.ashkelon2018.forum.domain;

public enum Role implements IRole {
	ADMIN, MODERATOR, USER;

	@Override
	public String getName() {
		return this.name();
	}
}
