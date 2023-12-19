import React from "react";
import styles from "./SidePopup.module.scss";
// Import your styles here if needed
import { FaRegThumbsUp } from "react-icons/fa";
import { FaRegThumbsDown } from "react-icons/fa";
import { api } from "@/lib/apiUtils";


function SidePopup({ card, closePopup,labels }) {
  if (!card) return null;
  
  const handleUpvote = async () => {
    if(card.feedback <1){
      const result = await api.post(`/api/feedback/upvote`, {
        method: 'POST',
        headers: {
          "Content-Type": "application/json",
        }, body: JSON.stringify({ entityID: card._id, entityType: card.nre.toLowerCase() })
      })
      const data = await result.json();
    }
    else{
      const result = await api.post(`/api/feedback/unvote`, {
        method: 'POST',
        headers: {
          "Content-Type": "application/json",
        }, body: JSON.stringify({ entityID: card._id, entityType: card.nre.toLowerCase() })
      })
      const data = await result.json();
    }
    // Here you can implement the logic to handle the upvote
  };

  const handleDownvote = async () => {
    if(card.feedback <1){
      const result = await api.post(`/api/feedback/downvote`, {
        method: 'POST',
        headers: {
          "Content-Type": "application/json",
        }, body: JSON.stringify({ entityID: card._id, entityType: card.nre.toLowerCase() })
      })
      const data = await result.json();
    }
    else{
      const result = await api.post(`/api/feedback/unvote`, {
        method: 'POST',
        headers: {
          "Content-Type": "application/json",
        }, body: JSON.stringify({ entityID: card._id, entityType: card.nre.toLowerCase() })
      })
      const data = await result.json();
    }
  };

  return (
    <div className={`${styles.sidePopup} ${card ? styles.open : ""}`}>
      <button onClick={closePopup} className={styles.closeButton}>
        Close
        {labels.UI.close}
      </button>
      <div className={styles.text}>
        <h2 className={styles.title}> {card.nre} Details</h2>
        <div className={styles.content}>
          <p>{labels.sort_criteria.type}: {card.type}</p>
          <p>{labels.sort_criteria.description}: {card.description} </p>
          {/* Add more details as needed */}
        </div>
        <div className={styles.votingButtons}>
          <button onClick={handleUpvote} className={styles.upvoteButton}>
            <FaRegThumbsUp />
          </button>
          <button onClick={handleDownvote} className={styles.downvoteButton}>
            <FaRegThumbsDown />
          </button>
        </div>
      </div>
    </div>
  );
}

export default SidePopup;
