import React from 'react';
import CircularProgress from 'material-ui/CircularProgress';
const Loading = () => {
  return (
    <div style={{position: 'absolute', top: '50%', right: '50%'}}>
      <CircularProgress size={60} thickness={5} />
    </div>
  );
};

export default Loading;
