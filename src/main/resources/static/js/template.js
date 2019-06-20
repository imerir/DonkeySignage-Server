

document.addEventListener('DOMContentLoaded', function () {

    let modal = document.querySelectorAll('.modal');
    M.Modal.init(modal);

    let elems = document.querySelectorAll('.collapsible');
    M.Collapsible.init(elems);

    let selects = document.querySelectorAll('select');
    M.FormSelect.init(selects);

    let side = document.querySelectorAll('#slide-edit');
    M.Sidenav.init(side, {edge: 'right'});
    templateAddListeners();
    templateEditorListeners();
    mapTypeListeners();
});
