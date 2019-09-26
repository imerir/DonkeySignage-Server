function mapTypeListeners() {
    $(".listen-map").keyup(onChange);
    $(".listen-map").change(onChange);

    $(".map_add").click(function () {
        let hasColor = $(this).hasClass("withColor");
        let selector = this.attributes['data-map'].value;

        let target = selector.replace("map_", "modify_custom_");
        let key = selector.replace("map_", "key_");
        let value = selector.replace("map_", "value_");
        let color = selector.replace("map_", "color_");
        let html = "" +
            " <li class='collection-item'  data-id='' style='padding: 5px'>\n" +
            "   <div class='row valign-wrapper' style=\"margin: 0\">\n" +
            "       <div class=\"col s4\">\n" +
            "           <input type='text' class='listen-map " + key + "' data-target='" + target + "'/>\n" +
            "       </div>\n"
        if (!hasColor){
            html = html +
                "       <div class=\"col s8\">\n"+
                "           <input type='text' class='listen-map " + value + "' data-target='" + target + "'/>\n" +
                "       </div>\n" +
                "   </div>\n" +
                "</li>";
        }else{
            html = html +
                "       <div class=\"col s7\">\n" +
                "           <input type='text' class='listen-map " + value + "' data-target='" + target + "'/>\n" +
                "       </div>\n" +
                "       <div class=\"col s1\">\n" +
                "           <input type='color' value='#32CD32' class='listen-map " + color + "' data-target='" + target + "' style='width: 100%; height: 30px'/>\n" +
                "       </div>\n" +
                "   </div>\n" +
                "</li>";
        }

        $(".listen-map").off("keyup");
        $(".map_add").off("click");
        $("#" + selector).append(html);
        mapTypeListeners();
    });

}

function onChange(){
    let selector = '#' + this.attributes['data-target'].value;
    let keys = selector.replace("#modify_custom_", ".key_");
    let value = selector.replace("#modify_custom_", ".value_");
    let color = selector.replace("#modify_custom_", ".color_");
    recontructMap(selector, keys, value, color);
}


function recontructMap(inputId, keySelector, valueSelector, colorSelector) {
    let keyList = [];
    $(keySelector).each(function () {
        keyList.push(this.value.replace(/\s/g, '_'));
    });

    let obj = {};
    let colors = $(colorSelector);
    $(valueSelector).each(function (index) {
        let key = keyList[index];
        if (key !== "" && this.value !== "") {
            if(colorSelector == null){
                obj[key] = this.value;
            }
            else{
                obj[key] = {
                    value : this.value,
                    color : colors[index].value
                }
            }

        }

    });
    let input = $(inputId);
    input.val(JSON.stringify(obj));

    if (keyList.length === 0)
        input.addClass("valid");
    else
        input.removeClass("valid");

    input.change();
}