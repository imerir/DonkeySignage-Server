package Donkey.WebSite;

import Donkey.Database.Entity.UserAndPrivileges.UserEntity;
import Donkey.Database.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Locale;

@ControllerAdvice
public class AnnotationAdvice {

    @Autowired
    UserRepository userRepository;

    @Autowired
    ResourceBundleMessageSource rbms;

    @ModelAttribute("user")
    public UserEntity getCurrentUser() {
        if(SecurityContextHolder.getContext().getAuthentication() == null || !(SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof UserEntity)){
            return null;
        }
        return (UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    @ModelAttribute("langs")
    public HashMap<String, String> getLangs(){
        rbms.setFallbackToSystemLocale(false);
        HashMap<String, String> languages = new HashMap<>();
        String defaultMessage = "default";
        HashMap<String, Locale> availableLocales = new HashMap<>();
        for (Locale locale : Locale.getAvailableLocales()) {
            String msg = rbms.getMessage("currentLocale", null, defaultMessage, locale);
            if(locale.getLanguage().equals("en")){
                availableLocales.put(locale.getLanguage(), locale);
            }
            else if (!defaultMessage.equals(msg) && !availableLocales.containsKey(locale.getLanguage())){
                availableLocales.put(locale.getLanguage(), locale);
            }
        }
        for (String c : availableLocales.keySet()){
            languages.put(c, availableLocales.get(c).getDisplayLanguage(availableLocales.get(c)));
        }
        return languages;
    }

    @ModelAttribute("lang")
    public String dispLocal(){
        Locale locale = LocaleContextHolder.getLocale();
        HashMap<String, String> langs = getLangs();
        String localeSrt = locale.getLanguage();
        if(langs.keySet().contains(localeSrt)){
            return localeSrt;
        }

        return "en";
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ModelAndView handleMaxSizeException(
            MaxUploadSizeExceededException exc,
            HttpServletRequest request,
            HttpServletResponse response) {

        ModelAndView modelAndView = new ModelAndView("Media/uploadForm");
        modelAndView.getModel().put("error", "FILE_TO_LARGE");
        modelAndView.getModel().put("user", getCurrentUser());
        modelAndView.getModel().put("langs", getLangs());
        modelAndView.getModel().put("lang", dispLocal());
        return modelAndView;
    }

}
