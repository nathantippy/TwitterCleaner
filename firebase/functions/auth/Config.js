"use strict";

var config = {
    firebase: {
        apiKey: "",
        authDomain: "",
        databaseURL: "",
        projectId: "",
        storageBucket: "",
        messagingSenderId: ""
    }, twitter: {
        consumer_key: '',
        consumer_secret: '',
        access_token: '',
        access_token_secret: '',
        timeout_ms: 60 * 1000 // optional HTTP request timeout to apply to all requests.
    }, firebaseAdmin: {
        credential: "",
        databaseURL: ""
    }, firebaseAdminSDK: {
        "type": "",
        "project_id": "",
        "private_key_id": "",
        "private_key": "",
        "client_email": "",
        "client_id": "",
        "auth_uri": "",
        "token_uri": "",
        "auth_provider_x509_cert_url": "",
        "client_x509_cert_url": ""
    }
};

module.exports = config;