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
    axios.get('http://127.0.0.1:8000/emailreport/')
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
  <div>
    <h1>Report an Activity</h1>
    <form onSubmit={handleSubmit}>
      <div>
        <label>Reporter</label>
        <input
          type="text"
          name="reporter"
          value="John Doe"
          onChange={(event) => setReporter(event.target.value)}/>
      </div>

      <div>
        <label>Activity ID</label>
        <input
          type="text"
          name="activity"
          value="NEED352"
          onChange={(event) => setActivity(event.target.value)}/>
      </div>

      <div>
        <label>Reason</label>
        <div>
          <select id="reason" name="reason" required>
            <option value="Misinformation">Misinformation</option>
            <option value="Spam">Spam</option>
          </select>
        </div>
      </div>

      <div>
        <label>More Details</label>
        <div>
          <textarea
            id="details"
            name="details"
            onChange={(event) => setDetails(event.target.value)} required>
          </textarea>
        </div>
      </div>  

      <button type="submit">Send Report</button>
    </form>
  </div>
  );
};

export default emailReport;
