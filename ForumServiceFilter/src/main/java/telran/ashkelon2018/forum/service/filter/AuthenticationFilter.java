package telran.ashkelon2018.forum.service.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

import telran.ashkelon2018.forum.configuration.AccountConfiguration;
import telran.ashkelon2018.forum.configuration.AccountUserCredentials;
import telran.ashkelon2018.forum.dao.UserAccountRepository;
import telran.ashkelon2018.forum.domain.UserAccount;

@Service
@Order(1)
public class AuthenticationFilter implements Filter {
	@Autowired
	UserAccountRepository repository;
	@Autowired
	AccountConfiguration configuration;

	@Override
	public void doFilter(ServletRequest reqs, ServletResponse resp, FilterChain chain)
			throws IOException, ServletException {

		HttpServletRequest request = (HttpServletRequest) reqs;
		HttpServletResponse response = (HttpServletResponse) resp;
		String path = request.getServletPath();
		// System.out.println(path);
		String method = request.getMethod();
		// System.out.println(method);
		boolean f1 = path.startsWith("/account") && !"POST".equals(method);
		boolean f2 = path.startsWith("/forum") && !path.startsWith("/forum/posts");
		if (f1 || f2) {
			String token = request.getHeader("Authorization");
			if (token == null) {
				response.sendError(401, "Unauthorized");
				return;
			}
			AccountUserCredentials credentials = null;
			try {
				credentials = configuration.tokenDecode(token);
			} catch (Exception e) {
				response.sendError(401, "No authorization");
				return;
			}
			UserAccount userAccount = repository.findById(credentials.getLogin()).orElse(null);
			if (userAccount == null) {
				response.sendError(401, "User is not found");
				return;
			} else {
				if (!BCrypt.checkpw(credentials.getPassword(), userAccount.getPassword())) {
					response.sendError(403, "Forbidden");
					return;
				}
			}
		}
		chain.doFilter(request, response);
	}

}
