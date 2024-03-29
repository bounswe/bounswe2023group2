import React, { useState } from "react";
import axios from 'axios';


const emailReport = () => {

  // track form states
  const [reporter, setReporter] = useState("");
  const [activity, setActivity] = useState("");
  const [reason, setReason] = useState("");
  const [details, setDetails] = useState("");

  // state to show the result after submitting
  const [responseData, setResponseData] = useState(null);


  const handleSubmit = async (event) => {
    event.preventDefault();

    const data = {
      reporter: event.target.reporter.value,
      activity: event.target.activity.value,
      reason: event.target.reason.value,
      details: event.target.details.value,
    };

    const headers = {
      'Content-Type': 'application/json',
    };
    try {
      const response = await axios.post(`${process.env.NEXT_PUBLIC_BACKEND_URL}/emailreport`, data, headers);
      const result = await JSON.stringify(response.data);
      console.log(result)
      setReporter(reporter)
      setActivity(activity)
      setReason(reason)
      setDetails(details)
      window.alert("Your report has been sent")
      } catch (error) {
        window.alert(error)
    }
    
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
          placeholder="John Doe"
          onChange={(event) => setReporter(event.target.value)} required
          />
      </div>

      <div className="h_block">
        <label>Activity ID (where was the button clicked) </label>
        <input
          type="text"
          name="activity"
          placeholder="NEED352"
          onChange={(event) => setActivity(event.target.value)} required
          />
      </div>

      <div className="h_block">
        <label>Reason</label>
        <div>
          <select
            className="h_selector" 
            name="reason" 
            onChange={(event) => setReason(event.target.value)} required>
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
