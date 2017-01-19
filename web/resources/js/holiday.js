$( document ).ready(function() {

    $('.edit-holiday-btn').click(function () {

        date=$(this.parentNode.parentNode).find(".date").html();
        description=$(this.parentNode.parentNode).find(".description").html();
        holidayId=$(this.parentNode.parentNode).find(".holidayId").val();

        $('#holiday-form input[name=date]').val(date);
        $('#holiday-form input[name=description]').val(description);

        $('#holiday-form input[name=holidayId]').val(holidayId);

        $('#holiday-form input[name=add-or-edit]').val("edit");

        $('#holiday-form button[id=save-holiday-btn]').removeAttr('disabled');
    });


    $('#save-holiday-btn').click(function () {
        add_edit=$(this).find("#add-or-edit").val();

        if(add_edit == "edit") {

            $.post(
                "/holidays/edit",
                {
                    holidayId: $('#holiday-form input[name=holidayId]').val(),
                    date: $('#holiday-form  input[name=date]').val(),
                    description: $('#holiday-form input[name=description]').val(),
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
                    date: $('#holiday-form  input[name=date]').val(),
                    description: $('#holiday-form input[name=description]').val(),
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

function cloneHolidays ( year ) {
    if(confirm(strings['confirm.clone'])){
        $.post(
            "/holidays/clone",
            {
                year: year
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

function cloneHoliday ( holidayId ) {
    if (confirm(strings['confirm.clone.one'])) {
        $.post(
            "/holidays/holiday/clone",
            {
                holidayId: holidayId
            },
            function () {
                location.reload();
            }
        ).fail(function (response) {
            document.body.innerHTML = response.responseText;
        });
    }
    else {
        return false;
    }
}
