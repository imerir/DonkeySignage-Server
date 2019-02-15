package Donkey.Database.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

@Entity
public class WidgetConfig {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;


    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "template_id")
    private Template template;

    private int posX;
    private int posY;
    private int sizeWidth;
    private int sizeHight;

    private String param;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Template getTemplate() {
        return template;
    }

    public void setTemplate(Template template) {
        this.template = template;
    }

    public int getPosX() {
        return posX;
    }

    public void setPosX(int posX) {
        this.posX = posX;
    }

    public int getPosY() {
        return posY;
    }

    public void setPosY(int posY) {
        this.posY = posY;
    }

    public int getSizeWidth() {
        return sizeWidth;
    }

    public void setSizeWidth(int sizeWidth) {
        this.sizeWidth = sizeWidth;
    }

    public int getSizeHight() {
        return sizeHight;
    }

    public void setSizeHight(int sizeHight) {
        this.sizeHight = sizeHight;
    }

    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param;
    }
}
