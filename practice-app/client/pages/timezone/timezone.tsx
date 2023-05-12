import { useState } from 'react';
import axios from 'axios';

const TimeZone = () => {
  const [latitude, setLatitude] = useState('');
  const [longitude, setLongitude] = useState('');
  const [timezone, setTimezone] = useState('');
  const [currentTime, setCurrentTime] = useState('');

  const [fromTime, setFromTime] = useState('');
  const [toTime, setToTime] = useState('');
  const [fromTimezone, setFromTimezone] = useState('');
  const [toTimezone, setToTimezone] = useState('');
  
  const [savedConversions, setSavedConversions] = useState([]);

  const handleSubmit = async (event) => {
    event.preventDefault();
    try {
      const res = await axios.post(`${process.env.NEXT_PUBLIC_BACKEND_URL}/timezone/get_timezone`, {
        latitude,
        longitude,
      });
      setTimezone(res.data.timezone);
      setCurrentTime(res.data.date);
    } catch (error) {
      console.error('Error:', error);
    }
  };

  const handleSaveConversion = async (event) => {
    event.preventDefault();
    try {
      const res = await axios.post(`${process.env.NEXT_PUBLIC_BACKEND_URL}/tz_conversion/convert_time`, {
        time: fromTime,
        from_tz: fromTimezone,
        to_tz: toTimezone,
      });
      setToTime(res.data.converted_time)
      const conversionName = res.data.conversion_name;
      await axios.post(`${process.env.BACKEND_URL}/tz_conversion/save_conversion`, {
        conversion_name: conversionName,
      });
      setSavedConversions([...savedConversions, conversionName]);
    } catch (error) {
      console.error('Error:', error);
    }
  };

  const handleDeleteConversion = async (event, conversionName) => {
    event.preventDefault();
    try {
      await axios.post(`${process.env.NEXT_PUBLIC_BACKEND_URL}/tz_conversion/delete_conversion`, {
        conversion_name: conversionName,
      });
      const updatedConversions = savedConversions.filter((conv) => conv !== conversionName);
      setSavedConversions(updatedConversions);
    } catch (error) {
      console.error('Error:', error);
    }
  };

  const handleGetSavedConversions = async (event) => {
    event.preventDefault();
    try {
      const res = await axios.get(`${process.env.NEXT_PUBLIC_BACKEND_URL}/tz_conversion/saved_conversions`);
      setSavedConversions(res.data.map((conv) => conv.conversion_name));
    } catch (error) {
      console.error('Error:', error);
    }
  };

  return (
<body>
   <div className="container">
      <div className="row">
         <div className="col-md-4">
            <h1>Timezone Converter</h1>
            <form onSubmit={handleSubmit}>
               <div className="form-group">
                  <label>Latitude</label>
                  <input
                     type="text"
                     className="form-control"
                     placeholder="Enter latitude"
                     value={latitude}
                     onChange={(event) => setLatitude(event.target.value)}
                  />
               </div>
               <div className="form-group">
                  <label>Longitude</label>
                  <input
                     type="text"
                     className="form-control"
                     placeholder="Enter longitude"
                     value={longitude}
                     onChange={(event) => setLongitude(event.target.value)}
                  />
               </div>
               <div className="result">
                  <p>Timezone: {timezone}</p>
                  <p>Current Time: {currentTime}</p>
               </div>
               <button type="submit" className="btn btn-primary">
               Convert Timezone
               </button>
            </form>
            <form onSubmit={handleSaveConversion}>
               <div className="form-group">
                  <label>Input Time</label>
                  <input
                     type="text"
                     className="form-control"
                     placeholder="Enter from time"
                     value={fromTime}
                     onChange={(event) => setFromTime(event.targetvalue)}
                  />
               </div>
               <div className="form-group">
                  <label>Input Timezone</label>
                  <input
                     type="text"
                     className="form-control"
                     placeholder="Enter from timezone"
                     value={fromTimezone}
                     onChange={(event) => setFromTimezone(event.targetvalue)}
                  />
               </div>
               <div className="form-group">
                  <label>Output Timezone</label>
                  <input
                     type="text"
                     className="form-control"
                     placeholder="Enter to timezone"
                     value={toTimezone}
                     onChange={(event) => setToTimezone(event.target.value)}
                  />
               </div>
               
               <div className="row">
                  <div className="col-md-12">
                     <div className="result">
                        <p>Converted Time: {toTime}</p>
                     </div>
                  </div>
               </div>
               
               <button type="submit" className="btn btn-primary">
                Save Conversion
               </button>
            </form>
         </div>
         <div className="col-md-8">
            <h1>Saved Conversions</h1>
            <button className="btn btn-primary" onClick={handleGetSavedConversions}>
            Get Saved Conversions
            </button>
            <ul className="list-group">
               {savedConversions.map((conversionName) => (
               <li key={conversionName} className="list-group-item">
                  {conversionName}
                  <button
                     className="btn btn-danger float-right"
                     onClick={(event) => handleDeleteConversion(event, conversionName)}
                  >
                  Delete
                  </button>
               </li>
               ))}
            </ul>
         </div>
      </div>
      <div className="col-md-8">
        Show on Map
         <iframe
         src={`https://maps.google.com/maps?q=${latitude},${longitude}&z=10&output=embed`}
         width="100%"
         height="480"
         frameBorder="0"
         style={{ border: 0 }}
         allowFullScreen=""
         aria-hidden="false"
         tabIndex="0"
         ></iframe>
      </div>
   </div>
</body>
  );
};

export default TimeZone;