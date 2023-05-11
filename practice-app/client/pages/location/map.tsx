import { MapContainer, Marker, Popup, TileLayer } from 'react-leaflet'
import 'leaflet/dist/leaflet.css'
import * as L from 'leaflet';
import iconMarker from 'leaflet/dist/images/marker-icon.png'
import iconRetina from 'leaflet/dist/images/marker-icon.png'
import iconShadow from 'leaflet/dist/images/marker-shadow.png'

var greenIcon = new L.Icon({
  iconUrl: 'https://raw.githubusercontent.com/pointhi/leaflet-color-markers/master/img/marker-icon-2x-green.png',
  shadowUrl: 'https://cdnjs.cloudflare.com/ajax/libs/leaflet/0.7.7/images/marker-shadow.png',
  iconSize: [25, 41],
  iconAnchor: [12, 41],
  popupAnchor: [1, -34],
  shadowSize: [41, 41]
});

const Map = () => {
  return (
    <div>
      <div style={{textAlign:"center" }}>MAP</div>
    <MapContainer center={[37,37]} zoom={5
    } scrollWheelZoom={true} style={{height: 800, width: "100%"}}>
      <TileLayer
        attribution='&copy; <a href="http://osm.org/copyright">OpenStreetMap</a> contributors'
        url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
      />
      <Marker position={[37,37]} icon={greenIcon}>
        <Popup>
          A pretty CSS3 popup. <br /> Easily customizable.
        </Popup>
      </Marker>
    </MapContainer>
    </div>
  )
}

export default Map