package Donkey.Widgets;

import Donkey.Database.Entity.WidgetConfigEntity;
import Donkey.Database.Repository.ScreenRepository;
import Donkey.Database.Repository.WidgetConfigRepository;
import Donkey.SpringContext;
import Donkey.Tools.FilesTools;
import Donkey.Tools.Json;
import biweekly.ICalendar;
import biweekly.component.VEvent;
import biweekly.component.VTimezone;
import biweekly.io.text.ICalReader;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.ApplicationContext;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

public class RaplaCalendar implements WidgetInterface{
    Logger logger = LogManager.getLogger();

    @Override
    public String getName() {
        return "Rapla Calendar";
    }

    @Override
    public String getId() {
        return "RAPLA_CALENDAR";
    }

    @Override
    public String getVersion() {
        return "0.1";
    }

    @Override
    public String getTemplate() throws IOException {

        return FilesTools.getINSTANCE().getFileContent("widgets/rapla_calendar.html");
    }

    @Override
    public String convertParam(String paramStr) {

        try {
            HashMap<String, Object> conf = Json.loadObject(paramStr);
            HashMap<String, String> calendars = (HashMap<String, String>) conf.get("URLS");
            HashMap<String, List<RaplaCalEvent>> convertedCal = new HashMap<>();
            //check no cal
            for(Map.Entry<String, String> entry : calendars.entrySet()){
                List<RaplaCalEvent> events = null;
                try{
                    events = convertIcal(entry.getValue());
                }catch (IOException ignored){}
                convertedCal.put(entry.getKey(), events);
            }
            HashMap<String, Object> toReturn = new HashMap<>();
            toReturn.put("day_start", conf.get("day_start"));
            toReturn.put("day_end", conf.get("day_end"));
            toReturn.put("weekend", conf.get("weekend"));
            toReturn.put("calendars", convertedCal);
            return Json.stringify(toReturn);
        } catch (IOException e) {
            logger.catching(e);
            return null;
        }
    }


    @JsonIgnore
    @Override
    public List<WidgetConfDefinition> getParam() {
        WidgetConfDefinition url = new WidgetConfDefinition("URLS", "Calendar name : Calendar URL", ConfType.MAP, true, false, false, "", "",  null);
        WidgetConfDefinition dayStart = new WidgetConfDefinition("day_start", "Day start at (Hour):", ConfType.NUMBER, true, false, false, "7", "",  null);
        WidgetConfDefinition dayEnd = new WidgetConfDefinition("day_end", "Day end at (hour):", ConfType.NUMBER, true, false, false, "19", "",  null);
        WidgetConfDefinition weekEnd = new WidgetConfDefinition("weekend", "Show weekend:", ConfType.BOOL, true, false, false, "true", "", null);

        return Arrays.asList(dayStart, dayEnd, weekEnd, url);
    }

    @Override
    public Map<String, WidgetConfDefinition> getParam(String jsonValue) throws IOException {
        HashMap<String, Object> parsed = Json.loadObject(jsonValue);
        Map<String, WidgetConfDefinition> map = new HashMap<>();
        map.put("URLS", new WidgetConfDefinition("URLS", "Calendar name | Calendar URL", ConfType.MAP, true, false, false, parsed.get("URLS"), Json.stringify(parsed.get("URLS")), null));

        map.put("day_start", new WidgetConfDefinition("day_start", "Day start at (Hour):", ConfType.NUMBER, true, false, false, parsed.get("day_start"),  Integer.toString((Integer)parsed.get("day_start")), null));

        map.put("day_end", new WidgetConfDefinition("day_end", "Day end at (hour):", ConfType.NUMBER, true, false, false, parsed.get("day_end"), Integer.toString((Integer) parsed.get("day_end")), null));
        map.put("weekend", new WidgetConfDefinition("weekend", "Show weekend:", ConfType.BOOL, true, false, false, parsed.get("weekend"), Boolean.toString((Boolean) parsed.get("weekend")), null));
        return  map;
    }

    @Override
    public boolean needUpdate(WidgetConfigEntity widgetConf) {

        try {
            String param = widgetConf.getParam();
            Date lastEdit = widgetConf.getLastUpdate();
            if(lastEdit == null)
                return true;
            HashMap<String, Object> conf = Json.loadObject(param);
            HashMap<String, String> calendars = (HashMap<String, String>) conf.get("URLS");
            int totalSize = 0;
            for(Map.Entry<String, String> entry : calendars.entrySet()){
                try{
                    List<RaplaCalEvent> events = convertIcal(entry.getValue());
                    totalSize += events.size();
                    for(RaplaCalEvent event : events){
                        if(event.lastEdit.getTime() > lastEdit.getTime()) //TODO Correct bug whit UTC on rapla ??????
                            return true;
                    }
                }catch (IOException ignored){}
            }
            if(conf.get("lastSize") != null){
                int lastSize = (int) conf.get("lastSize");
                if(lastSize != totalSize){
                    conf.put("lastSize", totalSize);
                    ApplicationContext context = SpringContext.getAppContext();
                    WidgetConfigRepository widgetConfigRepository = (WidgetConfigRepository) context.getBean("widgetConfigRepository");
                    widgetConf.setParam(Json.stringify(conf));
                    widgetConfigRepository.save(widgetConf);
                    return true;

                }
            }
            else{
                conf.put("lastSize", totalSize);
                ApplicationContext context = SpringContext.getAppContext();
                WidgetConfigRepository widgetConfigRepository = (WidgetConfigRepository) context.getBean("widgetConfigRepository");
                widgetConf.setParam(Json.stringify(conf));
                widgetConfigRepository.save(widgetConf);
                return true;
            }
        } catch (IOException e) {
            logger.error("Fail to load json config.");
            return false;
        }



        return false;
    }

    class RaplaCalEvent {
        public Date lastEdit;
        public long created;
        public long dtstart;
        public long dtstamp;
        public long dtend;
        public String uid;
        public String summary;
        public String location;

        public RaplaCalEvent(Date lastEdit, long created, long dtstart, long dtstamp, long dtend, String uid, String summary, String location) {
            this.lastEdit = lastEdit;
            this.created = created/1000;
            this.dtstart = dtstart/1000;
            this.dtstamp = dtstamp/1000;
            this.dtend = dtend/1000;
            this.uid = uid;
            this.summary = summary;
            this.location = location;
        }
    }

    private List<RaplaCalEvent> convertIcal(String url) throws IOException {
        URL urlC = new URL(url);
        logger.debug(url);
        URLConnection yc = urlC.openConnection();
        InputStreamReader in =new InputStreamReader(yc.getInputStream());

        ICalReader reader = new ICalReader(in);

        List<ICalendar> calendarList = reader.readAll();
        reader.close();
        List<RaplaCalEvent> list = new ArrayList<>();
        VTimezone timezone = (VTimezone) calendarList.get(0).getTimezoneInfo().getComponents().toArray()[0];
        String tzstr = timezone.getTimezoneId().getValue();
        int offset = LocalDateTime.now().atZone(ZoneId.of(tzstr)).getOffset().getTotalSeconds();
        for(VEvent event : calendarList.get(0).getEvents()){
            Calendar lastModified = Calendar.getInstance();
            lastModified.setTime(event.getLastModified().getValue());
            lastModified.add(Calendar.SECOND, offset);
            list.add(new RaplaCalEvent(lastModified.getTime(), event.getCreated().getValue().getTime(), event.getDateStart().getValue().getTime(), event.getDateTimeStamp().getValue().getTime(), event.getDateEnd().getValue().getTime(), event.getUid().getValue(), event.getSummary().getValue(), event.getLocation().getValue()));
        }

        return list;
    }


}
