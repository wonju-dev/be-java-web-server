package service;

import db.Database;
import http.common.Cookie;
import http.common.Session;
import http.request.HttpRequest;
import model.User;

import java.util.Optional;

public class AuthService {
    public Boolean isAuthenticated(HttpRequest request) {
        Cookie sessionCookie = request.getCookie(Session.SESSION_FIELD_NAME);
        if (sessionCookie == null) {
            return false;
        }
        Session session = Database.getSession(sessionCookie.getValue());
        if (session == null) {
            return false;
        }
        return session.isValid();
    }

    public Session getSession(HttpRequest request) {
        Cookie sessionCookie = request.getCookie(Session.SESSION_FIELD_NAME);
        return Database.getSession(sessionCookie.getValue());
    }

    public Optional<User> getUser(HttpRequest request) {
        return Optional.ofNullable(getSession(request).getUser());
    }
}