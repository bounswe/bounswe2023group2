import { useState } from 'react';
import axios from 'axios';

const TimeZone = () => {
  const [latitude, setLatitude] = useState('');
  const [longitude, setLongitude] = useState('');
  const [timezone, setTimezone] = useState('');
  const [currentTime, setCurrentTime] = useState('');
  const [savedConversions, setSavedConversions] = useState([]);

  const handleSubmit = async (event) => {
    event.preventDefault();
    try {
      const res = await axios.post(`${process.env.BACKEND_URL}/timezone`, {
        latitude,
        longitude,
      });
      setTimezone(res.data.zoneName);
      setCurrentTime(new Date(res.data.formatted).toLocaleTimeString());
    } catch (error) {
      console.error('Error:', error);
    }
  };

  const handleSaveConversion = async (event) => {
    event.preventDefault();
    try {
      const res = await axios.post(`${process.env.BACKEND_URL}/convert_time`, {
        time: new Date().toISOString().slice(0, 19).replace('T', ' '),
        from_tz: timezone,
        to_tz: timezone,
      });
      const conversionName = res.data.conversion_name;
      await axios.post(`${process.env.BACKEND_URL}/save_conversion`, {
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
      await axios.post(`${process.env.BACKEND_URL}/delete_conversion`, {
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
      const res = await axios.get(`${process.env.BACKEND_URL}/saved_conversions`);
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
              <button type="submit" className="btn btn-primary" disabled={!latitude || !longitude}>
                Convert Time
              </button>
            </form>
            <br />
          {timezone && currentTime && (
          <div className="alert alert-success" role="alert">
            <h4 className="alert-heading">Conversion Result:</h4>
            <p>
              The current time in {timezone} is {currentTime}.
            </p>
            <hr />
            <p className="mb-0">
              <button className="btn btn-primary" onClick={handleSaveConversion}>
                Save Conversion
              </button>
            </p>
          </div>
        )}

        {savedConversions.length > 0 && (
          <div>
            <h2>Saved Conversions:</h2>
            <ul>
              {savedConversions.map((conv) => (
                <li key={conv}>
                  {conv}{' '}
                  <button className="btn btn-danger" onClick={(event) => handleDeleteConversion(event, conv)}>
                    Delete
                  </button>
                </li>
              ))}
            </ul>
          </div>
        )}

        <button className="btn btn-secondary" onClick={handleGetSavedConversions}>
          Get Saved Conversions
        </button>
      </div>
      <div className="col-md-8">
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
  </div>
  </body>
  );
};

export default TimeZone;