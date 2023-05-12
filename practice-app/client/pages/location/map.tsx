import { MapContainer, Marker, Popup, TileLayer } from 'react-leaflet'
import 'leaflet/dist/leaflet.css'
import * as L from 'leaflet';
import { useState } from 'react';
import iconMarker from 'leaflet/dist/images/marker-icon.png'
import iconRetina from 'leaflet/dist/images/marker-icon.png'
import iconShadow from 'leaflet/dist/images/marker-shadow.png'
import axios from 'axios';

var greenIcon = new L.Icon({
  iconUrl: 'https://raw.githubusercontent.com/pointhi/leaflet-color-markers/master/img/marker-icon-2x-green.png',
  shadowUrl: 'https://cdnjs.cloudflare.com/ajax/libs/leaflet/0.7.7/images/marker-shadow.png',
  iconSize: [25, 41],
  iconAnchor: [12, 41],
  popupAnchor: [1, -34],
  shadowSize: [41, 41]
});

//const [responseData, setResponseData] = useState(null);
// var map = L.map('map').setView([37, 37], 8);
// L.tileLayer(
//   'http://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
//     attribution: '&copy; <a href="http://osm.org/copyright">OpenStreetMap</a> contributors',
//     maxZoom: 18,
//   }).addTo(map);

const MarkerCLust = () => {

};

const MyButton = () => {
  const [responseData, setResponseData] = useState(null);
  const handleClick = () => {
    // Send a POST request to your API
    axios.get('http://127.0.0.1:8000/location/')
    .then(response => {
      // Handle the response from the API
      console.log('API response:', response.data);
      setResponseData(response.data);
      const MarkerCLust = () => {
        
      };
    })
    .catch(error => {
      // Handle any error that occurred during the request
      console.error('Error:', error);
    });
  };

  return (
    <div>
      <button onClick={handleClick}>Collect Data</button>
      {responseData && (
        <div>
          {responseData[0].length}
        </div>
      )}
    </div>
  );
};


//   for (var i = 0; i < responseData[0].length; i++) {
//     marker = new L.marker(responseData[0][i], responseData[1][i]])
//       .bindPopup(responseData[1][i])
//       .addTo(map);
//   }

const Map = () => {
  return (
    <div>
      <div id="map"></div>
    </div>
  )

}



// const Map = () => {
//   return (
//     <div>
//       <script>collectData();</script>
//       <div style={{textAlign:"center" }}>MAP</div>
//       <MyButton />
//     <MapContainer center={[37,37]} zoom={5
//     } scrollWheelZoom={false} style={{height: 800, width: "100%"}}>
//       <TileLayer
//         attribution='&copy; <a href="http://osm.org/copyright">OpenStreetMap</a> contributors'
//         url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
//       />
//       <Marker position={[37,37]} icon={greenIcon}>
//         <Popup>
//           A pretty CSS3 popup. <br /> Easily customizable.
//         </Popup>
//       </Marker>
//     </MapContainer>
//     </div>
//   )
// }

export default Map