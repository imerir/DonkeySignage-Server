document.addEventListener('DOMContentLoaded', function () {

    templateEditorListeners();
    
});

function templateEditorListeners() {

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

    $(".delete_btn").click(function () {
        idToDelete = $(this).attr("data-widget");
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


    let listWidget = $('.widget_item');

    listWidget.click(function () {
        let id = $(this).attr("data-id");
        console.log(id);
        listWidget.removeClass("active");
        $(this).addClass("active");
        $(".modify-conf").attr("hidden", "hidden");
        console.log($('.widget_' + id));
        $('.widget_' + id).removeAttr("hidden");
    });

}


