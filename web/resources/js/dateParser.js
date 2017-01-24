var getStringFromDate = function () {

    var today = new Date();

    var getCurrentYearStr = function () {
        return today.getFullYear().toString();
    };

    var getCurrentMonthStr = function () {
        var mm = today.getMonth() + 1; // Javascript months count from 0

        return addExtra0AndGetStr(mm);
    };

    var getCurrentDayStr = function () {
        var dd = today.getDate();

        return addExtra0AndGetStr(dd);
    };

    //Add 0 to given days or month, because Javascript dates have 1 digit for day less than 10
    var addExtra0AndGetStr = function (xx) {
        if(xx < 10) {
            xx='0'+ xx
        }
        return xx.toString();
    };

    var getFullStrDate = function () {
        return getCurrentYearStr() + "-" + getCurrentMonthStr() + "-" + getCurrentDayStr();
    };

    var getMonthStrDate = function () {
        return getCurrentYearStr() + "-" + getCurrentMonthStr();
    };

    return {
        getFullStrDate : getFullStrDate(),
        getMonthStrDate : getMonthStrDate()
    }
};
