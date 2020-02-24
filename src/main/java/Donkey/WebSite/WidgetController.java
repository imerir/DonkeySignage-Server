package Donkey.WebSite;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class WidgetController {

    @RequestMapping(value ="/widget", method = RequestMethod.GET)
    public String getWidget(){
        return "Media/widget";
    }
}
