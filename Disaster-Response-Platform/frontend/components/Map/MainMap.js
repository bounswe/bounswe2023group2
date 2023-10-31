import React, { useState, useEffect, useRef } from "react";
import { useDisclosure } from "@nextui-org/react";
import AddResourceForm from "../AddResourceMap";


import {
  MapContainer,
  TileLayer,
  Marker,
  useMap,
  Popup,
  useMapEvents,
} from "react-leaflet";
import "leaflet/dist/leaflet.css";
import styles from "./MainMap.module.scss";
import "leaflet-defaulticon-compatibility";
import "leaflet-defaulticon-compatibility/dist/leaflet-defaulticon-compatibility.css";

var redIcon = new L.Icon({
  iconUrl:
    "https://raw.githubusercontent.com/pointhi/leaflet-color-markers/master/img/marker-icon-2x-red.png",
  shadowUrl:
    "https://cdnjs.cloudflare.com/ajax/libs/leaflet/0.7.7/images/marker-shadow.png",
  iconSize: [25, 41],
  iconAnchor: [12, 41],
  popupAnchor: [1, -34],
  shadowSize: [41, 41],
});

function LocationMarker({ lat, lng }) {
  const [MarkerArr, setMarkerArr] = useState([]);
  const map = useMap();
  return (
    <Marker position={[41.08714, 29.043474]}>
      <Popup>You are here</Popup>
    </Marker>
  );
}

export default function Map({isClickActivated}){
  const {isOpen, onOpen, onOpenChange} = useDisclosure();
  const [center, setCenter] = useState({ lat: 41.08714, lng: 29.043474 });
  const [selectedPosition, setSelectedPosition] = useState({
    x_coord: "",
    y_coord: "",
  });
  const ZOOM_LEVEL = 15;
  const mapRef = useRef();

  const MarkerAdd = () => {
    
    var x = 0;
    var y = 0;
    const map = useMapEvents({
        click(e){   
            onOpen();
            x = e.latlng.lat;
            y = e.latlng.lng;                   
            setSelectedPosition({
              x_coord:e.latlng.lat,
              y_coord:e.latlng.lng
            });
            //console.log(MarkerArr);
            //setMarkerArr(MarkerArr => [...MarkerArr, selectedPosition]);
                           
        },            
    })

    return (<Marker position={[x,y]} icon={redIcon}>
    <Popup>
      
    </Popup>
  </Marker>)
    
}








  return (
    <div className={styles.map}>
      <MapContainer
        center={center}
        zoom={ZOOM_LEVEL}
        ref={mapRef}
        className={styles.map}
      >

        <TileLayer
          attribution='&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
          url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
        />
        selectedPosition ? 
        <Marker position={[selectedPosition.x_coord, selectedPosition.y_coord]} icon={redIcon}>
          <Popup>
            new marker
          </Popup>
        </Marker> :
        <></>
        {isClickActivated ? (<MarkerAdd />):<></>}
        <AddResourceForm onOpenChange={onOpenChange} isOpen={isOpen}/>
      </MapContainer>
    </div>
  );
}
