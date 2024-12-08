package utevn.ff.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import utevn.ff.service.UserDetailService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {

    @Autowired
    private UserDetailService userDetailService; // Inject service to check if user exists

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, 
                                        org.springframework.security.core.AuthenticationException exception) throws IOException {
        String errorMessage = "Tài khoản hoặc mật khẩu không chính xác!";

        if (exception instanceof BadCredentialsException) {
            // Kiểm tra xem tài khoản có tồn tại trong hệ thống không
            String username = request.getParameter("username");
            boolean userExists = userDetailService.isUserExist(username); // Check if user exists

            if (!userExists) {
                errorMessage = "Tài khoản chưa tồn tại! Hãy đăng ký.";
            }
        } else if (exception instanceof InternalAuthenticationServiceException) {
            // Tài khoản không tồn tại
            errorMessage = "Tài khoản chưa tồn tại! Hãy đăng ký.";
        }

        // Redirect tới trang đăng nhập với thông báo lỗi
        String redirectUrl = UriComponentsBuilder.fromUriString("/login")
                                                .queryParam("error", errorMessage)
                                                .toUriString();

        response.sendRedirect(redirectUrl);
    }
}
