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
import biweekly.property.ExceptionDates;
import biweekly.util.Frequency;
import biweekly.util.ICalDate;
import biweekly.util.Recurrence;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.ApplicationContext;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.URL;
import java.net.URLConnection;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

public class RaplaCalendar implements WidgetInterface {
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
            HashMap<String, HashMap<String, String>> calendars = (HashMap<String, HashMap<String, String>>) conf.get("URLS");
            HashMap<String, RaplaContainer> convertedCal = new HashMap<>();
            //check no cal
            for (Map.Entry<String, HashMap<String, String>> entry : calendars.entrySet()) {
                List<RaplaCalEvent> events = null;
                try {
                    events = convertIcal(entry.getValue().get("value"));
                } catch (IOException ignored) {
                }
                convertedCal.put(entry.getKey(), new RaplaContainer(entry.getKey(), events, entry.getValue().get("color")));
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
        WidgetConfDefinition url = new WidgetConfDefinition("URLS", "Calendar name : Calendar URL", ConfType.MAP_WITH_COLOR, true, false, false, "", "", null);
        WidgetConfDefinition dayStart = new WidgetConfDefinition("day_start", "Day start at (Hour):", ConfType.NUMBER, true, false, false, "7", "", null);
        WidgetConfDefinition dayEnd = new WidgetConfDefinition("day_end", "Day end at (hour):", ConfType.NUMBER, true, false, false, "19", "", null);
        WidgetConfDefinition weekEnd = new WidgetConfDefinition("weekend", "Show weekend:", ConfType.BOOL, true, false, false, "true", "", null);

        return Arrays.asList(dayStart, dayEnd, weekEnd, url);
    }

    @Override
    public Map<String, WidgetConfDefinition> getParam(String jsonValue) throws IOException {
        HashMap<String, Object> parsed = Json.loadObject(jsonValue);
        Map<String, WidgetConfDefinition> map = new HashMap<>();
        if( parsed.get("URLS") != null) {
            map.put("URLS", new WidgetConfDefinition("URLS", "Calendar name | Calendar URL", ConfType.MAP_WITH_COLOR, true, false, false, parsed.get("URLS"), Json.stringify(parsed.get("URLS")), null));
        }
        else{
            map.put("URLS", new WidgetConfDefinition("URLS", "Calendar name | Calendar URL", ConfType.MAP_WITH_COLOR, true, false, false, new HashMap<>(), Json.stringify(parsed.get("URLS")), null));
        }


        map.put("day_start", new WidgetConfDefinition("day_start", "Day start at (Hour):", ConfType.NUMBER, true, false, false, parsed.get("day_start"), Integer.toString((Integer) parsed.get("day_start")), null));

        map.put("day_end", new WidgetConfDefinition("day_end", "Day end at (hour):", ConfType.NUMBER, true, false, false, parsed.get("day_end"), Integer.toString((Integer) parsed.get("day_end")), null));
        map.put("weekend", new WidgetConfDefinition("weekend", "Show weekend:", ConfType.BOOL, true, false, false, parsed.get("weekend"), Boolean.toString((Boolean) parsed.get("weekend")), null));
        return map;
    }

    @Override
    public boolean needUpdate(WidgetConfigEntity widgetConf) {

        try {
            String param = widgetConf.getParam();
            Date lastEdit = widgetConf.getLastUpdate();
            if (lastEdit == null)
                return true;
            HashMap<String, Object> conf = Json.loadObject(param);
            HashMap<String, HashMap<String, String>> calendars = (HashMap<String, HashMap<String, String>>) conf.get("URLS");
            if(calendars == null)
                return false;
            int totalSize = 0;
            for (Map.Entry<String, HashMap<String, String>> entry : calendars.entrySet()) {
                try {
                    List<RaplaCalEvent> events = convertIcal(entry.getValue().get("value"));
                    totalSize += events.size();
                    for (RaplaCalEvent event : events) {
                        if (event.lastEdit.getTime() > lastEdit.getTime()) //TODO Correct bug whit UTC on rapla ??????
                            return true;
                    }
                } catch (IOException ignored) {
                }
            }
            if (conf.get("lastSize") != null) {
                int lastSize = (int) conf.get("lastSize");
                if (lastSize != totalSize) {
                    conf.put("lastSize", totalSize);
                    ApplicationContext context = SpringContext.getAppContext();
                    WidgetConfigRepository widgetConfigRepository = (WidgetConfigRepository) context.getBean("widgetConfigRepository");
                    widgetConf.setParam(Json.stringify(conf));
                    widgetConfigRepository.save(widgetConf);
                    return true;

                }
            } else {
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

    static class RaplaCalEvent {
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
            this.created = created / 1000;
            this.dtstart = dtstart / 1000;
            this.dtstamp = dtstamp / 1000;
            this.dtend = dtend / 1000;
            this.uid = uid;
            this.summary = summary;
            this.location = location;
        }

        public RaplaCalEvent(RaplaCalEvent event) {
            this.lastEdit = event.lastEdit;
            this.created = event.created;
            this.dtstart = event.dtstart;
            this.dtstamp = event.dtstamp;
            this.dtend = event.dtend;
            this.uid = event.uid;
            this.summary = event.summary;
            this.location = event.location;
        }
    }

    public static class RaplaContainer{
        public String name;
        public List<RaplaCalEvent> events;
        public String color;

        public RaplaContainer(String name, List<RaplaCalEvent> events, String color) {
            this.name = name;
            this.events = events;
            this.color = color;
        }
    }

    private List<RaplaCalEvent> convertIcal(String url) throws IOException {
        if (!pingHost(url, 1000)) {
            logger.warn("Calendar unreachable (url: " + url + ")");
            return new ArrayList<>();
        }
        URL urlC = new URL(url);
        URLConnection yc = urlC.openConnection();
        InputStreamReader in = new InputStreamReader(yc.getInputStream());

        ICalReader reader = new ICalReader(in);

        List<ICalendar> calendarList = reader.readAll();
        reader.close();
        List<RaplaCalEvent> list = new ArrayList<>();
        if (calendarList == null || calendarList.isEmpty()) {
            logger.info("Calendar is empty or read error ?");
            return new ArrayList<>();
        }

        VTimezone timezone = (VTimezone) calendarList.get(0).getTimezoneInfo().getComponents().toArray()[0];
        String tzstr = timezone.getTimezoneId().getValue();
        int offset = LocalDateTime.now().atZone(ZoneId.of(tzstr)).getOffset().getTotalSeconds();
        for (VEvent event : calendarList.get(0).getEvents()) {
            Calendar lastModified = Calendar.getInstance();
            lastModified.setTime(event.getLastModified().getValue());
            lastModified.add(Calendar.SECOND, offset);
            RaplaCalEvent baseEvent = new RaplaCalEvent(lastModified.getTime(), event.getCreated().getValue().getTime(), event.getDateStart().getValue().getTime(), event.getDateTimeStamp().getValue().getTime(), event.getDateEnd().getValue().getTime(), event.getUid().getValue(), event.getSummary().getValue(), event.getLocation().getValue());
            list.add(baseEvent);

            if (event.getRecurrenceRule() != null) {
                Recurrence recurrence = event.getRecurrenceRule().getValue();

                Calendar rollingStartDate = Calendar.getInstance();
                rollingStartDate.setTime(event.getDateStart().getValue());

                Calendar rollingEndDate = Calendar.getInstance();
                rollingEndDate.setTime(event.getDateEnd().getValue());

                List<Calendar> exceptionCalendars = new ArrayList<>();
                for (ExceptionDates exDate : event.getExceptionDates()) {
                    for (ICalDate date : exDate.getValues()) {
                        Calendar tempDate = Calendar.getInstance();
                        tempDate.setTime(date);
                        exceptionCalendars.add(tempDate);
                    }
                }

                Calendar enDate = Calendar.getInstance();
                enDate.setTime(recurrence.getUntil());
                int freq = -1;
                switch (recurrence.getFrequency()){
                    case SECONDLY:
                        freq = Calendar.SECOND;
                        break;
                    case MINUTELY:
                        freq = Calendar.MINUTE;
                        break;
                    case HOURLY:
                        freq = Calendar.HOUR;
                        break;
                    case DAILY:
                        freq = Calendar.DATE;
                        break;
                    case WEEKLY:
                        freq = Calendar.WEEK_OF_YEAR;
                        break;
                    case MONTHLY:
                        freq = Calendar.MONTH;
                        break;
                    case YEARLY:
                        freq = Calendar.YEAR;
                        break;
                }

                if(freq == -1){
                    logger.error("Recurrence frequency unknown : " + recurrence.getFrequency());
                    break;
                }

                while (rollingStartDate.before(enDate)) {
                    rollingStartDate.add(Calendar.DATE, recurrence.getInterval());
                    rollingEndDate.add(Calendar.DATE, recurrence.getInterval());
                    if (!isExceptionDay(rollingStartDate, exceptionCalendars)) {
                        RaplaCalEvent tempEvent = new RaplaCalEvent(baseEvent);
                        tempEvent.dtstart = rollingStartDate.getTimeInMillis() / 1000;
                        tempEvent.dtend = rollingEndDate.getTimeInMillis() / 1000;
                        list.add(tempEvent);
                    }

                }
            }
        }

        return list;
    }

    private boolean isExceptionDay(Calendar date, List<Calendar> exceptionDates) {
        for (Calendar exDate : exceptionDates) {
            if (exDate.get(Calendar.DAY_OF_YEAR) == date.get(Calendar.DAY_OF_YEAR) && exDate.get(Calendar.YEAR) == date.get(Calendar.YEAR))
                return true;
        }
        return false;
    }

    public boolean pingHost(String url, int timeout) {
        boolean isSSL = false;
        if (url.startsWith("https")) {
            url = url.replace("https://", "");
            isSSL = true;
        } else {
            url = url.replace("http://", "");
        }
        int endHost = url.indexOf('/');
        String host;
        if (endHost != -1) {
            host = url.substring(0, endHost);
        } else
            host = url;

        int port;
        if (host.contains(":")) {
            String portStr = host.substring(host.indexOf(':')).replace(":", "");
            port = Integer.parseInt(portStr);
            host = host.substring(0, host.indexOf(':'));
        } else {
            port = isSSL ? 443 : 80;

        }

        try (Socket socket = new Socket()) {
            socket.connect(new InetSocketAddress(host, port), timeout);
            return true;
        } catch (IOException e) {
            return false; // Either timeout or unreachable or failed DNS lookup.
        }
    }


}
