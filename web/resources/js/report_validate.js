$(document).ready(function() {

    $('#reportForm').bootstrapValidator({
        message: validateModalStrings['invalid.value.error.message'],
        live: 'enabled',
        submitButtons: '#btnSaveReport',
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
            "time": {
                validators: {
                    notEmpty: {
                        message: validateModalStrings['empty.time.error.message']
                    },
                    between: {
                        min: 0,
                        max: 24,
                        message: validateModalStrings['invalid.time.error.message']
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
