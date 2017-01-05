$( document ).ready(function() {

    $('.editHolidayBtnClass').click(function () {

        date=$(this.parentNode.parentNode).find(".date").html();
        description=$(this.parentNode.parentNode).find(".description").html();
        holidayId=$(this.parentNode.parentNode).find(".holidayId").val();

        $('#holidayForm input[name=date]').val(date);
        $('#holidayForm input[name=description]').val(description);

        $('#holidayForm input[name=holidayId]').val(holidayId);

        $('#holidayForm input[name=add-or-edit]').val("edit");

        $('#holidayForm button[id=btnSaveHoliday]').removeAttr('disabled');
    });


    $('#btnSaveHoliday').click(function () {
        add_edit=$(this).find("#add-or-edit").val();

        if(add_edit == "edit") {

            $.post(
                "/holidays/edit",
                {
                    holidayId: $('#holidayForm input[name=holidayId]').val(),
                    date: $('#holidayForm  input[name=date]').val(),
                    description: $('#holidayForm input[name=description]').val(),
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
                "/holidays/add",
                {
                    date: $('#holidayForm  input[name=date]').val(),
                    description: $('#holidayForm input[name=description]').val(),
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


function deleteHoliday ( holidayId ) {
    if(confirm(strings['confirm.delete'])){
        $.post(
            "/holidays/delete",
            {
                holidayId: holidayId
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
