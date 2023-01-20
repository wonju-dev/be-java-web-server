package Controller;

import Request.HttpRequest;
import Request.StatusCode;
import Response.HttpResponse;
import Response.HttpResponseBody;
import Response.HttpResponseHeaders;
import Response.HttpResponseStartLine;
import db.SessionDb;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpResponseUtil;
import util.LoginUtil;

import java.util.Objects;

public class LoginController implements Controller {
    public static final String PATH = "/user/login";
    public static final String INDEX_HTML = "/index.html";
    public static final String USER_LOGIN_FAILED_HTML = "/user/login_failed.html";
    private final Logger logger = LoggerFactory.getLogger(LoginController.class);
    private static LoginController loginController = null;

    public static LoginController getInstance(){
        if(Objects.isNull(loginController)){
            loginController = new LoginController();
        }
        return loginController;
    }
    @Override
    public HttpResponse createResponse(HttpRequest httpRequest) {
        logger.debug("select LoginController");
        String sessionId;
        if (LoginUtil.checkUserInfoMatch(httpRequest)) {
            sessionId = SessionDb.saveNewSession(httpRequest);
            return responseRedirectIndex(httpRequest, sessionId);
        }
        return responseLoginFailed(httpRequest);
    }

    private HttpResponse responseRedirectIndex(HttpRequest httpRequest, String sessionId) {
        HttpResponse response = redirect(INDEX_HTML, httpRequest);
        response.putHeader("Set-Cookie", "sid=" + sessionId + "; Path=/");

        return response;
    }

    private HttpResponse responseLoginFailed(HttpRequest httpRequest) {
        byte[] body = HttpResponseUtil.generateBody(USER_LOGIN_FAILED_HTML);
        HttpResponse response = new HttpResponse().startLine(new HttpResponseStartLine(StatusCode.OK, httpRequest.getProtocol()))
                .headers(new HttpResponseHeaders(USER_LOGIN_FAILED_HTML, body.length))
                .body(new HttpResponseBody(body));
        return response;
    }

}
