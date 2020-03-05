

var ulDiv = null;
var targetInputId = null;

$(".modal-trigger-media").click(function(){
    let rawJson = $("#"+this.getAttribute("data-input")).val();
    let json;
    try{
        json = JSON.parse(rawJson);
    }catch (e) {
        json = []
    }
    initMediaModal(this.getAttribute("data-list"),this.getAttribute("data-input"), json);
});


function initMediaModal(ulDivId, inputId, selectedList) {
    ulDiv = ulDivId;
    targetInputId = inputId;


    $.get("/api/media", function (response) {
        let mediaDiv = $('#mediaContent');
        mediaDiv.empty();
        let html = "";
        for(let elem of response){
            if(selectedList.indexOf(elem) !== -1){

            }
            console.log(elem);
            if(elem.includes(".jpg")){
                html += "<div class='col s2'><a href='#' class='media-box "+(selectedList.indexOf(elem) !== -1 ? "selected" : "")+"' data-id='"+elem+"'><div class='card-panel "+(selectedList.indexOf(elem) !== -1 ? "green" : "")+"' style='padding: 5px; line-height: 0;height: 16vh;'><img class='responsive-img' src='/files/"+elem+"'/> </div></a></div>"
            }
            if(elem.includes(".mp4")){
                html += "<div class='col s2'><a href='#' class='media-box "+(selectedList.indexOf(elem) !== -1 ? "selected" : "")+"' data-id='"+elem+"'><div class='card-panel "+(selectedList.indexOf(elem) !== -1 ? "green" : "")+"' style='padding: 5px; line-height: 0; height: 16vh;'><video class='responsive-video' src='/files/"+elem+"' type=\"video/mp4\" controls=\"true\"></video></div></a></div>"
            }
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
    let collectionDiv = $('#' + ulDiv);
    let html = "";
    for(let img of images){
        ids.push(img.getAttribute("data-id"));
        html += "<li class='collection-item'>" +
            "<div class='truncate'>"+img.getAttribute("data-id")+"" +
            "<a href='#' class='secondary-content delMedia' data-id='"+img.getAttribute("data-id")+"' data-list='"+ulDiv+"' data-target='"+targetInputId+"' >" +
            "<i class='material-icons red-text'>delete</i>" +
            "</a>" +
            "</div>" +
            "</li>"
    }

    collectionDiv.empty();
    collectionDiv.html(html);
    let input = $("#" + targetInputId);
    input.val(JSON.stringify(ids));

    if(ids.length === 0)
        input.addClass("valid");
    else
        input.removeClass("valid");

    $(".delMedia").click(function () {
        mediaDel(this);
    });

    input.change();




}

function mediaDel(elem){
    let listId = elem.getAttribute("data-list");
    let target = elem.getAttribute("data-target");
    let mediaId = elem.getAttribute("data-id");
    let input = $("#" + target);
    console.log("Before: "+input.val());
    let val = JSON.parse(input.val());

    let index = val.indexOf(mediaId);
    if(index > -1){
        val.splice(index, 1);
    }
    let html = "";
    for(let img of val){
        html += "<li class='collection-item' >" +
            "<div>"+img+"" +
            "<a href='#' class='secondary-content delMedia' data-id='"+img+"' data-list='"+listId+"' data-target='"+ target +"' >" +
            "<i class='material-icons red-text'>delete</i>" +
            "</a>" +
            "</div>" +
            "</li>"
    }
    let collectionDiv = $('#'+ listId );
    collectionDiv.empty();
    collectionDiv.html(html);
    $(".delMedia").click(function () {
        mediaDel(this);
    });
    if(val.length === 0)
        input.val("");
    else
    input   .val(JSON.stringify(val));
    if(val.length === 0)
        input.addClass("valid");
    else
        input.removeClass("valid");

    console.log("After: "+input.val());
    input.change();

}