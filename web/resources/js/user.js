$( document ).ready(function() {

    $('#addUserBtn').click(function () {
        $('#userModal-title').html( strings['title.add'] );
        $('#userModal').modal('show');
    });

    $('#addUserBtn2').click(function () {
        $('#userModal-title').html( strings['title.add'] );
        $('#userModal').modal('show');
    });

    $('.editUserBtnClass').click(function () {
        $('#userModal-title').html( strings['title.edit'] );
        $('#userModal').modal('show');
    });


    $('#userModal').on('show.bs.modal', function (event) {
        var button = $(event.relatedTarget) // Button that triggered the modal
        var recipient = button.data('whatever') // Extract info from data-* attributes
        // If necessary, you could initiate an AJAX request here (and then do the updating in a callback).
        // Update the modal's content. We'll use jQuery here, but you could use a data binding library or other methods instead.
        var modal = $(this)
        //modal.find('.modal-title').text('New message to ' + recipient)
        modal.find('.modal-body input').val(recipient)
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

                var selector = "#users-row-" + userId;
                $( selector ).remove();

                var tableBody = $( "#users-table tbody" );
                if ( tableBody.children().length == 0 )
                    location.reload();
            }
        );
    }
    else{
        return false;
    }
}
