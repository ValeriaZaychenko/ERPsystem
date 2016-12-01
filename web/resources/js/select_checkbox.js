$(document).ready(function(){
    $("#users-table #checkall").click(function () {
        if ($("#users-table #checkall").is(':checked')) {
            $("#users-table input[type=checkbox]").each(function () {
                $(this).prop("checked", true);
            });

        } else {
            $("#users-table input[type=checkbox]").each(function () {
                $(this).prop("checked", false);
            });
        }
    });

    $("[data-toggle=tooltip]").tooltip();
});
