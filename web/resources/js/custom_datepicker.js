
$(document).ready(function () {

    $("#datepicker-month").datepicker(
        {
            language: locale['languageCode'],
            viewMode: 'months',
            format: 'yyyy-mm',
            minViewMode: "months",
            maxViewMode: "years"
        });

    $("#datepicker_year").datepicker(
        {
            language: locale['languageCode'],
            viewMode: 'years',
            format: 'yyyy',
            minViewMode: "years",
            maxViewMode: "years"
        });
});
