$(document).ready(function() {

    $('#userModalForm').bootstrapValidator({
        message: validateModalStrings['invalid.value.error.message'],
        live: 'enabled',
        submitButtons: '#btnSaveUser',
        feedbackIcons: {
            valid: 'glyphicon glyphicon-ok',
            invalid: 'glyphicon glyphicon-remove',
            validating: 'glyphicon glyphicon-refresh'
        },
        fields: {
            "user-name": {
                message: validateModalStrings['invalid.name.error.message'],
                validators: {
                    notEmpty: {
                        message: validateModalStrings['empty.name.error.message']
                    }
                }
            },
            "user-email": {
                validators: {
                    notEmpty: {
                        message: validateModalStrings['empty.email.error.message']
                    },
                    emailAddress: {
                        message: validateModalStrings['invalid.email.error.message']
                    }
                }
            }
        }
    });
});
