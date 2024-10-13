package com.example.kitchensinkboot.web.jsf;

import java.util.List;

import org.springframework.stereotype.Component;

import com.example.kitchensinkboot.model.member.Member;
import com.example.kitchensinkboot.service.MemberRegistrationService;

import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;

@Component("memberJsf")
public class MemberJsf {


    private Member member;
    private final MemberRegistrationService memberRegistrationService;
    private final FacesContext facesContext;


    public MemberJsf(MemberRegistrationService memberRegistrationService, FacesContext facesContext) {
        this.memberRegistrationService = memberRegistrationService;
        this.facesContext = facesContext;
        initNewMember();
    }


    public Member getNewMember() {
        return member;
    }

    public void register() {
        try {
            memberRegistrationService.register(member);
            FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_INFO, "Registered!", "Registration successful");
            facesContext.addMessage(null, m);
            initNewMember();
        } catch (Exception e) {
            String errorMessage = getRootErrorMessage(e);
            FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, "Registration unsuccessful");
            facesContext.addMessage(null, m);
        }

    }

    private void initNewMember() {
        member = new Member();
    }

    private String getRootErrorMessage(Exception e) {
        // Default to general error message that registration failed.
        String errorMessage = "Registration failed. See server log for more information";
        if (e == null) {
            // This shouldn't happen, but return the default messages
            return errorMessage;
        }

        // Start with the exception and recurse to find the root cause
        Throwable t = e;
        while (t != null) {
            // Get the message from the Throwable class instance
            errorMessage = t.getLocalizedMessage();
            t = t.getCause();
        }
        // This is the root cause message
        return errorMessage;
    }

    public List<Member> getMembers() {
        return memberRegistrationService.getAll();
    }

}
