function setLocale ( lang ) {
    $.post(
        "/setlocale/",
        {
            language: lang
        },
        function () {
            location.reload();
        }
    ).fail( function() {
        alert( "Failed to change locale" );
    });
}
