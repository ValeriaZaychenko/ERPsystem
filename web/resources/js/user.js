$( document ).ready(function() {

    $('#addUserBtn').click(function () {
        $('#userModal-title').html( strings['title.add'] );

        $('#userModal input[name=user-name]').val("");
        $('#userModal input[name=user-email]').val("");
        $('#userModal option').val("");
        $('#userModal input[name=user-id]').val("").prop("disabled", true);
        $('#userModal button[id=btnSaveUser]').attr('disabled', 'disabled');;

        $('#userModal input[name=add-or-edit]').val("add");

        $('#userModal').modal('show');
    });

    $('.editUserBtnClass').click(function () {
        $('#userModal-title').html( strings['title.edit'] );

        name=$(this).find(".user-name").html();
        email=$(this).find(".user-email").html();
        role=$(this).find("#user-role").html();
        id=$(this).find(".user-id").html();


        $('#userModal input[name=user-name]').val(name);
        $('#userModal input[name=user-email]').val(email);
        $('#userModal option').val(role).prop("selected", true);
        $('#userModal input[name=user-id]').val(id).prop("disabled", false);

        $('#userModal input[name=add-or-edit]').val("edit");
        $('#userModal button[id=btnSaveUser]').removeAttr('disabled');

        $('#userModal').modal('show');
    });

    $('#btnSaveUser').click(function () {
        add_edit=$(this).find("#add-or-edit").val();

        if(add_edit == "add") {

            $.post(
                "/users/add",
                {
                    name: $('#userModal input[name=user-name]').val(),
                    email: $('#userModal input[name=user-email]').val(),
                    userRole: $('#userModal option:selected').text()
                },
                function () {
                    location.reload();
                }
            ).fail( function( response ) {
                document.body.innerHTML = response.responseText;
            });
        }
        else if (add_edit == "edit") {
            $.post(
                "/users/edit",
                {
                    id: $('#userModal input[name=user-id]').val(),
                    name: $('#userModal input[name=user-name]').val(),
                    email: $('#userModal input[name=user-email]').val(),
                    userRole: $('#userModal option:selected').text()
                },
                function () {
                    location.reload();
                }
            ).fail( function( response ) {
                document.body.innerHTML = response.responseText;
            });
        }
    });


    $('#userModal').on('shown.bs.modal', function () {
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
