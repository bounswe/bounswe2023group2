import React, { useState } from 'react';
import axios from 'axios';

export default function GetAQData() {
    const [country, setCountry] = useState("");
    const [city, setCity] = useState("");
    const [sort, setSort] = useState("asc");
    const [aqData, setAqData] = useState(null);

    const fetchData = async () => {
        try {
            const response = await axios.get(`${process.env.NEXT_PUBLIC_BACKEND_URL}/filtersort/aqdata?country=${country}&city=${city}&sort=${sort}`);
            setAqData(response.data);
        } catch (error) {
            console.error("Error fetching data", error);
            setAqData(null);
        }
    };

    const inputStyle = {
        margin: '0.5rem 0',
        padding: '0.5rem',
        width: '200px',
        color: "black"
    };

    const buttonStyle = {
        padding: '0.5rem 1rem',
        margin: '1rem 1rem',
        backgroundColor: '#007BFF',
        color: '#fff',
        border: 'none',
        borderRadius: '4px',
        cursor: 'pointer'
    };

    const dataContainerStyle = {
        margin: '1rem 0',
        padding: '1rem',
        border: '1px solid #ddd',
        borderRadius: '4px',
        backgroundColor: '#f9f9f9',
        color: "black"
    };

    return (
        <div>
            <h1>Get Air Quality Data</h1>
            <label> Country Code </label>
            <input style={inputStyle} type="text" value={country} onChange={e => setCountry(e.target.value)} placeholder="TR" />
            <label> City Name </label>
            <input style={inputStyle} type="text" value={city} onChange={e => setCity(e.target.value)} placeholder="İstanbul" />
            <label> Sort by Last Update Direction </label>
            <select style={inputStyle} value={sort} onChange={e => setSort(e.target.value)}>
                <option value="asc">Ascending</option>
                <option value="desc">Descending</option>
            </select>
            <button style={buttonStyle} onClick={fetchData}>Fetch Data</button>
            {aqData && (
                <div style={dataContainerStyle}>
                    <h2>Air Quality Data:</h2>
                    <pre>{JSON.stringify(aqData, null, 2)}</pre>
                </div>
            )}
        </div>
    );
}
