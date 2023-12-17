
import React from 'react';
import styles from "./SidePopup.module.scss";
// Import your styles here if needed

function SidePopup({ resource, closePopup, labels }) {
  if (!resource) return null;

  return (
    <div className={`${styles.sidePopup} ${resource ? styles.open : ''}`}>
      <button 
        onClick={closePopup}
        className={styles.closeButton}
      >
        {labels.UI.close}
      </button>
      <div className={styles.text}>
      <h2 className={styles.title}>Resource Details</h2>
      <div className={styles.content}>
        <p>{labels.sort_criteria.type}: {resource.type}</p>
        <p>{labels.sort_criteria.description}: {resource.description}</p>
        {/* Add more details as needed */}
      </div>
      </div>
    </div>
  );
}

export default SidePopup;