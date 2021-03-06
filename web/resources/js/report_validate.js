$(document).ready(function() {

    $('#report-form').bootstrapValidator({
        message: validateModalStrings['invalid.value.error.message'],
        live: 'enabled',
        submitButtons: '#btn-save-report',
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
            "duration": {
                validators: {
                    notEmpty: {
                        message: validateModalStrings['empty.duration.error.message']
                    },
                    between: {
                        min: 0,
                        max: 24,
                        message: validateModalStrings['invalid.duration.error.message']
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
