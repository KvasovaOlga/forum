package telran.ashkelon2018.forum.service;

import java.time.LocalDateTime;
import java.util.Set;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import telran.ashkelon2018.forum.configuration.AccountConfiguration;
import telran.ashkelon2018.forum.configuration.AccountUserCredentials;
import telran.ashkelon2018.forum.dao.UserAccountRepository;
import telran.ashkelon2018.forum.domain.UserAccount;
import telran.ashkelon2018.forum.dto.UserProfileDto;
import telran.ashkelon2018.forum.dto.UserRegDto;
import telran.ashkelon2018.forum.exceptions.NotAuthorizedException;
import telran.ashkelon2018.forum.exceptions.UserConflictException;

@Service
public class AccounServiceImpl implements AccountService {
	@Autowired
	UserAccountRepository userRepository;
	@Autowired
	AccountConfiguration accountConfiguration;

	@Override
	public UserProfileDto addUser(UserRegDto user, String token) {
		AccountUserCredentials credentials = accountConfiguration.tokenDecode(token);
		if (userRepository.existsById(credentials.getLogin())) {
			throw new UserConflictException();
		}
		String hashPassword = BCrypt.hashpw(credentials.getPassword(), BCrypt.gensalt());
		UserAccount userAccount = UserAccount.builder().login(credentials.getLogin()).password(hashPassword)
				.firstName(user.getFirstName()).lastName(user.getLastName()).role("User")
				.expDate(LocalDateTime.now().plusDays(accountConfiguration.getExpPeriod())).build();
		userRepository.save(userAccount);
		return convertToUserProfileDto(userAccount);
	}

	private UserProfileDto convertToUserProfileDto(UserAccount user) {
		return UserProfileDto.builder().login(user.getLogin()).firstName(user.getFirstName())
				.lastName(user.getLastName()).roles(user.getRoles()).build();
	}

	@Override
	public UserProfileDto editUser(UserRegDto user, String token) {
		AccountUserCredentials credentials = accountConfiguration.tokenDecode(token);
		UserAccount userAccount = userRepository.findById(credentials.getLogin()).get();
		if (user.getLastName() != null) {
			userAccount.setLastName(user.getLastName());
		}
		if (user.getFirstName() != null) {
			userAccount.setFirstName(user.getFirstName());
		}
		return convertToUserProfileDto(userAccount);
	}

	@Override

	public UserProfileDto removeUser(String login, String token) {
		AccountUserCredentials credentials = accountConfiguration.tokenDecode(token);
		UserAccount userAccount1 = userRepository.findById(credentials.getLogin()).get();
		
		 Set<String> roles = userAccount1.getRoles();
		 boolean hasRight = roles.stream().anyMatch(s -> "Admin".equals(s) ||
		 "Moderator".equals(s));
		 hasRight = hasRight || credentials.getLogin().equals(login);
		 if (!hasRight) {
		 throw new NotAuthorizedException("Forbidden");
		 }
		UserAccount userAccount = userRepository.findById(login).orElse(null);
//		if (!login.equals(credentials.getLogin())) {
//			preCheckRole(userAccount1);
//		}
		if (userAccount != null) {
			userRepository.delete(userAccount);
		}
		return convertToUserProfileDto(userAccount);

	}

	@Override
	public Set<String> addRole(String login, String role, String token) {
		AccountUserCredentials credentials = accountConfiguration.tokenDecode(token);
		UserAccount userAccount1 = userRepository.findById(credentials.getLogin()).orElse(null);
		preCheckRole(userAccount1);
		UserAccount userAccount = userRepository.findById(login).orElse(null);
		if (userAccount != null) {
			userAccount.addRole(role);
			userRepository.save(userAccount);
		} else {
			return null;
		}
		return userAccount.getRoles();
	}

	public void preCheckRole(UserAccount userAccount1) throws RuntimeException {
		if (!(userAccount1.hasRole("Admin") || userAccount1.hasRole("Moderator"))) {
			throw new NotAuthorizedException("No permission to do it");
		}
	}

	@Override
	public Set<String> removeRole(String login, String role, String token) {
		AccountUserCredentials credentials = accountConfiguration.tokenDecode(token);
		UserAccount userAccount1 = userRepository.findById(credentials.getLogin()).orElse(null);
		preCheckRole(userAccount1);
		UserAccount userAccount = userRepository.findById(login).orElse(null);
		if (userAccount != null) {
			userAccount.removeRole(role);
			userRepository.save(userAccount);
		} else {
			return null;
		}
		return userAccount.getRoles();
	}

	@Override
	public void changePassword(String password, String token) {
		AccountUserCredentials credentials = accountConfiguration.tokenDecode(token);
		UserAccount userAccount = userRepository.findById(credentials.getLogin()).get();
		String hashPassword = BCrypt.hashpw(password, BCrypt.gensalt());
		userAccount.setPassword(hashPassword);
		userAccount.setExpDate(LocalDateTime.now().plusDays(accountConfiguration.getExpPeriod()));
		userRepository.save(userAccount);
	}

	@Override
	public UserProfileDto logIn(String token) {
		// FIX
		AccountUserCredentials credentials = accountConfiguration.tokenDecode(token);
		UserAccount userAccount = userRepository.findById(credentials.getLogin()).get();

		return convertToUserProfileDto(userAccount);
	}

}
