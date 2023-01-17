import http.Method;
import http.QueryParameters;
import http.RequestLine;
import http.Uri;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;
public class RequestLineTest {

    @DisplayName("쿼리문이 있는 요청을 받아 인스턴스로 저장")
    @Test
    void 쿼리문_존재_Test() {
        //given
        String userId = "javajigi";
        String password = "password";
        String name = "%EB%B0%95%EC%9E%AC%EC%84%B1";
        String email = "javajigi%40slipp.net";
        String request = "GET /user/create?" +
                "userId=" + userId +
                "&password=" + password +
                "&name=" + name +
                "&email=" + email +
                " HTTP/1.1";

        //when
        RequestLine requestLine = RequestLine.from(request);
        Uri uri = requestLine.getUri();
        QueryParameters queryParameters = uri.getQueryParameters();

        //then
        assertThat(requestLine.getMethod()).isEqualTo(Method.GET);
        assertThat(queryParameters.getParameter("userId")).isEqualTo(userId);
        assertThat(queryParameters.getParameter("password")).isEqualTo(password);
        assertThat(queryParameters.getParameter("name")).isEqualTo(name);
        assertThat(queryParameters.getParameter("email")).isEqualTo(email);
        assertThat(requestLine.getVersion()).isEqualTo("HTTP/1.1");

    }
    @DisplayName("쿼리문이 없는 요청을 받아 인스턴스로 저장")
    @Test
    void 쿼리문_존재X_Test() {
        //given
        String request = "GET /index.html HTTP/1.1";

        //when
        RequestLine requestLine = RequestLine.from(request);

        //then
        assertThat(requestLine.getMethod()).isEqualTo(Method.GET);
        assertThat(requestLine.getUri().getPath()).isEqualTo("/index.html");
        assertThat(requestLine.getVersion()).isEqualTo("HTTP/1.1");

    }
}