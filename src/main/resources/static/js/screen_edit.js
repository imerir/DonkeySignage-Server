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
    editableInput.removeAttr('disabled');
    editableInput.prop('disabled', false); //TODO Maybe erase it
    editableInput.removeClass("grey-text text-darken-2");
    editableInput.addClass("validate valid");
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


