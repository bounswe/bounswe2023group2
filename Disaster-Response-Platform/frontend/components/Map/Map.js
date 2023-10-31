import React, { useState, useRef } from 'react'
import { MapContainer, TileLayer, Marker } from 'react-leaflet'
import 'leaflet/dist/leaflet.css'
import styles from './Map.module.scss'

export default function Map() {
  const [center, setCenter] = useState({ lat: 41.08714, lng: 29.043474 })
  const ZOOM_LEVEL = 15
  const mapRef = useRef()

  
  return (

      <div className={styles.map} >
        <MapContainer center={center} zoom={ZOOM_LEVEL} ref={mapRef} className={styles.map}>
          <TileLayer
            attribution='&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
            url='https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png'
            />
          {location.loaded && !location.error && (
            <Marker
            position={[
              location.coordinates.lat,
              location.coordinates.lng,
            ]}
            ></Marker>
            )}
        </MapContainer>
</div>
  
  )
}