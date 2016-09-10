function onBodyLoad() {
    document.getElementById('listSelectionTab').onclick = tabChange;
}

function tabChange(event) {
    var tabName = event.srcElement.id.replace('Tab', '');
    var rows = document.getElementsByClassName('rowHideable');
    for (var i = 0; i < rows.length; i++) {
        rows[i].hidden = rows[i].id.substr(0, 6) !== tabName.substr(0, 6);
    }
}

function contactWhenChange() {
    document.getElementById('groupContactCalendar').hidden = 
        document.getElementById('contactWhen').selectedIndex !== 6;
}

function contactWhenModeClick(mode) {
    if (mode) {
        document.getElementById('contactWhenChangeBtn').setAttribute('class', 'btn btn-primary active');
        document.getElementById('contactWhenCreateBtn').setAttribute('class', 'btn btn-default');
    } else {
        document.getElementById('contactWhenCreateBtn').setAttribute('class', 'btn btn-primary active');
        document.getElementById('contactWhenChangeBtn').setAttribute('class', 'btn btn-default');
    }
}