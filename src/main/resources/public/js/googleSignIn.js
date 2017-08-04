function onSignIn(googleUser) {
    var id_token = googleUser.getAuthResponse().id_token;
    console.log("ID Token: " + id_token);
    var xhr = new XMLHttpRequest();
    xhr.open('POST', window.location.href);
    console.log(window.location.href);
    xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
    xhr.onload = function () {
        if (xhr.responseText != 'null') {
            console.log('Signed in as: ' + xhr.responseText);
            console.log('Redirecting to /lobby');
            window.location.replace(window.location.protocol + '//' + window.location.host + '/lobby');
        }
    };
    xhr.send("idToken=" + id_token);
}