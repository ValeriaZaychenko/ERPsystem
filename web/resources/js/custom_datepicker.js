
$(document).ready(function () {

    $("#datepicker_month").datepicker(
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
