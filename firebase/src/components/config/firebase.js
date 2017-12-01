import firebase from 'firebase';

const config = {
  apiKey: '',
  authDomain: '',
  databaseURL: '',
  projectId: '',
  storageBucket: '',
  messagingSenderId: ''
};

export const app = firebase.initializeApp(config);
export const provider = new firebase.auth.TwitterAuthProvider();
export const auth = firebase.auth();
export const ref = firebase.database().ref();
export default firebase;