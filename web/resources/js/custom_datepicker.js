
$(document).ready(function () {

    $("#datepicker_month").datepicker(
        {
            viewMode: 'months',
            format: 'yyyy-mm',
            minViewMode: "months",
            maxViewMode: "months"
        });

    $("#datepicker_year").datepicker(
        {
            viewMode: 'years',
            format: 'yyyy',
            minViewMode: "years",
            maxViewMode: "years"
        });
});
