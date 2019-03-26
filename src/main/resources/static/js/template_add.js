
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

    $('.media-list-add').click(function () {
        currentEditID = $(this).attr('data-conf-id')
    });

    $('.modal-trigger-media').click(function () {
        updateMedia();
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
        try{
            param[elem.id] = JSON.parse(elem.value);
        }catch (e) {
            console.log("catch");
            param[elem.id] = elem.value;
        }

    }

    console.log(param);
    let data = [
        {
            "name" : $('#addWidgetName').val(),
            "widgetId": $('#widgetSelect').val(),
            "posX": $('#addWidgetPosX').val(),
            "posY": $('#addWidgetPosY').val(),
            "sizeWidth": $('#addWidgetSizeWidth').val(),
            "sizeHeight": $('#addWidgetSizeHeight').val(),
            "param": JSON.stringify(param)
        }
    ];

    console.log(data);

    let settings = {
        "url": "/api/template/" + templateId + "/widgetConf",
        "method": "POST",
        "headers": {
            "Content-Type": "application/json"
        },
        "data": JSON.stringify(data),
    };
    console.log(settings);

    $.ajax(settings).done(function () {
        location.reload()
    }).fail(function (response) {
        console.log(response);
        M.toast({
            html: " <i class=\"material-icons\" style='margin-right: 10px'>warning</i> Add fail",
            classes: 'red',
            displayLength: 4000
        });
    });
}


function updateMedia() {
    $.get("/api/media", function (response) {
        let mediaDiv = $('#mediaContent');
        mediaDiv.empty();
        let html = "";
        for(let elem of response){
            html += "<div class='col s2'><a href='#' class='media-box' data-id='"+elem+"'><div class='card-panel' style='padding: 5px; line-height: 0;'><img class='responsive-img' src='/files/"+elem+"'/> </div></a></div>"
        }

        mediaDiv.html(html);

        $('.media-box').click(function () {
            onMediaBoxClick(this);
        })
    });
}


function onMediaBoxClick(element){
    let elem = $(element);
    let card = elem.find('.card-panel');
    let id = elem.attr("data-id");
    if(!elem.hasClass("selected")){
        elem.addClass("selected");
        card.addClass("green")
    }

    else{
        elem.removeClass("selected");
        card.removeClass("green");
    }
}



function addImagesToCollection() {
    let images = $('.media-box.selected');
    let ids = [];
    let collectionDiv = $('#'+currentEditID+"_list");
    let html = "";
    for(let img of images){
        ids.push(img.getAttribute("data-id"));
        html += "<li class='collection-item' data-id='"+img.getAttribute("data-id")+"'>" +
            "<div>"+img.getAttribute("data-id")+"" +
            "<a href='#' class='secondary-content delMedia' data-id='"+img.getAttribute("data-id")+"' data-conf-id='"+currentEditID+"' >" +
                "<i class='material-icons red-text'>delete</i>" +
            "</a>" +
            "</div>" +
            "</li>"
    }

    collectionDiv.empty();
    collectionDiv.html(html);
    let input = $("#" + currentEditID);
    input.val(JSON.stringify(ids));

    if(ids.length === 0)
        input.addClass("valid");
    else
        input.removeClass("valid");

    $(".delMedia").click(function () {
        mediaDel(this);
    });

    let inputs = $(".conf-input,.custom-conf-input-" + $('#widgetSelect').val());
    checkTextInput();
    checkAddField(inputs);



}


function mediaDel(elem){
    let confID = elem.getAttribute("data-conf-id");
    let mediaId = elem.getAttribute("data-id");
    let input = $("#" + confID);
    console.log(input.val());
    let val = JSON.parse(input.val());

    let index = val.indexOf(mediaId);
    if(index > -1){
        val.splice(index, 1);
    }

    let html = "";
    for(let img of val){
        html += "<li class='collection-item' data-id='"+img+"'>" +
            "<div>"+img+"" +
            "<a href='#' class='secondary-content delMedia' data-id='"+img+"' data-conf-id='"+confID+"' >" +
            "<i class='material-icons red-text'>delete</i>" +
            "</a>" +
            "</div>" +
            "</li>"
    }
    let collectionDiv = $('#'+confID+"_list");
    collectionDiv.empty();
    collectionDiv.html(html);
    $(".delMedia").click(function () {
        mediaDel(this);
    });
    input.val(JSON.stringify(val));
    if(val.length === 0)
        input.addClass("valid");
    else
        input.removeClass("valid");

    let inputs = $(".conf-input,.custom-conf-input-" + $('#widgetSelect').val());
    checkTextInput();
    checkAddField(inputs);


}


function checkTextInput(){
    let textInput = $("input[type=text].conf-input,.custom-conf-input-" + $('#widgetSelect').val());
    console.log(textInput);
    for(let input of textInput){
        if(input.value.length > 0)
            $(input).addClass("valid");
        else
            $(input).removeClass("valid");
    }
}

