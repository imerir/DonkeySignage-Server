package Donkey.Widgets;

import Donkey.Database.Entity.WidgetConfigEntity;
import Donkey.Tools.FilesTools;
import Donkey.Tools.Json;
import biweekly.ICalendar;
import biweekly.component.VEvent;
import biweekly.io.text.ICalReader;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;

public class Calendar implements WidgetInterface{
    Logger logger = LogManager.getLogger();

    @Override
    public String getName() {
        return "Calendar";
    }

    @Override
    public String getId() {
        return "CALENDAR";
    }

    @Override
    public String getVersion() {
        return "0.1";
    }

    @Override
    public String getTemplate() throws IOException {

        return FilesTools.getINSTANCE().getFileContent("widgets/calendar.html");
    }

    @Override
    public String convertParam(String paramStr) {

        try {
            HashMap<String, Object> conf = Json.loadObject(paramStr);
            List<CalEvent> events = convertIcal((String) conf.get("URL"));
//            TODO Add Name of canlendar et for multible calandar
            HashMap<String, Object> toReturn = new HashMap<>();
            toReturn.put("day_start", conf.get("day_start"));
            toReturn.put("day_end", conf.get("day_end"));
            toReturn.put("calendars", events);
            return Json.stringify(toReturn);
        } catch (IOException e) {
            logger.catching(e);
            return null;
        }
    }


    @JsonIgnore
    @Override
    public List<WidgetConfDefinition> getParam() {
        WidgetConfDefinition url = new WidgetConfDefinition("URL", ConfType.TEXT, true, false, false, "", "",  null);
        WidgetConfDefinition dayStart = new WidgetConfDefinition("day_start", ConfType.NUMBER, true, false, false, "7", "",  null);
        WidgetConfDefinition dayEnd = new WidgetConfDefinition("day_end", ConfType.NUMBER, true, false, false, "19", "",  null);

        return Arrays.asList(url, dayStart, dayEnd);
    }

    @Override
    public Map<String, WidgetConfDefinition> getParam(String jsonValue) throws IOException {
        HashMap<String, Object> parsed = Json.loadObject(jsonValue);
        Map<String, WidgetConfDefinition> map = new HashMap<>();
        map.put("URL", new WidgetConfDefinition("URL", ConfType.TEXT, true, false, false, parsed.get("URL"), (String) parsed.get("URL"), null));

        map.put("day_start", new WidgetConfDefinition("day_start", ConfType.NUMBER, true, false, false, parsed.get("day_start"),  Integer.toString((Integer)parsed.get("day_start")), null));

        map.put("day_end", new WidgetConfDefinition("day_end", ConfType.NUMBER, true, false, false, parsed.get("day_end"), Integer.toString((Integer) parsed.get("day_end")), null));
        return  map;
    }

    @Override
    public boolean needUpdate(WidgetConfigEntity widgetConf) {
        return false;
    }

    class CalEvent{
        public long lastEdit;
        public long created;
        public long dtstart;
        public long dtstamp;
        public long dtend;
        public String uid;
        public String summary;
        public String location;

        public CalEvent(long lastEdit, long created, long dtstart, long dtstamp, long dtend, String uid, String summary, String location) {
            this.lastEdit = lastEdit/1000;
            this.created = created/1000;
            this.dtstart = dtstart/1000;
            this.dtstamp = dtstamp/1000;
            this.dtend = dtend/1000;
            this.uid = uid;
            this.summary = summary;
            this.location = location;
        }
    }

    private List<CalEvent> convertIcal(String url) throws IOException {
        URL urlC = new URL(url);
        URLConnection yc = urlC.openConnection();
        InputStreamReader in =new InputStreamReader(yc.getInputStream());

        ICalReader reader = new ICalReader(in);

        List<ICalendar> calendarList = reader.readAll();
        reader.close();
        List<CalEvent> list = new ArrayList<>();
        for(VEvent event : calendarList.get(0).getEvents()){
            list.add(new CalEvent(event.getLastModified().getValue().getTime(), event.getCreated().getValue().getTime(), event.getDateStart().getValue().getTime(), event.getDateTimeStamp().getValue().getTime(), event.getDateEnd().getValue().getTime(), event.getUid().getValue(), event.getSummary().getValue(), event.getLocation().getValue()));
        }

        return list;
    }


}
