# FirebaseReact
Web based application using firebase.

## Requirements
Node: 6.0.0^

## Instructions
1. Install all of the node packages in two locations.
    1. In the firebase folder (./firebase)
        ```terminal
        npm i
        ```
    2. In the firebase functions folder (./firebase/functions)
        ```terminal
        npm i
        ```
2. Fill the config files in two locations.
    1. In the auth folder in the config.js file (./firebase/auth/config.js)
        ```js
        var config = {
            firebase: {
                apiKey: "",
                authDomain: "",
                databaseURL: "",
                projectId: "",
                storageBucket: "",
                messagingSenderId: ""
            }, twitter: {
                consumer_key:         '',
                consumer_secret:      '',
                access_token:         '',
                access_token_secret:  '',
                timeout_ms:           60*1000,  // optional HTTP request timeout to apply to all requests.
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
        }
        ```
    2. In the config folder in the src folder there is a firebase.js that also needs to be filled in (./firebase/src/config/firebase.js)
        ```js
        const config = {
            apiKey: '',
            authDomain: '',
            databaseURL: '',
            projectId: '',
            storageBucket: '',
            messagingSenderId: ''
        };
        ```
3. You have to login to firebase before you can use the code. You can login by using the following command.
```terminal
node_modules/.bin/firebase login
```
4. After logging in, you can now compile and run the code locally using the following command line code.
```terminal
npm run webpack && npm run babel && node_modules/.bin/firebase serve --only functions,hosting
```