import { useState, useEffect } from 'react';
import fetch from 'isomorphic-unfetch';

const TimeZone = () => {
  const [timezone, setTimezone] = useState('');

  useEffect(() => {
    const fetchTimezone = async () => {
      const res = await fetch('http://worldtimeapi.org/api/ip');
      const data = await res.json();
      setTimezone(data.timezone);
    };

    fetchTimezone();
  }, []);

  return (
    <div>
      <h1>Your timezone is:</h1>
      <p>{timezone}</p>
    </div>
  );
};

export default TimeZone;