$( document ).ready(function() {

    var updateDatepicker = function () {
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

    updateDatepicker();
    $( "#filter-select" ).change( function () {
        updateDatepicker();
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
    var dateParser = getStringFromDate();

    selectedOptionFilter = $("#filter-select :selected").val();
    selectedOptionGrouping = $("#groupby :selected").val();

    switch (selectedOptionFilter) {
        case "filter-option-other-month":
            month = $("#filter-panel input[name=filter-month]").val();
            if (selectedOptionGrouping === undefined)
                getReportsFilterBy(month);
            else
                getReportsAndThenGroup(month, selectedOptionGrouping);
            break;

        case "filter-option-current-month":
            if (selectedOptionGrouping === undefined)
                getReportsFilterBy(dateParser.getMonthStrDate);
            else
                getReportsAndThenGroup(dateParser.getMonthStrDate, selectedOptionGrouping);
            break;

        case "filter-option-current-day":
            if (selectedOptionGrouping === undefined)
                getReportsFilterBy(dateParser.getFullStrDate);
            else
                getReportsAndThenGroup(dateParser.getFullStrDate, selectedOptionGrouping);
            break;

        case "filter-option-other-day":
            date = $("#filter-panel input[name=filter-date]").val();
            if (selectedOptionGrouping === undefined)
                getReportsFilterBy(date);
            else
                getReportsAndThenGroup(date, selectedOptionGrouping);
            break;
    }
}

function getReportsFilterBy(data){
    $.get(
        "/reports/userReports",
        {
            filter: data
        },
        function (data) {
            refreshUserReports(data);
        }
    ).fail( function( response ) {
        document.body.innerHTML = response.responseText;
    });
}

function getReportsAndThenGroup(filter, groupBy){
    $.get(
        "/reports/userReports",
        {
            filter: filter,
            groupBy: groupBy
        },
        function (data) {
            refreshUserReports(data);
        }
    ).fail( function( response ) {
        document.body.innerHTML = response.responseText;
    });
}

function refreshUserReports(data) {
    var element = $( "#user-reports" );
    element.html( data );
}
