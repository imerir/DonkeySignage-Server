/*
 * Developed by Sebastien CLEMENT.
 * File created on 9/26/19, 2:28 PM.
 */

package Donkey.Widgets;


/**
 * This class is used whit the confType "MAP_WITH_COLOR"
 * If you want tu use this confType, you need tu create a map of <String, ColorMapValue>
 */
public class ColorMapValue{
    public String value;
    public String color;

    public ColorMapValue(String value, String color) {
        this.value = value;
        this.color = color;
    }

    public ColorMapValue() {
    }
}
