package com.scm20.helpers;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpSession;

// this class is for to remove the Successful registration message after showing for once
@Component
public class sessionHelper {
    public static void removeMessage(){
        try{
            System.out.println("removing message from session");
            HttpSession session=((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest().getSession();
            session.removeAttribute("message");
        }catch(Exception e){
            System.out.println("Error in sessionhemper : "+e);
            e.printStackTrace();
        }

    }
}
