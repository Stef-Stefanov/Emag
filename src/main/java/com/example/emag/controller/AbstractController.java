package com.example.emag.controller;

import com.example.emag.model.dto.ErrorDTO;
import com.example.emag.model.exceptions.BadRequestException;
import com.example.emag.model.exceptions.NotFoundException;
import com.example.emag.model.exceptions.UnauthorizedException;
import com.example.emag.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;

public abstract class AbstractController {
    @Autowired
    protected UserService userService;
    public static final String LOGGED = "LOGGED";
    public static final String USER_ID = "USER_ID";
    public static final String REMOTE_IP = "REMOTE_IP";

    @ExceptionHandler(value = NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorDTO handleNotFound(Exception e){
        return buildError(e, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDTO handleBadRequest(Exception e){
        return buildError(e, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = UnauthorizedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorDTO handleUnauthorized(Exception e){
        return buildError(e, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorDTO handleOtherExceptions(Exception e){
        return buildError(e, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ErrorDTO buildError(Exception e, HttpStatus status){
        e.printStackTrace();
        ErrorDTO dto = new ErrorDTO();
        dto.setMsg(e.getMessage());
        dto.setTime(LocalDateTime.now());
        dto.setStatus(status.value());
        return dto;
    }

    public long getLoggedUserId(HttpServletRequest req){
        HttpSession session = req.getSession();
        String ip = req.getRemoteAddr();
        System.out.println(session.getAttribute(LOGGED));
        System.out.println(session.getAttribute(USER_ID));
        System.out.println(session.getAttribute(REMOTE_IP));
        if(session.getAttribute(LOGGED) == null ||
                (!(boolean) session.getAttribute(LOGGED)) ||
                !session.getAttribute(REMOTE_IP).equals(ip)){
            throw new UnauthorizedException("You have to login! C");
        }
        return (long) session.getAttribute(USER_ID);
    }

    public void logUser(HttpServletRequest req, long id){
        HttpSession session = req.getSession();
        session.setAttribute(LOGGED, true);
        session.setAttribute(USER_ID, id);
        session.setAttribute(REMOTE_IP, req.getRemoteAddr());
    }
    protected void checkIfLogged(HttpServletRequest req){
        HttpSession session = req.getSession();
        if (session.isNew()){
            session.setAttribute(LOGGED,false);
            session.setAttribute(REMOTE_IP, req.getRemoteAddr());
            throw new UnauthorizedException("You are not logged in! A");
        }
        if (!(boolean) session.getAttribute(LOGGED)){
            throw new UnauthorizedException("You are not logged in! B");
        }
    }
    public boolean checkIfLoggedBoolean(HttpSession s){
        if(null == s.getAttribute(LOGGED)){
            s.setAttribute(LOGGED,false);
        }
        return (boolean) s.getAttribute(LOGGED);
    }
    public void checkIfAdmin(HttpServletRequest req){
        checkIfLogged(req);
        checkIpWithSessionIp(req);
        if ( ! userService.checkIfAdminUserId((long) req.getSession().getAttribute("USER_ID"))){
            throw new UnauthorizedException("You are not an administrator!");
        }
    }
    /**
     * Protects against session high jacking by comparing the session's logged IP with the request's IP.
     * If there is a mismatch the method invalidates the session.
     * @param  req HttpServletRequest
     * @return boolean
     */
    public void checkIpWithSessionIp(HttpServletRequest req) {
        checkIfLogged(req);
        if ( ! req.getRemoteAddr().equals(req.getSession().getAttribute(REMOTE_IP))) {
            req.getSession().invalidate();
            throw new BadRequestException("Possible session high jacking");
        }
    }
}
