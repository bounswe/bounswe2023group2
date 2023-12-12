import React, { useState, useEffect, useRef } from "react";
import { useDisclosure } from "@nextui-org/react";
import AddResourceForm from "../AddResourceMap";
import { useRouter } from "next/router";
import SidePopup from './SidePopup';
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
  shadowSize: [21, 21],
});

function LocationMarker({ lat, lng }) {
  const map = useMap();
  return (
    <Marker position={[41.08714, 29.043474]}>
      <Popup>You are here</Popup>
    </Marker>
  );
}

export default function Map({
  isClickActivated,
  activateClick,
  resourceApiData,
}) {
  const [MarkerArr, setMarkerArr] = useState([]);

  useEffect(() => {
    // Here you can use apiData to set markers or perform other actions
    if (resourceApiData.length > 0) {
      // Process and use the apiData
    }
  }, [resourceApiData]);

  const { isOpen, onOpen, onOpenChange } = useDisclosure();
  const [center, setCenter] = useState({ lat: 41.08714, lng: 29.043474 });
  const [selectedMarker, setSelectedMarker] = useState(null);
  const [selectedPosition, setSelectedPosition] = useState({
    x_coord: "",
    y_coord: "",
  });
  const closePopup = () => {
    setSelectedMarker(null);
  };
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
      // created_by: "Şahin",
      // description: ,
      // initialQuantity: ,
      // currentQuantity: ,
      // type: ,
      // details: ,
      // x: selectedPosition.x_coord,
      // y: selectedPosition.y_coord,
      // recurrence_id: ,
      // recurrence_rate: ,
      // recurrence_deadline: ,
      // active: ,
      // occur_at: ,
      // created_at: ,
      // last_updated_at: ,
      // upvote: 0,
      // downvote: 0,

      name: newData.name,
      type: newData.type,
      subType: newData.sub_type,
      dueDate: newData.due_date,
      x_coord: selectedPosition.x_coord,
      y_coord: selectedPosition.y_coord,
    });

    setMarkerArr((MarkerArr) => [...MarkerArr, data]);

    const storeData = MarkerArr;
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
    <div className={`${styles.map} ${selectedMarker ? styles['with-popup'] : ''}`}>
      {/* <SidePopup resource={selectedMarker} closePopup={closePopup} /> */}
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
        {selectedPosition && (
          <Marker
            position={[selectedPosition.x_coord, selectedPosition.y_coord]}
            icon={redIcon}
          >
            <Popup>{/* Popup content */}</Popup>
          </Marker>
        )}

        {resourceApiData.map((resource, index) => (
          <Marker
            key={index}
            position={[resource.x, resource.y]}
            icon={redIcon}
            eventHandlers={{
              click: () => {
                setSelectedMarker(resource);
              },
            }}
          >
            <Popup>
              <h3>Tür: {resource.type}</h3>
              {/* Other details you want to show in the popup */}
            </Popup>
          </Marker>
        ))}

        {isClickActivated ? <MarkerAdd /> : <></>}
        <AddResourceForm
          onOpenChange={onOpenChange}
          isOpen={isOpen}
          fetchData={fetchData}
        />
        {MarkerArr &&
          MarkerArr.map(({ type, subType, dueDate, x_coord, y_coord }) => (
            <Marker position={[x_coord, y_coord]} icon={redIcon}>
              <Popup>
                <h3>Tür: {type} </h3>

                <h3>Alt Tür: {subType} </h3>

                <h3>Tarih: {dueDate} </h3>
              </Popup>
            </Marker>
          ))}
      </MapContainer>
      <SidePopup resource={selectedMarker} closePopup={closePopup} />
    </div>
  );
}
