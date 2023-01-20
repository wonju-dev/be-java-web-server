package webserver.controller;

import db.UserDatabase;
import model.User;
import webserver.ControllerInterceptor;
import webserver.Service.AuthService;
import webserver.UserListViewResolver;
import webserver.annotation.ControllerInfo;
import webserver.domain.ContentType;
import webserver.domain.ModelAndView;
import webserver.domain.RequestMethod;
import webserver.domain.StatusCodes;
import webserver.domain.request.Request;
import webserver.domain.response.Response;
import webserver.exception.HttpRequestException;
import webserver.utils.HttpCookieUtils;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class UserController implements Controller {

    private AuthService authService;

    //TODO: 싱글톤으로 클래스 초기화
    public UserController(){
        this.authService = new AuthService();
    }


    @ControllerInfo(path = "/user/create", methodName = "userCreate", queryStr = {"userId", "password", "name", "email"}, method = RequestMethod.POST)
    public void userCreate(Map<String, String> queryStrs, Response response) {
        //TODO: authService.join의 리턴값 핸들링, javascript 문법 하드 코딩 개선
        byte[] result = authService.addUser(queryStrs.get("userId"), queryStrs.get("password"), queryStrs.get("name"), queryStrs.get("email"));
        response.ok(StatusCodes.OK, ("<script>alert('계정 생성이 완료되었습니다.'); window.location.href = 'http://localhost:8080/index.html';</script>").getBytes(), ContentType.TEXT_HTML);
    }

    @ControllerInfo(path = "/user/login", methodName = "userLogout", queryStr = {"userId", "password"}, method = RequestMethod.POST)
    public void userLogin(Map<String, String> queryStrs, Response response){
        try{
            String userId = authService.login(queryStrs.get("userId"), queryStrs.get("password"));
            if(queryStrs.get("Cookie") == null)     //when client got no session for now, generate a new cookie and add it on response header
                response.addCookieOnHeader(HttpCookieUtils.generateCookie(userId).toString());
            response.redirect(StatusCodes.SEE_OTHER, ("<script>alert('로그인이 완료되었습니다.');</script>").getBytes(), ContentType.TEXT_HTML,"/index.html");
        }catch(HttpRequestException e){
            response.redirect(e.getErrorCode(), e.getMsg().getBytes(), ContentType.TEXT_HTML, e.getRedirectURL());
        }
    }

    @ControllerInfo(path = "/user/logout", methodName = "userLogout", method = RequestMethod.GET) //TODO : logout 요청 POST로 처리하기
    public void userLogout(Map<String, String> queryStrs, Response response){
        String sessionId = queryStrs.get("Cookie");
        response.ok(StatusCodes.OK, ("<script>alert('로그아웃 완료되었습니다.'); window.location.href = 'http://localhost:8080/index.html';</script>").getBytes(), ContentType.TEXT_HTML);
        HttpCookieUtils.cookieInvalidation(sessionId);
        response.addCookieOnHeader(HttpCookieUtils.cookieInvalidation(sessionId));
    }

    @ControllerInfo(path = "/user/list", methodName = "userList", method = RequestMethod.GET)
    public void userList(Map<String, String> queryStrs, Response response) throws IOException {
        ModelAndView mv = new ModelAndView();
        List<User> allUsers = UserDatabase.findAll();
        mv.setViewName("/user/list.html");
        mv.addViewModel("user", allUsers);
        UserListViewResolver.makeUserListView(mv,response);   //TODO : 컨트롤러에서 뷰리졸버를 호출하는 방향이 아닌 RequestHandler에서 호출할 수 있도록
    }

    @Override
    public void chain(Request req, Response res) throws HttpRequestException, IOException {
        ControllerInterceptor.executeController(UserController.class, req, res);
    }
}