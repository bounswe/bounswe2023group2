import { useState } from 'react';
import axios from 'axios';

const TimeZone = () => {
  const [timezone, setTimezone] = useState('');
  const [currentTime, setCurrentTime] = useState('');
  const [latitude, setLatitude] = useState('');
  const [longitude, setLongitude] = useState('');

  const handleSubmit = async (event) => {
    event.preventDefault();
    try {
      const res = await axios.post('${process.env.BACKEND_URL}/timezone/', {
        latitude,
        longitude,
      });
      setTimezone(res.data.zoneName);
      setCurrentTime(new Date(res.data.formatted).toLocaleTimeString());
    } catch (error) {
      console.error('Error:', error);
    }
  };

  return (
    <div>
      <form onSubmit={handleSubmit}>
        <label>
          Latitude:
          <input type="text" name="latitude" onChange={(event) => setLatitude(event.target.value)} />
        </label>
        <br />
        <label>
          Longitude:
          <input type="text" name="longitude" onChange={(event) => setLongitude(event.target.value)} />
        </label>
        <br />
        <input type="submit" value="Submit" disabled={!latitude || !longitude} />
      </form>
      {timezone && currentTime && (
        <div>
          <h1>Your timezone is:</h1>
          <p>{timezone}</p>
          <h1>Current Time:</h1>
          <p>{currentTime}</p>
        </div>
      )}
    </div>
  );
};

export default TimeZone;
