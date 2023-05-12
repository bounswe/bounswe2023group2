import React, { useState, useEffect } from 'react';
import axios from 'axios';

export default function FilterSortPage() {
  const [resources, setResources] = useState(null);
  const [aqdata, setAqdata] = useState(null);

  const ResourceCard = (resourceItem) => {
    let item = resourceItem.resourceItem
    return (
      <div className="max-w-sm p-6 bg-white border border-gray-200 rounded-lg shadow dark:bg-gray-800 dark:border-gray-700 m-5">
        <h5 className="mb-2 text-2xl font-bold tracking-tight text-gray-900 dark:text-white">{item.type}</h5>
        <p className="mb-3 font-normal text-gray-700 dark:text-gray-400">{item.location}</p>
        <button onClick={() => deleteResource(item.id)} className="inline-flex items-center px-3 py-2 text-sm font-medium text-center text-white bg-red-700 rounded-lg hover:bg-red-800 focus:ring-4 focus:outline-none focus:ring-red-300 dark:bg-red-600 dark:hover:bg-red-700 dark:focus:ring-red-800">
          Delete
        </button>
      </div>
    )
  };

  const deleteResource = async (resourceId) => {
    const response = await axios.delete(`/api/resources/${resourceId}`);
    if(response.status === 200) {
      setResources(resources.filter(resource => resource.id !== resourceId));
    } else {
      console.error(`Error: ${response.status}`);
    }
  };

  const getResources = async () => {
    const response = await axios.get('/api/resources');
    if(response.status === 200) {
      setResources(response.data);
    } else {
      console.error(`Error: ${response.status}`);
    }
  };

  const getAqdata = async () => {
    const response = await axios.get('/api/aqdata');
    if(response.status === 200) {
      setAqdata(response.data);
    } else {
      console.error(`Error: ${response.status}`);
    }
  };

  useEffect(() => {
    getResources();
    getAqdata();
  }, []);

  return (
    <main className="flex min-h-screen flex-col items-center justify-center p-24">
      <div className="flex flex-row justify-center flex-wrap pt-20">
        {resources == null ? <>loading</> :
          resources.map((resourceItem, index) => {
            return (
              <ResourceCard key={index} resourceItem={resourceItem} ></ResourceCard>
            );
          })}
      </div>

      <div className="flex flex-row justify-center flex-wrap pt-20">
        {aqdata == null ? <>loading</> :
          aqdata.map((aqdataItem, index) => {
            return (
              <ResourceCard key={index} resourceItem={aqdataItem} ></ResourceCard>
            );
          })}
      </div>
    </main>
  );
}
