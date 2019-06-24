function mapTypeListeners() {
    $(".listen-map").keyup(function () {
        let selector = '#' + this.attributes['data-target'].value;
        let keys = selector.replace("#modify_custom_", ".key_");
        let value = selector.replace("#modify_custom_", ".value_");
        recontructMap(selector, keys, value);
    });

    $(".map_add").click(function () {
        let selector = this.attributes['data-map'].value;

        let target = selector.replace("map_", "modify_custom_");
        let key = selector.replace("map_", "key_");
        let value = selector.replace("map_", "value_");

        let html = "" +
            " <li class='collection-item'  data-id='' style='padding: 5px'>\n" +
            "   <div class=\"row\" style=\"margin: 0\">\n" +
            "       <div class=\"col s4\">\n" +
            "           <input type='text' class='listen-map " + key + "' data-target='" + target + "'/>\n" +
            "       </div>\n" +
            "       <div class=\"col s8\">\n" +
            "           <input type='text' class='listen-map " + value + "' data-target='" + target + "'/>\n" +
            "       </div>\n" +
            "   </div>\n" +
            "</li>";

        $(".listen-map").off("keyup");
        $(".map_add").off("click");
        $("#" + selector).append(html);
        mapTypeListeners();
    });

}


function recontructMap(inputId, keySelector, valueSelector) {
    let keyList = [];
    $(keySelector).each(function () {
        keyList.push(this.value.replace(/\s/g, '_'));
    });

    let obj = {};
    $(valueSelector).each(function (index) {
        let key = keyList[index];
        if (key !== "" && this.value !== "") {
            obj[key] = this.value;
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