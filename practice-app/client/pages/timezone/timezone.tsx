import { useState, useEffect } from 'react';
import fetch from 'isomorphic-unfetch';
import GoogleMapReact from 'google-map-react';

const TimeZone = () => {
  const [timezone, setTimezone] = useState('');
  const [currentTime, setCurrentTime] = useState('');

  useEffect(() => {
    const fetchTimezone = async () => {
      const res = await fetch('http://worldtimeapi.org/api/ip');
      const data = await res.json();
      setTimezone(data.timezone);
      const date = new Date(data.datetime);
      const options = { hour12: true, hour: 'numeric', minute: 'numeric', timeZone: data.timezone };
      setCurrentTime(date.toLocaleString('en-US', options));
    };

    fetchTimezone();
  }, []);

  const [map, setMap] = useState<google.maps.Map>();
  const [marker, setMarker] = useState<google.maps.Marker>();

  useEffect(() => {
    if (timezone && map) {
      const geocoder = new google.maps.Geocoder();
      geocoder.geocode({ address: timezone }, (results, status) => {
        if (status === google.maps.GeocoderStatus.OK) {
          map.setCenter(results[0].geometry.location);
          if (marker) marker.setMap(null);
          const newMarker = new google.maps.Marker({
            position: results[0].geometry.location,
            map,
            title: timezone,
          });
          setMarker(newMarker);
        }
      });
    }
  }, [timezone, map]);

  const handleMapLoad = (map: google.maps.Map) => {
    setMap(map);
    setMarker(marker);
    if (marker) {
      map.setCenter(marker.getPosition());
      map.setZoom(10);
    }
  };

  return (
    <div>
      <h1>Your timezone is:</h1>
      <p>{timezone}</p>
      <h1>Current Time:</h1>
      <p>{currentTime}</p>
      <div style={{ height: '400px', width: '100%' }}>
        <GoogleMapReact
          bootstrapURLKeys={{ key: process.env.NEXT_PUBLIC_API_KEY }}
          defaultCenter={{ lat: 0, lng: 0 }}
          defaultZoom={2}
          yesIWantToUseGoogleMapApiInternals
          onGoogleApiLoaded={({ map }) => handleMapLoad(map)}
        />
      </div>
    </div>
  );
};

export default TimeZone;
