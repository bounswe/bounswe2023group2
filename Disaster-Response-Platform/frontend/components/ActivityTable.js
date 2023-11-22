import React, { useEffect, useState } from "react";
import Filter from "./Filter";
import Sort from "./Sort";



export default function ActivityTable({ needFilter, resourceFilter }) {
    const [filters, setFilters] = useState({})
    const [resources, setResources] = useState([]);
    const [needs, setNeeds] = useState([]);
    const [selectedKeys, setSelectedKeys] = useState(new Set(["text"]));
  
    const getResources = async () => {
        const response = await fetch('/api/resource', { method: 'GET', headers: { "Content-Type": "application/json" } });
        let res = await response.json();
        if (response.ok) {
    
          setResources(res.resources)
        } else {
          toast.error("An unexpected error occurred while saving, please try again")
        }
      }
      const getNeeds = async () => {
        const response = await fetch('/api/need/get', { method: 'GET', headers: { "Content-Type": "application/json" } });
        let res = await response.json();
        if (response.ok) {
    
          setNeeds(res.needs)
        } else {
    
          toast.error("An unexpected error occurred while saving, please try again")
        }
      }
    useEffect(() => {
        getResources();
        getNeeds();
    
      }, [])
    
    const filterActivities = async () => {
        let response
        if (needFilter) {
            response = await fetch('/api/need/filter', { method: 'POST', headers: { "Content-Type": "application/json" }, body: JSON.stringify(filters) });
            let res = await response.json();
            if (response.ok) {
                setNeeds(res.needs)
            } else {
                // unknown error
                toast.error("An unexpected error occurred while saving, please try again")
            }
        }
        if (resourceFilter) {
            response = await fetch('/api/resource/filter', { method: 'POST', headers: { "Content-Type": "application/json" }, body: JSON.stringify(filters) });
            let res = await response.json();
            if (response.ok) {
                setResources(res.resources)
            } else {
                // unknown error
                toast.error("An unexpected error occurred while saving, please try again")
            }
        }
    }
    return (
        
        <div class="w-full overflow-x-auto shadow-md sm:rounded my-10">
            <Filter setFilters={setFilters} filters={filters} filterActivities={filterActivities} />
            <Sort needFilter={needFilter}  resourceFilter={resourceFilter} setSelectedKeys={setSelectedKeys} selectedKeys={selectedKeys} filterActivities={filterActivities} />
            <table class="w-full text-sm text-left text-gray-500 dark:text-gray-400">
                <thead class="text-xs text-gray-700 uppercase bg-white dark:bg-slate-400 dark:text-black">
                    <tr>
                        <th scope="col" class="px-6 py-5">
                            Type
                        </th>
                        <th scope="col" class="px-6 py-3">
                            Location
                            <a href="#"><svg class="w-3 h-3 ml-1.5" aria-hidden="true" xmlns="http://www.w3.org/2000/svg" fill="currentColor" viewBox="0 0 24 24">
                                <path d="M8.574 11.024h6.852a2.075 2.075 0 0 0 1.847-1.086 1.9 1.9 0 0 0-.11-1.986L13.736 2.9a2.122 2.122 0 0 0-3.472 0L6.837 7.952a1.9 1.9 0 0 0-.11 1.986 2.074 2.074 0 0 0 1.847 1.086Zm6.852 1.952H8.574a2.072 2.072 0 0 0-1.847 1.087 1.9 1.9 0 0 0 .11 1.985l3.426 5.05a2.123 2.123 0 0 0 3.472 0l3.427-5.05a1.9 1.9 0 0 0 .11-1.985 2.074 2.074 0 0 0-1.846-1.087Z" />
                            </svg></a>
                        </th>
                        <th scope="col" class="px-6 py-3">
                            Created by
                            <a href="#"><svg class="w-3 h-3 ml-1.5" aria-hidden="true" xmlns="http://www.w3.org/2000/svg" fill="currentColor" viewBox="0 0 24 24">
                                <path d="M8.574 11.024h6.852a2.075 2.075 0 0 0 1.847-1.086 1.9 1.9 0 0 0-.11-1.986L13.736 2.9a2.122 2.122 0 0 0-3.472 0L6.837 7.952a1.9 1.9 0 0 0-.11 1.986 2.074 2.074 0 0 0 1.847 1.086Zm6.852 1.952H8.574a2.072 2.072 0 0 0-1.847 1.087 1.9 1.9 0 0 0 .11 1.985l3.426 5.05a2.123 2.123 0 0 0 3.472 0l3.427-5.05a1.9 1.9 0 0 0 .11-1.985 2.074 2.074 0 0 0-1.846-1.087Z" />
                            </svg></a>
                        </th>
                        <th scope="col" class="px-6 py-3">
                            Details
                            <a href="#"><svg class="w-3 h-3 ml-1.5" aria-hidden="true" xmlns="http://www.w3.org/2000/svg" fill="currentColor" viewBox="0 0 24 24">
                                <path d="M8.574 11.024h6.852a2.075 2.075 0 0 0 1.847-1.086 1.9 1.9 0 0 0-.11-1.986L13.736 2.9a2.122 2.122 0 0 0-3.472 0L6.837 7.952a1.9 1.9 0 0 0-.11 1.986 2.074 2.074 0 0 0 1.847 1.086Zm6.852 1.952H8.574a2.072 2.072 0 0 0-1.847 1.087 1.9 1.9 0 0 0 .11 1.985l3.426 5.05a2.123 2.123 0 0 0 3.472 0l3.427-5.05a1.9 1.9 0 0 0 .11-1.985 2.074 2.074 0 0 0-1.846-1.087Z" />
                            </svg></a>
                        </th>

                    </tr>
                </thead>
                <tbody>
                    {resourceFilter && resources && resources.map((resource) => (
                        <>
                            <tr class="bg-white border-b dark:bg-gray-100 dark:border-gray-500">
                                <th scope="row" class="px-6 py-4 font-medium text-gray-900 whitespace-nowrap dark:text-black">
                                    {resource.type}
                                </th>
                                <td class="px-6 py-4">
                                    {resource.condition}
                                </td>
                                <td class="px-6 py-4">
                                    {resource.created_by}
                                </td>
                                <td class="px-6 py-4">
                                    {resource.details.subtype ?? resource.details.tool_type}
                                </td>
                            </tr>
                        </>
                    ))}
                    {needFilter && needs && needs.map((need) => (
                        <>
                            <tr class="bg-white border-b dark:bg-gray-100 dark:border-gray-500">
                                <th scope="row" class="px-6 py-4 font-medium text-gray-900 whitespace-nowrap dark:text-black">
                                    {need.type}
                                </th>
                                <td class="px-6 py-4">
                                    {need.condition}
                                </td>
                                <td class="px-6 py-4">
                                    {need.created_by}
                                </td>
                                <td class="px-6 py-4">
                                    {need.details.subtype ?? need.details.tool_type}
                                </td>
                            </tr>
                        </>
                    ))}

                </tbody>
            </table>
        </div>

    );
}