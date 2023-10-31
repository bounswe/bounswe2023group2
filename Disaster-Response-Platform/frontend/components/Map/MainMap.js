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
import { Value } from "sass";

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
  const map = useMap();
  return (
    <Marker position={[41.08714, 29.043474]}>
      <Popup>You are here</Popup>
    </Marker>
  );
}

export default function Map({ isClickActivated, activateClick }) {
  const [MarkerArr, setMarkerArr] = useState([]);

  useEffect(() => {
    const storedMarkerArr = JSON.parse(localStorage.getItem('markerArr'));

    setMarkerArr(storedMarkerArr);
    console.log("stored Marker Arr :", storedMarkerArr);
  }, []);

  const { isOpen, onOpen, onOpenChange } = useDisclosure();
  const [center, setCenter] = useState({ lat: 41.08714, lng: 29.043474 });

  const [selectedPosition, setSelectedPosition] = useState({
    x_coord: "",
    y_coord: "",
  });

  const [data, setData] = useState({
    name: "",
    type: "",
    subType: "",
    dueDate: "",
    x_coord: "",
    y_coord: "",
  });

  const ZOOM_LEVEL = 15;
  const mapRef = useRef();

  

  const storeMarkerArr = (array) => {
    console.log("load data : ", array);

    localStorage.setItem("markerArr", JSON.stringify(array));
  };

  const fetchData = (newData) => {
    setData({
      name: newData.name,
      type: newData.type,
      subType: newData.sub_type,
      dueDate: newData.due_date,
      x_coord: selectedPosition.x_coord,
      y_coord: selectedPosition.y_coord,
    });

    setMarkerArr(MarkerArr => [...MarkerArr, data]);


    const storeData = MarkerArr
    storeMarkerArr(storeData);
  };

  const MarkerAdd = () => {
    var x = 0;
    var y = 0;

    const map = useMapEvents({
      click(e) {
        onOpen();

        x = e.latlng.lat;
        y = e.latlng.lng;
        activateClick();
        setSelectedPosition({
          x_coord: e.latlng.lat,
          y_coord: e.latlng.lng,
        });
        if (MarkerArr == undefined) {
          setData({
            ...data,
            x_coord: selectedPosition.x_coord,
            y_coord: selectedPosition.y_coord,
          });
          setMarkerArr([data]);
        } else {
          setData({
            ...data,
            x_coord: selectedPosition.x_coord,
            y_coord: selectedPosition.y_coord,
          });
          
          //setMarkerArr(MarkerArr => [...MarkerArr, data]);
        }
      },


      
    });

    return <></>;
  };

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
        <Marker
          position={[selectedPosition.x_coord, selectedPosition.y_coord]}
          icon={redIcon}
        >
          <Popup>
            <h3>Tür: {data.type} </h3>

            <h3>Alt Tür: {data.subType} </h3>

            <h3>Tarih: {data.dueDate} </h3>
          </Popup>
        </Marker>
        :<></>
        {isClickActivated ? <MarkerAdd /> : <></>}
        <AddResourceForm
          onOpenChange={onOpenChange}
          isOpen={isOpen}
          fetchData={fetchData}
        />
        {MarkerArr && MarkerArr.map(({ type, subType, dueDate, x_coord, y_coord }) => (
            <Marker position={[x_coord, y_coord]} icon={redIcon}>
              <Popup>
                <h3>Tür: {type} </h3>

                <h3>Alt Tür: {subType} </h3>

                <h3>Tarih: {dueDate} </h3>
              </Popup>
            </Marker>
          ))}
      </MapContainer>
    </div>
  );
}
