import React, { useState } from "react";
import axios from 'axios';


const emailReport = () => {

  // track form states
  const [reporter, setReporter] = useState("");
  const [activity, setActivity] = useState("");
  const [details, setDetails] = useState("");

  // state to show the result after submitting
  const [responseData, setResponseData] = useState(null);

  const handleSubmit = () => {
    // Send a POST request to your API
    axios.get(process.env.NEXT_PUBLIC_BACKEND_URL + "/emailreport")
    .then(response => {
      // Handle the response from the API
      console.log('API response:', response.data);
      setResponseData(response.data);
    })
    .catch(error => {
      // Handle any error that occurred during the request
      console.error('Error:', error);
    });
  };


  return (
  <div className="h_form">
    <form onSubmit={handleSubmit} className="h_container">
      <h1>Report an Activity</h1>
      <div className="h_block">
        <label>Reporter (who clicked the button)</label>
        <input
          type="text"
          name="reporter"
          value="halil.gurbuz@boun.edu.tr"
          onChange={(event) => setReporter(event.target.value)}
          />
      </div>

      <div className="h_block">
        <label>Activity ID (where was the button clicked) </label>
        <input
          type="text"
          name="activity"
          value="NEED352"
          onChange={(event) => setActivity(event.target.value)}
          />
      </div>

      <div className="h_block">
        <label>Reason</label>
        <div>
          <select className="h_selector" id="reason" name="reason" required>
            <option value="Misinformation">Misinformation</option>
            <option value="Spam">Spam</option>
          </select>
        </div>
      </div>

      <div className="h_message h_block">
        <label>More Details</label>
        <div>
          <textarea
            id="details"
            name="details"
            onChange={(event) => setDetails(event.target.value)} required>
          </textarea>
        </div>
      </div>  
      <div className="h_button h_block">
        <button type="submit">Send Report</button>
      </div>
      
    </form>
  </div>
  );
};

export default emailReport;
