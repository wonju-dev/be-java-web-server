package view;

import db.Database;
import enums.ContentType;
import enums.HttpStatus;
import model.User;
import request.HttpRequest;
import response.HttpResponse;
import session.HttpSessionManager;
import utils.FileIoUtils;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Collection;


public class UserListView implements View{

    private static final String COOKIE_SESSION_KEY = "sid";
    private static final String CONTENT_LENGTH_HEADER_KEY = "Content-Length";
    @Override
    public void render(HttpRequest request, HttpResponse response, Model data) throws IOException, URISyntaxException {
        System.out.println("UserListController doGet");
        String sessionKey = request.getCookie(COOKIE_SESSION_KEY);
        System.out.println("session key: "+sessionKey);
        // 쿠키 값이 유효한 경우 == 로그인 된 경우
        if (HttpSessionManager.getSession(sessionKey) != null) {
            ContentType contentType = ContentType.HTML;
            response.setStatus(HttpStatus.OK);
            response.setContentType(contentType);
            System.out.println(Database.findAll());
            Collection<User> users = (Collection<User>) data.getAttribute("users");
            byte[] body = getTable(users).getBytes();
            response.addToHeader(CONTENT_LENGTH_HEADER_KEY, String.valueOf(body.length));
            response.setBody(body);
            return;
        }

        //로그인하지 않은 상태일 경우 로그인 페이지(login.html)로 이동
        response.redirect("/user/login.html");
    }

    private String getTable(Collection<User> users) throws IOException, URISyntaxException {
        int idx = 0;

        StringBuilder content = new StringBuilder();

        for (User user: users) {
            content.append("<tbody>")
                    .append("<tr><th scope=\"row\">")
                    .append(idx++)
                    .append("</th> <td>")
                    .append(user.getUserId())
                    .append("</td> <td>")
                    .append(user.getName())
                    .append("</td> <td>")
                    .append(user.getEmail())
                    .append("</td><td><a href=\"#\" class=\"btn btn-success\" role=\"button\">수정</a></td></tr>");
        }

        byte[] bytes = FileIoUtils.loadFileFromClasspath("./templates/user/list.html");
        String html = new String(bytes);
        html = html.replace("<tbody>",content);
        return html;
    }
}