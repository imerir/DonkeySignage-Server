package Donkey.Widgets;


import java.util.List;

public class WidgetConfDefinition {

    public enum Type{
        MEDIA,
        TEXT,
        NUMBER
    }


    private String name;

    private Type type;

    private boolean freeValue;

    private boolean list;

    private Object value;

    private List<Object> valueSet;


    public WidgetConfDefinition(){}

    public WidgetConfDefinition(String name, Type type, boolean isFreeValue, boolean isList, Object value, List<Object> valueSet) {
        this.name = name;
        this.freeValue = isFreeValue;
        this.type = type;
        this.list = isList;
        this.value = value;
        this.valueSet = valueSet;
    }

    public boolean isFreeValue() {
        return freeValue;
    }

    public void setFreeValue(boolean freeValue) {
        this.freeValue = freeValue;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
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
}
