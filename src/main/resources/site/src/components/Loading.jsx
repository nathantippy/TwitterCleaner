import React from 'react';
import styles from '../scss/Loading.module.scss';
const Loading = () => {
  return (
    <div className={styles.body}>
      <img className={styles.image} src="OCILogo.png" />
    </div>
  );
};

export default Loading;
