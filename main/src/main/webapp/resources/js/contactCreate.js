function onBodyLoad() {
    var resetButtons = document.getElementsByClassName('btn fieldReset');
    for (var i = 0; i < resetButtons.length; i++) {
        resetButtons[i].onclick = resetFieldset; 
    }
}

function onResetAllClick() {
    companyRadioChange(true);
}

function resetFieldset(event) {
    var fieldset = event.srcElement.parentElement.parentElement;
    if (fieldset !== null && fieldset.type === 'fieldset') {
        for (var i = 0; i < fieldset.elements.length - 1; i++) {
            var field = fieldset.elements[i];
            if ('INPUT' === field.tagName || 'TEXTAREA' === field.tagName) {
                field.value = field.defaultValue;
            } else if ('SELECT' === field.tagName && '0' !== field.value) {
                field.value = 0;
            }
        }
    }
}

function companyRadioChange(value) {
    var groups = document.getElementsByClassName('form-group companyNew');
    for (var i = 0; i < groups.length; i++) {
        groups[i].hidden = !value;
    }
    groups = document.getElementsByClassName('form-group companySelect');
    for (i = 0; i < groups.length; i++) {
        groups[i].hidden = value;
    }
}

function taskPeriodChange() {
    var value = Number(document.getElementById('task_period').value);
    var currDate = new Date(), timeId = 0;
    var year = currDate.getFullYear();
    var month = currDate.getMonth();
    var date = currDate.getDate();
    switch (value) {
        case 0:
            timeId = currDate.getHours() * 2 + (currDate.getMinutes() < 30 ? 2 : 3);
            timeId = timeId > 48 ? 1 : timeId;
            break;
        case 1:
            break;
        case 2:
            currDate = new Date(year, month, date + 1);
            break;
        case 3:
            var dayNumber = currDate.getDay();
            if (dayNumber === 0) {
                dayNumber = 7;
            }
            currDate = new Date(year, month, date + 8 - dayNumber);
            break;
        case 4:
            currDate = new Date(year, month + 1);
            break;
        case 5:
            currDate = new Date(year + 1, 0);
            break;
        default:
    }
    document.getElementById('task_date').value = formatDate(currDate);
    document.getElementById('task_time').options[timeId].selected = true;
}

function formatDate(date) {
    var month = date.getMonth() + 1;
    var day = date.getDate();
    return [date.getFullYear(), (month < 10 ? '0' : '') + month, (day < 10 ? '0' : '') + day].join('-');
}
