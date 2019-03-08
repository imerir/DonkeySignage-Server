var groupSelectorDiv;

var parent = -1

document.addEventListener('DOMContentLoaded', function () {

    groupSelectorDiv = $("#groupSelector");
});




function getGroupPage(groupId) {
    console.log(groupId);
    $.get("/api/group/getChildren?id="+groupId, (response)=>{
        console.log(response);
        groupSelectorDiv.empty();
        if(groupId != -1)
            groupSelectorDiv.append("<li class='collection-item'><a href='#' data-id='"+response.parentId+"' class='changeGroupBtn'>...<i class='material-icons'>arrow_upward</i></a> </li>");
        response.children.forEach((el)=>{
            console.log(el);
            groupSelectorDiv.append("" +
                "<li class=\"collection-item\">" +
                "<div>" +
                "<a href='#' class='changeGroupBtn' data-id='"+ el.id+"'>"+ el.name+"</a><a href='#' data-id='"+ el.id+"' class='secondary-content'><i class='material-icons'>send</i></a>" +
                "</div>" +
                "</li>");
        });
        listeners();
    });



}


function listeners(){
    $(".changeGroupBtn").click(function(){
        console.log($(this).attr("data-id"));
        getGroupPage($(this).attr("data-id"));
    })
}