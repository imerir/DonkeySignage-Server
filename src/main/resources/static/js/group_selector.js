var groupSelectorDiv;

var parent = -1;

document.addEventListener('DOMContentLoaded', function () {

    groupSelectorDiv = $("#groupSelector");
});




function getGroupPage(groupId) {
    console.log(groupId);
    $.get("/api/group/getChildren?id="+groupId, (response)=>{
        groupSelectorDiv.empty();
        if(groupId != -1)
            groupSelectorDiv.append("<li class='collection-item'><a href='#' data-id='"+response.parentId+"' class='changeGroupBtn'><i class='material-icons'>arrow_back</i></a> </li>");
        response.children.forEach((el)=>{
            console.log(el);
            groupSelectorDiv.append("" +
                "<li class=\"collection-item\">" +
                "<div>" +
                "<a href='#' class='changeGroupBtn' style='font-size: 18px' data-id='"+ el.id+"'>" +
                "<i class='material-icons left grey-text' style='margin-right: 5px'>folder</i>"+ el.name+"</a>" +
                "<a href='#' data-id='"+ el.id+"' data-name='"+el.name+"' class='secondary-content selectGroupBtn'><i class='material-icons green-text'>exit_to_app</i>" +
                "</a>" +
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
    });

    $(".selectGroupBtn").click(function(){
        console.log($(this).attr("data-id"));
        console.log($(this).attr("data-name"));
        selectGroup($(this).attr("data-id"), $(this).attr("data-name"));
        var instance = M.Modal.getInstance($("#modalGroup").get(0));
        instance.close();
    })



}


function selectGroup(id, name) {
    $('#groupId').val(id);
    $('#groupName').val(name);
}