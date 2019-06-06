package Donkey.Widgets;


import java.util.List;

public class WidgetConfDefinition {


    private String name;

    private ConfType type;

    private boolean freeValue;

    private String rawValue;

    private boolean list;

    private Object value;

    private List<Object> valueSet;

    private boolean needDownload;


    /**
     * Constructor for definition only (set value to null)
     * @param name
     * @param type
     * @param freeValue
     * @param list
     * @param valueSet If is not a free value, set the possible value.
     */
    public WidgetConfDefinition(String name, ConfType type, boolean freeValue, boolean list, List<Object> valueSet) {
        this.name = name;
        this.type = type;
        this.freeValue = freeValue;
        this.list = list;
        this.valueSet = valueSet;
    }

    /**
     *
     * @param name
     * @param type
     * @param isFreeValue
     * @param isList
     * @param neeedDownlaod
     * @param value
     * @param rawValue
     * @param valueSet
     */
    public WidgetConfDefinition(String name, ConfType type, boolean isFreeValue, boolean isList, boolean neeedDownlaod, Object value, String rawValue, List<Object> valueSet) {
        this.name = name;
        this.freeValue = isFreeValue;
        this.type = type;
        this.list = isList;
        this.value = value;
        this.valueSet = valueSet;
        this.rawValue = rawValue;
        this.needDownload = neeedDownlaod;
    }

    public boolean isFreeValue() {
        return freeValue;
    }

    public void setFreeValue(boolean freeValue) {
        this.freeValue = freeValue;
    }

    public ConfType getType() {
        return type;
    }

    public void setType(ConfType type) {
        this.type = type;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public List<Object> getValueSet() {
        return valueSet;
    }

    public void setValueSet(List<Object> valueSet) {
        this.valueSet = valueSet;
    }

    public boolean isList() {
        return list;
    }

    public void setList(boolean list) {
        this.list = list;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRawValue() {
        return rawValue;
    }

    public void setRawValue(String rawValue) {
        this.rawValue = rawValue;
    }

    public boolean isNeedDownload() {
        return needDownload;
    }

    public void setNeedDownload(boolean needDownload) {
        this.needDownload = needDownload;
    }
}
