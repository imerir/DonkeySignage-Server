let selectedEditWidget = -1;
let slide_edit;


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

    $(".delMedia").click(function () {
        mediaDel(this);
    });

    $('#deleteWidgetBtn').click(function () {
        console.log(selectedEditWidget);
        let settings = {
            "url": "/api/widgetConf?id=" + selectedEditWidget,
            "method": "DELETE"
        };
        $.ajax(settings).done(function (response) {
            window.location.reload();
        });
    });
    let side = document.querySelectorAll('#slide-edit');
    M.Sidenav.init(side, {edge: 'right', onCloseStart : onSideEditClose});

    slide_edit = M.Sidenav.getInstance(document.querySelector("#slide-edit"));
    let listWidget = $('.widget_item');

    /**
     * Listen for click on one item in the list and open the side nav for editing
     */
    listWidget.click(function () {
        let id = $(this)[0].id;
        listWidget.removeClass("active");
        $(this).addClass("active");
        $(".modify-conf").attr("hidden", "hidden");
        $('.widget_' + id).removeAttr("hidden");
        slide_edit.open();
        selectedEditWidget = id;
    });

    $("#close-edit-btn").click(function () {
        slide_edit.close();
    });



    let editInputs = $(".listen-edit");
    editInputs.change(function () {
        console.log("change");
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
            if(elem.type === 'checkbox'){
                param[elem.getAttribute("data-name")] = elem.checked;
            }else{
                try{
                    param[elem.getAttribute("data-name")] = JSON.parse(elem.value);
                }catch (e) {
                    console.log("catch");
                    param[elem.getAttribute("data-name")] = elem.value;
                }
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
            "url": "/api/widgetConf?id="+id,
            "method": "PUT",
            "headers": {
                "Content-Type": "application/json"
            },
            "data": JSON.stringify(data),
        };

        $.ajax(settings).done(function () {
            sessionStorage.setItem("success", "true");
            window.location.href = "#" + id;
            location.reload()
        }).fail(function (response) {
            console.log(response);
            M.toast({
                html: " <i class=\"material-icons\" style='margin-right: 10px'>warning</i> Edit fail",
                classes: 'red',
                displayLength: 4000
            });
        });
    });
    console.log(window.location.hash);
    if(window.location.hash) {
        $(window.location.hash).click();
    }

    if(sessionStorage.getItem("success") != null && sessionStorage.getItem("success") !== "null"){
        sessionStorage.setItem("success", null);
        M.toast({
            html: " <i class=\"material-icons\" style='margin-right: 10px'>check</i> Change saved",
            classes: 'green',
            displayLength: 4000
        });
    }
}


function checkTextInputEdit(){
    let textInput = $("input[type=text].modify_"+selectedEditWidget+",.modify_custom_" + selectedEditWidget);
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

function onSideEditClose(){
    window.location.hash = "";
    $('.widget_item').removeClass("active");
}