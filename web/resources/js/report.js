$( document ).ready(function() {

    $('.editReportBtnClass').click(function () {

        date=$(this.parentNode.parentNode).find(".date").html();
        duration=$(this.parentNode.parentNode).find(".duration").html();
        description=$(this.parentNode.parentNode).find(".description").html();
        remote=$(this.parentNode.parentNode).find(".remote").prop( 'checked' );
        reportId=$(this.parentNode.parentNode).find(".reportId").val();

        $('#reportForm input[name=date]').val(date);
        $('#reportForm input[name=duration]').val(duration);
        $('#reportForm input[name=description]').val(description);
        $('#reportForm input[name=remote]').attr('checked', remote);

        $('#reportForm input[name=reportId]').val(reportId);

        $('#reportForm input[name=add-or-edit]').val("edit");
    });


    $('#btnSaveReport').click(function () {
        add_edit=$(this).find("#add-or-edit").val();

        if(add_edit == "edit") {

            $.post(
                "/reports/edit",
                {
                    reportId: $('#reportForm input[name=reportId]').val(),
                    date: $('#reportForm  input[name=date]').val(),
                    duration: $('#reportForm input[name=duration]').val(),
                    description: $('#reportForm input[name=description]').val(),
                    remote: $('#reportForm input[name=remote]').is(':checked')
                },
                function () {
                    location.reload();
                }
            ).fail( function( response ) {
                document.body.innerHTML = response.responseText;
            });
        }
        else {
            $.post(
                "/reports/add",
                {
                    date: $('#reportForm  input[name=date]').val(),
                    duration: $('#reportForm input[name=duration]').val(),
                    description: $('#reportForm input[name=description]').val(),
                    remote: $('#reportForm input[name=remote]').is(':checked')
                },
                function () {
                    location.reload();
                }
            ).fail( function( response ) {
                document.body.innerHTML = response.responseText;
            });
        }
    });
});


function deleteReport ( reportId ) {
    if(confirm(strings['confirm.delete'])){
        $.post(
            "/reports/delete",
            {
                reportId: reportId
            },
            function () {
                location.reload();
            }
        ).fail( function( response ) {
            document.body.innerHTML = response.responseText;
        });
    }
    else{
        return false;
    }
}
