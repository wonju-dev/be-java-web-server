package http.request;

import http.HttpHeader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HttpRequest {

    private static final Logger logger = LoggerFactory.getLogger(HttpRequest.class);

    private final HttpRequestLine httpRequestLine;
    private final HttpHeader httpHeader;
    private final String body;

    private HttpRequest(HttpRequestLine httpRequestLine, HttpHeader httpHeader, String body) {
        this.httpRequestLine = httpRequestLine;
        this.httpHeader = httpHeader;
        this.body = body;
    }

    public static HttpRequest from(InputStream in) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        HttpRequestLine httpRequestLine = readHttpRequestLine(br);
        HttpHeader httpHeader = readHttpRequestHeader(br);

        String body = "";
        String contentLength = httpHeader.getHeader("Content-Length");
        if (contentLength != null) {
            body = readBodyMessage(br, Integer.parseInt(contentLength));
            logger.debug("request body: {}", body);
        }

        return new HttpRequest(httpRequestLine, httpHeader, body);
    }

    private static HttpRequestLine readHttpRequestLine(BufferedReader br) throws IOException {
        String requestLine = br.readLine();
        if (requestLine == null) {
            return null;
        }
        logger.debug("requestLine : {}", requestLine);

        return HttpRequestLine.from(requestLine);
    }

    private static HttpHeader readHttpRequestHeader(BufferedReader br) throws IOException {
        List<String> lines = new ArrayList<>();
        String line = br.readLine();
        if (line == null) {
            return new HttpHeader();
        }
        lines.add(line);

        while(!(line = br.readLine()).equals("")) {
            logger.debug("request header : {}", line);
            lines.add(line);
        }
        Map<String, String> requestHeader = HttpRequestUtils.parseRequestHeader(lines);

        return HttpHeader.from(requestHeader);
    }

    private static String readBodyMessage(BufferedReader br, int contentLength) throws IOException {
        char[] body = new char[contentLength];
        br.read(body, 0, contentLength);
        return String.copyValueOf(body);
    }

    public boolean isLogin() {
        String sessionId = httpHeader.getHeader("Cookie");
        // TODO 세션매니저를 사용하여 로그인 여부 반환
        return true;
    }

    public HttpMethod getMethod() {
        return httpRequestLine.getMethod();
    }

    public String getUrl() {
        return httpRequestLine.getUrl();
    }

    public String getHttpVersion() {
        return httpRequestLine.getHttpVersion();
    }

    public String getHttpHeader(String name) {
        return httpHeader.getHeader(name);
    }

    public String getBody() {
        return body;
    }
}
