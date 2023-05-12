import { MapContainer, Marker, Popup, TileLayer,useMapEvents } from 'react-leaflet'
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

var redIcon = new L.Icon({
  iconUrl: 'https://raw.githubusercontent.com/pointhi/leaflet-color-markers/master/img/marker-icon-2x-red.png',
  shadowUrl: 'https://cdnjs.cloudflare.com/ajax/libs/leaflet/0.7.7/images/marker-shadow.png',
  iconSize: [25, 41],
  iconAnchor: [12, 41],
  popupAnchor: [1, -34],
  shadowSize: [41, 41]
});

const Map = () => {
  const [MarkerArr, setMarkerArr] = useState([]);
  const [selectedPosition, setSelectedPosition] = useState({x_coord: "" ,y_coord:""});
  //{x_coord: "" ,y_coord:""}
  const [responseData, setResponseData] = useState(null);
  const MyFetchButton = () => {

    const handleClickFetch = () => {

      // Send a POST request to your API
      axios.get(process.env.NEXT_PUBLIC_BACKEND_URL+'location')
        .then(response => {
          // Handle the response from the API
          console.log('API response:', response.data);
          setResponseData(response.data);
          
        })
        .catch(error => {
          // Handle any error that occurred during the request
          console.error('Errror:', error);
        });
    };

    return (
      <div>
        <button onClick={handleClickFetch}>Collect Data</button>
        
      </div>
    );
  };
  const MyInsertButton = () => {
  
    const handleClickInsert = () => {

      // Send a POST request to your API
      console.log(MarkerArr)
      MarkerArr[0].x_coord = selectedPosition.x_coord;
      MarkerArr[0].y_coord = selectedPosition.y_coord;
      

      axios.post(process.env.NEXT_PUBLIC_BACKEND_URL+'location/insert' ,JSON.stringify({items: MarkerArr}),{
        headers: {
          'content-type': 'application/json'
        }
      });
      setMarkerArr([]);
      setSelectedPosition({x_coord: "" ,y_coord:""});
      axios.get(process.env.NEXT_PUBLIC_BACKEND_URL+'location')
        .then(response => {
          // Handle the response from the API
          console.log('API response:', response.data);
          setResponseData(response.data);
          
        })
        .catch(error => {
          // Handle any error that occurred during the request
          console.error('Error:', error);
        });
            //setMarkerArr(MarkerArr => [...MarkerArr, selectedPosition]);
            console.log(MarkerArr);

        //JSON.stringify(MarkerArr)
    };

    return (
      <div>
        <button onClick={handleClickInsert}>Insert Data</button>
        
      </div>
    );
  };
  
  const MarkerAdd = () => {
    var x = 0;
    var y = 0;
    const map = useMapEvents({
        click(e){   
            x = e.latlng.lat;
            y = e.latlng.lng;                   
            setSelectedPosition({
              x_coord:e.latlng.lat,
              y_coord:e.latlng.lng
            });
            console.log(MarkerArr);
            setMarkerArr(MarkerArr => [...MarkerArr, selectedPosition]);
                           
        },            
    })

    return (<Marker position={[x,y]} icon={redIcon}>
    <Popup>
      new markerr
    </Popup>
  </Marker>)
    
}
    


  return (
    <div>

      <div style={{ textAlign: "center" }}>MAP</div>
      <div className = "box-content w-32 p-1 border-2 float-left"><MyFetchButton /></div>
      <div className = "box-content w-32 p-1 border-2 float-right"><MyInsertButton /></div>
      
      <MapContainer center={[37, 37]} zoom={7
      } scrollWheelZoom={false} style={{ height: 800, width: "100%" }}>
        <TileLayer
          attribution='&copy; <a href="http://osm.org/copyright">OpenStreetMap</a> contributors'
          url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
        />
        <MarkerAdd/>
        
        

        
        {responseData && responseData.map(({x_coord, y_coord ,username}, index) => (
          <Marker position={[x_coord, y_coord]} icon={greenIcon} key={index}>
            <Popup>
              {username} is username of the user
            </Popup>
          </Marker>
        ))}
        
        selectedPosition ? 
        <Marker position={[selectedPosition.x_coord, selectedPosition.y_coord]} icon={redIcon}>
          <Popup>
            new marker
          </Popup>
        </Marker> :
        <></>

        {MarkerArr && MarkerArr.map(({x_coord,y_coord}) =>(
          <Marker position={[x_coord, y_coord]} icon={redIcon}>
          <Popup>
            new marker
          </Popup>
        </Marker>
        ))}

      </MapContainer>
      
    </div>
  )
}

export default Map