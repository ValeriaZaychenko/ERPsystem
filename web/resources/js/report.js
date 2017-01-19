$( document ).ready(function() {

    var updatePickerB = function () {
        var selectedValue = $( "#filter-select").val();
        if ( selectedValue === "filter-option-other-month" ) {
            $( "#other-month-picker" ).show();
            $( "#other-day-picker").hide();
        }
        else if (selectedValue == "filter-option-other-day") {
            $("#other-day-picker").show();
            $("#other-month-picker").hide();
        }
        else {
            $("#other-month-picker").hide();
            $( "#other-day-picker").hide();
        }
    };

    updatePickerB();
    $( "#filter-select" ).change( function () {
        updatePickerB();
    } );

    $('#btn-save-report').click(function () {
        add_edit=$(this).find("#add-or-edit").val();

        if(add_edit == "edit") {

            $.post(
                "/reports/edit",
                {
                    reportId: $('#report-form input[name=reportId]').val(),
                    date: $('#report-form  input[name=date]').val(),
                    duration: $('#report-form input[name=duration]').val(),
                    description: $('#report-form input[name=description]').val(),
                    remote: $('#report-form input[name=remote]').is(':checked')
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
                    date: $('#report-form  input[name=date]').val(),
                    duration: $('#report-form input[name=duration]').val(),
                    description: $('#report-form input[name=description]').val(),
                    remote: $('#report-form input[name=remote]').is(':checked')
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

function editReport ( reportId, date, duration, description, remote ) {
    $('#report-form input[name=date]').val(date);
    $('#report-form input[name=duration]').val(duration);
    $('#report-form input[name=description]').val(description);

    if(remote == "true") {
        $('#report-form input[name=remote]').prop('checked', remote);
    }
    else if (remote == "false") {
        $('#report-form input[name=remote]').removeAttr('checked');
    }

    $('#report-form input[name=reportId]').val(reportId);

    $('#report-form input[name=add-or-edit]').val("edit");

    $('#report-form button[id=btn-save-report]').removeAttr('disabled');
}

function filterOrSearch() {
    selectedOption=$("#filter-select :selected").val();

    if(selectedOption == "filter-option-other-month") {
        month = $("#filter-panel input[name=filter-month]").val();
        $.get(
            "/reports/userReports",
            {
                filter: month
            },
            function (data) {

                refreshUserReports(data);
            }
        ).fail( function( response ) {
            document.body.innerHTML = response.responseText;
        });
    }

    else if(selectedOption == "filter-option-current-month"){
        var today = new Date();
        var mm = today.getMonth() + 1; //January is 0!
        var yyyy = today.getFullYear();
        var strDate = "";

        if(mm < 10) {
            strDate = yyyy.toString() + "-0" + mm.toString();
        }
        else {
            strDate = yyyy.toString() + "-" + mm.toString();
        }

        $.get(
            "/reports/userReports",
            {
                filter: strDate
            },
            function (data) {

                refreshUserReports(data);
            }
        ).fail( function( response ) {
            document.body.innerHTML = response.responseText;
        });
    }

    else if(selectedOption == "filter-option-current-day"){
        var today = new Date();
        var dd = today.getDate();
        var mm = today.getMonth() + 1; //January is 0!
        var yyyy = today.getFullYear();
        var strDate = "";

        if(dd < 10) {
            dd='0'+dd
        }

        if( mm < 10 ) {
            mm='0'+mm
        }
        var strDate = yyyy.toString() + "-" + mm.toString() + "-" + dd.toString();
        alert(strDate);

        $.get(
            "/reports/userReports",
            {
                filter: strDate
            },
            function (data) {

                refreshUserReports(data);
            }
        ).fail( function( response ) {
            document.body.innerHTML = response.responseText;
        });
    }

    else if(selectedOption == "filter-option-other-day") {
        date = $("#filter-panel input[name=filter-date]").val();
        $.get(
            "/reports/userReports",
            {
                filter: date
            },
            function (data) {

                refreshUserReports(data);
            }
        ).fail( function( response ) {
            document.body.innerHTML = response.responseText;
        });
    }
}

function refreshUserReports(data) {
    alert( "Refreshing data ");
    var element = $( "#user-reports" );
    element.html( data );
}
