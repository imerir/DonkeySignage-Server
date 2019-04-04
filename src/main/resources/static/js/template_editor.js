let selectedEditWidget = -1;


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
        selectedEditWidget = id;
    });


    let editInputs = $(".listen-edit");
    editInputs.change(function () {
        setTimeout(function () {
            let input = $(".modify_"+selectedEditWidget+",.modify_custom_" + selectedEditWidget);
            console.log(input);
            checkTextInputEdit();
            checkEditField(input);

        }, 100)
    });

    editInputs.keyup(function () {
        let input = $(".modify_"+selectedEditWidget+",.modify_custom_" + selectedEditWidget);
        checkTextInputEdit();
        checkEditField(input);
    });

    $('.modify-submit').click(function () {
        let id = $(this).attr("data-widget");

        let customInput = $(".modify_custom_" + id);
        let param = {};
        for (let elem of customInput) {
            try{
                param[$(elem).attr("data-name")] = JSON.parse(elem.value);
            }catch (e) {
                console.log("catch");
                param[$(elem).attr("data-name")] = elem.value;
            }

        }
        console.log(param);
        let data =
            {
                "name" : $('#modify_' + id + '_name').val(),
                "widgetId": $('#modify_' + id + '_type').val(),
                "posX": $('#modify_' + id + '_posX').val(),
                "posY": $('#modify_' + id + '_posY').val(),
                "sizeWidth": $('#modify_' + id + '_width').val(),
                "sizeHeight": $('#modify_' + id + '_height').val(),
                "param": JSON.stringify(param)
            }
        ;


        let settings = {
            "url": "http://localhost:8080/api/widgetConf?id="+id,
            "method": "PUT",
            "headers": {
                "Content-Type": "application/json"
            },
            "data": JSON.stringify(data),
        };

        $.ajax(settings).done(function () {
            location.reload()
        }).fail(function (response) {
            console.log(response);
            M.toast({
                html: " <i class=\"material-icons\" style='margin-right: 10px'>warning</i> Edit fail",
                classes: 'red',
                displayLength: 4000
            });
        });
    })


}


function checkTextInputEdit(){
    let textInput = $("input[type=text].modify_"+selectedEditWidget+",.modify_custom_" + selectedEditWidget);
    console.log(textInput);
    for(let input of textInput){
        if(input.value.length > 0)
            $(input).addClass("valid");
        else
            $(input).removeClass("valid");
    }
}

function checkEditField(confInput) {
    let valid = true;
    // debugger;
    for (let elem of confInput) {
        if (!$(elem).hasClass("valid")) {
            valid = false;
            break;
        }
    }

    if (valid)
        $(".modify-submit_"+selectedEditWidget).removeClass("disabled");
    else
        $(".modify-submit_"+selectedEditWidget).addClass("disabled");

}