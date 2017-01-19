



$(document).ready(function() {

    $.validator.setDefaults({
        highlight: function(element) {
            $(element).closest('.form-group').addClass('has-error');
        },
        unhighlight: function(element) {
            $(element).closest('.form-group').removeClass('has-error');
        },
        errorElement: 'span',
        errorClass: 'help-block',
        errorPlacement: function(error, element) {
            if(element.parent('.input-group').length) {
                error.insertAfter(element.parent());
            } else {
                error.insertAfter(element);
            }
        }
    });

    $('#change-password-form').validate({
        rules: {
            'oldPassword': {
                minlength: 1,
                required: true
            },
            'newPassword': {
                minlength: 1,
                required: true
            },
            'newPasswordConfirmed': {
                minlength: 1,
                required: true,
                equalTo: "#newPassword"
            }
        }

    });
});
