package Donkey.Api;

import Donkey.Tools.Json;
import biweekly.ICalendar;
import biweekly.component.VEvent;
import biweekly.io.json.JCalWriter;
import biweekly.io.text.ICalReader;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/")
public class ApiController {

    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public ResponseEntity<?> test() throws IOException {

        return new ResponseEntity<>("",HttpStatus.OK);
    }


}
