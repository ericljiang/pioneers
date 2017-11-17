import React from 'react';
import ReactDOM from 'react-dom';
import './index.css';
import App from './App';
import registerServiceWorker from './registerServiceWorker';

ReactDOM.render(<App />, document.getElementById('root'));
registerServiceWorker();

window.onGoogleYoloLoad = (googleyolo) => {
    // The 'googleyolo' object is ready for use.
    const retrievePromise = window.googleyolo.retrieve({
        supportedAuthMethods: [
            "https://accounts.google.com"
        ],
        supportedIdTokenProviders: [
            {
                uri: "https://accounts.google.com",
                clientId: "224119011410-5hbr37e370ieevfk9t64v9799kivttan.apps.googleusercontent.com"
            }
        ]
    });
    retrievePromise.then((credential) => {
        if (credential.password) {
            // An ID (usually email address) and password credential was retrieved.
            // Sign in to your backend using the password.
            // window.signInWithEmailAndPassword(credential.id, credential.password);
        } else {
            // A Google Account is retrieved. Since Google supports ID token responses,
            // you can use the token to sign in instead of initiating the Google sign-in
            // flow.
            // window.useGoogleIdTokenForAuth(credential.idToken);
        }
    }, (error) => {
        console.log(error);
        // Credentials could not be retrieved. In general, if the user does not
        // need to be signed in to use the page, you can just fail silently; or,
        // you can also examine the error object to handle specific error cases.

        // If retrieval failed because there were no credentials available, and
        // signing in might be useful or is required to proceed from this page,
        // you can call `hint()` to prompt the user to select an account to sign
        // in or sign up with.
        if (error.type === 'noCredentialsAvailable') {
            const hintPromise = window.googleyolo.hint({
                supportedAuthMethods: [
                    "https://accounts.google.com"
                ],
                supportedIdTokenProviders: [
                    {
                        uri: "https://accounts.google.com",
                        clientId: "224119011410-5hbr37e370ieevfk9t64v9799kivttan.apps.googleusercontent.com"
                    }
                ]
            });
        }
    });
};
