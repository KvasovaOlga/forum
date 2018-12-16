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

import telran.ashkelon2018.forum.dto.UserProfileDto;
import telran.ashkelon2018.forum.dto.UserRegDto;
import telran.ashkelon2018.forum.service.AccountService;

@RestController
@RequestMapping("/account")
public class AccountManagmentController {
	@Autowired
	AccountService accounService;

	@PostMapping
	public UserProfileDto register(@RequestBody UserRegDto user, @RequestHeader("Authorization") String token) {
		return accounService.addUser(user, token);
	}

	@PutMapping
	public UserProfileDto update(@RequestBody UserRegDto user, @RequestHeader("Authorization") String token) {
		return accounService.editUser(user, token);
	}

	@DeleteMapping("/{login}")
	public UserProfileDto remove(@PathVariable String login, @RequestHeader("Authorization") String token) {
		return accounService.removeUser(login, token);
	}

	@PutMapping("/{login}/{role}")
	public Set<String> addRole(@PathVariable String login, @PathVariable String role,
			@RequestHeader("Authorization") String token) {
		return accounService.addRole(login, role, token);
	}

	@DeleteMapping("/{login}/{role}")
	public Set<String> removeRole(String login, String role, String token) {
		return accounService.removeRole(login, role, token);
	}

	@PutMapping("/password")
	public void changePassword(@RequestHeader("X-Authorization") String password,
			@RequestHeader("Authorization") String token) {
		accounService.changePassword(password, token);
	}

	@GetMapping("/login")
	public UserProfileDto logIn(@RequestHeader("Authorization") String token) {
		return accounService.logIn(token);
	}

}
