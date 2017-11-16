import firebase from 'firebase';

const config1 = {
  apiKey: 'AIzaSyBVviSoI67-qCKNP8nHKZsRIHtGHot_ZOs',
  authDomain: 'twittercleaner-7476e.firebaseapp.com',
  databaseURL: 'https://twittercleaner-7476e.firebaseio.com',
  projectId: 'twittercleaner-7476e',
  storageBucket: 'twittercleaner-7476e.appspot.com',
  messagingSenderId: '328092237125'
};

const config = {
  apiKey: 'AIzaSyAui6CWd5xJscQRoH7bqI9-rho_-l_D2hY',
  authDomain: 'twitter-cleaner-3e810.firebaseapp.com',
  databaseURL: 'https://twitter-cleaner-3e810.firebaseio.com',
  projectId: 'twitter-cleaner-3e810',
  storageBucket: 'twitter-cleaner-3e810.appspot.com',
  messagingSenderId: '869022372749'
};
export const app = firebase.initializeApp(config);
export const provider = new firebase.auth.TwitterAuthProvider();
export const auth = firebase.auth();
export const ref = firebase.database().ref();
export default firebase;
