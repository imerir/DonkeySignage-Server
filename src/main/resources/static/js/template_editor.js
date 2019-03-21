document.addEventListener('DOMContentLoaded', function () {


    /*--Management name template--*/
    let modifyTemplate = $("#edit_btn");
    let submitTemplate = $("#confirm_btn");
    let cancelModifyTemplate = $("#cancel_btn");
    let editableTemplate = $(".editableTemplate");
    let toHideTemplate = $(".hide-to-edit");
    let toShowTemplate = $(".show-to-edit");

    modifyTemplate.click(function () {
        editableTemplate.removeAttr('disabled');
        editableTemplate.removeClass("grey-text text-darken-2");
        editableTemplate.addClass("validate valid");
        toHideTemplate.attr("hidden", "hidden");
        toShowTemplate.removeAttr("hidden");
    });

    cancelModifyTemplate.click(function () {
        location.reload();
    });

    submitTemplate.click(function () {
        let modifyTemplateJson = {
            "name": $("#name").val()
        };

        var settings = {
            "url": "api/template?id=" + templateId,
            "method": "PUT",
            "timeout": 0,
            "headers": {
                "Content-Type": "application/json"
            },
            "data": JSON.stringify(modifyTemplateJson),
        };

        $.ajax(settings).done(function (response) {
            console.log(response);
            window.location.reload();
        });
    });

    $('#deleteTemplateBtn').click(function () {
        let settings = {
            "url": "/api/template?id=" + templateId,
            "method": "DELETE"
        };
        $.ajax(settings).done(function (response) {
            console.log(response);
            window.location.replace("/media");
        });

    });

    /*--Management WidgetConfig template--*/
    let modifyEditConfig = $("#edit_btnWidget");
    let submitEditConfig = $("#confirm_btnWidget");
    let cancelModifyEditConfig = $("#cancel_btnWidget");
    let idToSend;
    let idToDelete;

    modifyEditConfig.click(function () {
        idToSend = $(this).attr("data-id");
        var allWidgets = $("." + idToSend + ".editableWidget");
        console.log(allWidgets);
        var changeWidgetConfigSelector = $("." + idToSend + "#confirm_btnWidget");
        var toHideEditConfigSelector = $("." + idToSend + ".hide-to-editWidget");
        let toShowEditConfigSelector = $("." + idToSend + ".show-to-editWidget");
        allWidgets.removeAttr('disabled');
        allWidgets.addClass("validate valid");
        changeWidgetConfigSelector.removeClass("grey-text text-darken-2");
        toHideEditConfigSelector.attr("hidden", "hidden");
        toShowEditConfigSelector.removeAttr("hidden");
    });

    cancelModifyEditConfig.click(function () {
        location.reload();
    });

    submitEditConfig.click(function () {
        var allWidgets = $("." + idToSend + ".editableWidget");
        let modifyEditConfigJson = {
            "sizeWidth": allWidgets[0].value,
            "sizeHeight": allWidgets[1].value,
            "param": ""
        };
        console.log(JSON.stringify(modifyEditConfigJson));
        var settings = {
            "url": "api/widgetConf?id=" + idToSend,
            "method": "PUT",
            "timeout": 0,
            "headers": {
                "Content-Type": "application/json"
            },
            "data": JSON.stringify(modifyEditConfigJson),
        };

        $.ajax(settings).done(function (response) {
            console.log(response);
            window.location.reload();
        });
    });

    $("#delete_btn").click(function () {
        idToDelete = $(this).attr("data-id");
    });

    $('#deleteWidgetBtn').click(function () {
        console.log(idToDelete);
        let settings = {
            "url": "/api/widgetConf?id=" + idToDelete,
            "method": "DELETE"
        };
        $.ajax(settings).done(function (response) {
            console.log(response);
            window.location.reload();
        });
    });
});


