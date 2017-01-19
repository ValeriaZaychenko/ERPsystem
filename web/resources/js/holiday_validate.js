$(document).ready(function() {

    $('#holiday-form').bootstrapValidator({
        message: validateModalStrings['invalid.value.error.message'],
        live: 'enabled',
        submitButtons: '#save-holiday-btn',
        feedbackIcons: {
            valid: 'glyphicon glyphicon-ok',
            invalid: 'glyphicon glyphicon-remove',
            validating: 'glyphicon glyphicon-refresh'
        },
        fields: {
            "date": {
                message: validateModalStrings['invalid.date.error.message'],
                validators: {
                    notEmpty: {
                        message: validateModalStrings['empty.date.error.message']
                    },
                    date: {
                        message: validateModalStrings['invalid.date.error.message']
                    }
                }
            },
            "description": {
                validators: {
                    notEmpty: {
                        message: validateModalStrings['empty.description.error.message']
                    }
                }
            }
        }
    });
});