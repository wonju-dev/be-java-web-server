package controller;

import http.request.HttpRequest;
import http.response.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.UserService;
import utils.FileIoUtils;
import utils.HttpMethod;
import utils.SessionManager;

import javax.naming.AuthenticationException;
import java.util.Map;

public class UserLoginController implements Controller {
    public static final String PATH = "/user/login";
    private static final Logger logger = LoggerFactory.getLogger(UserLoginController.class);
    private final UserService userService;

    public UserLoginController(UserService userService) {
        this.userService = userService;
    }

    @Override
    public HttpResponse service(HttpRequest httpRequest, HttpResponse httpResponse) {
        if (httpRequest.getHttpMethod().equals(HttpMethod.POST))
            return doPost(httpRequest, httpResponse);
        throw new IllegalArgumentException("Login Controller에 존재하지 않는 Http 메서드입니다.");
    }

    private HttpResponse doPost(HttpRequest httpRequest, HttpResponse httpResponse) {
        Map<String, String> params = FileIoUtils.parseQueryString(httpRequest.getRequestBody());
        try {
            userService.login(params.get("userId"), params.get("password"));
            httpResponse.setCookie(SessionManager.createSession(userService.findUser(params.get("userId"))));
            httpResponse.redirectHome();
        }
        catch (AuthenticationException e) {
            httpResponse.redirectLoginFailed();
        }
        return httpResponse;
    }
}