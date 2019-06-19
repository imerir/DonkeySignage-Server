
let currentEditID = null;


function templateAddListeners() {

    updateWidgetAdd($('#widgetSelect').val());

    $('#widgetSelect').change(function () {
        let id = $(this).val();
        updateWidgetAdd(id);
        let input = $(".conf-input,.custom-conf-input-" + $('#widgetSelect').val());
        checkAddField(input);
    });



    let confInput = $(".listen");
    confInput.change(function () {
        setTimeout(function () {
            let input = $(".conf-input,.custom-conf-input-" + $('#widgetSelect').val());
            checkTextInput();
            checkAddField(input);

        }, 100)
    });

    confInput.keyup(function () {
        let input = $(".conf-input,.custom-conf-input-" + $('#widgetSelect').val());
        checkTextInput();
        checkAddField(input);
    });

    $('#submitNewWidget').click(function () {
        sendNewWidget();
    });


    $('#mediaModalConfirm').click(function () {
        addImagesToCollection();
    })

}

function updateWidgetAdd(id){
    let conf = $('#' + id + '_conf');
    $('.custom_conf').attr("hidden", "hidden");
    conf.removeAttr("hidden");
}

function checkAddField(confInput) {
    let valid = true;
    for (let elem of confInput) {
        if (!$(elem).hasClass("valid")) {
            valid = false;
            break;
        }
    }

    if (valid)
        $("#submitNewWidget").removeClass("disabled");
    else
        $("#submitNewWidget").addClass("disabled");
}


function sendNewWidget() {
    let param = {};

    let customInput = $(".custom-conf-input-" + $('#widgetSelect').val());
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

    let data =
        [{
            "name" : $('#addWidgetName').val(),
            "widgetId": $('#widgetSelect').val(),
            "posX": $('#addWidgetPosX').val(),
            "posY": $('#addWidgetPosY').val(),
            "sizeWidth": $('#addWidgetSizeWidth').val(),
            "sizeHeight": $('#addWidgetSizeHeight').val(),
            "param": JSON.stringify(param)
        }];


    let settings = {
        "url": "/api/template/" + templateId + "/widgetConf",
        "method": "POST",
        "headers": {
            "Content-Type": "application/json"
        },
        "data": JSON.stringify(data),
    };
    console.log(settings);
    debugger;
    $.ajax(settings).done(function (response) {
        let id = response[0].id;
        window.location.href = "#" + id;
        location.reload();
    }).fail(function (response) {
        console.log(response);
        M.toast({
            html: " <i class=\"material-icons\" style='margin-right: 10px'>warning</i> Add fail",
            classes: 'red',
            displayLength: 4000
        });
    });
}



function checkTextInput(){
    let textInput = $("input[type=text].conf-input,.custom-conf-input-" + $('#widgetSelect').val());
    for(let input of textInput){
        if(input.value.length > 0)
            $(input).addClass("valid");
        else
            $(input).removeClass("valid");
    }
}

