let edition = false;
let editableInput;
let toHide;
let toShow;

document.addEventListener('DOMContentLoaded', function () {
    editableInput = $(".editable");
    toHide = $(".hide-to-edit");
    toShow = $(".show-to-edit");
    edit_listeners();

});


function changeInput() {
    if( typeof getGroupPage === 'function')
        getGroupPage(-1,'');

    editableInput = $(".editable");
    toHide = $(".hide-to-edit");
    toShow = $(".show-to-edit");
    editableInput.each(function () {
        console.log(this);
        if(this.nodeName === "SELECT"){
            $(this).removeClass("disabled");
            var elems = document.querySelectorAll('select');
            M.FormSelect.init(elems);
        }
        else{
            editableInput.removeClass("grey-text text-darken-2");
            editableInput.addClass("validate valid");
            editableInput.removeAttr('disabled');
        }
    });
    //$('#roleSelect').prop('disabled',''); //TODO tag select without disabled but nothing change !!!

    toHide.attr("hidden", "hidden");
    toShow.removeAttr("hidden");
    edition = true;
}



function edit_listeners() {
    $('#edit_btn').click(function () {
        changeInput();
    });

    $('#confirm_btn').click(function () {
        sendForm();
    });

    $('#cancel_btn').click(function () {
        location.reload();
    })

}


