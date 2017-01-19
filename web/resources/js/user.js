$( document ).ready(function() {

    $('#add-user-btn').click(function () {
        $('#user-modal-title').html( strings['title.add'] );

        $('#user-modal input[name=user-name]').val("");
        $('#user-modal input[name=user-email]').val("");
        $('#user-modal option').val("");
        $('#user-modal input[name=user-id]').val("").prop("disabled", true);
        $('#user-modal button[id=btnSaveUser]').attr('disabled', 'disabled');

        $('#user-modal input[name=add-or-edit]').val("add");

        $('#user-modal #error-hint').hide();

        $('#user-modal').modal('show');
    });

    $('.edit-user-btn').click(function () {
        $('#user-modal-title').html( strings['title.edit'] );

        name=$(this).find(".user-name").html();
        email=$(this).find(".user-email").html();
        role=$(this).find("#user-role").html();
        id=$(this).find(".user-id").html();


        $('#user-modal input[name=user-name]').val(name);
        $('#user-modal input[name=user-email]').val(email);
        $('#user-modal option').val(role).prop("selected", true);
        $('#user-modal input[name=user-id]').val(id).prop("disabled", false);

        $('#user-modal input[name=add-or-edit]').val("edit");
        $('#user-modal button[id=btnSaveUser]').removeAttr('disabled');

        $('#user-modal #error-hint').hide();

        $('#user-modal').modal('show');
    });

    $('#btnSaveUser').click(function () {
        add_edit=$(this).find("#add-or-edit").val();

        if(add_edit == "add") {

            $.post(
                "/users/add",
                {
                    name: $('#user-modal input[name=user-name]').val(),
                    email: $('#user-modal input[name=user-email]').val(),
                    userRole: $('#user-modal option:selected').text()
                },
                function () {
                    location.reload();
                }
            ).fail( function( response ) {
                $('#user-modal #error-hint').html(response.responseText).show();
             });
        }
        else if (add_edit == "edit") {
            $.post(
                "/users/edit",
                {
                    id: $('#user-modal input[name=user-id]').val(),
                    name: $('#user-modal input[name=user-name]').val(),
                    email: $('#user-modal input[name=user-email]').val(),
                    userRole: $('#user-modal option:selected').text()
                },
                function () {
                    location.reload();
                }
            ).fail( function( response ) {
                $('#user-modal #error-hint').html( response.responseText ).show();
            });
        }
    });


    $('#user-modal').on('shown.bs.modal', function () {
        $("input[name=user-name]").focus();
    });
});


function deleteUser ( userId ) {
    if(confirm(strings['confirm.delete'])){
        $.post(
            "/users/delete",
            {
                id: userId
            },
            function () {

                /*var selector = "#users-row-" + userId;
                $( selector ).remove();

                var tableBody = $( "#users-table tbody" );
                if ( tableBody.children().length == 0 )*/
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
