import { Button, TableBody, TableCell, TableColumn, TableHeader, TableRow, useDisclosure, Table } from "@nextui-org/react";
import React, { useEffect, useState } from "react";
import ActivityModal from "./ActivityModal";
import Filter from "./Filter";
import Sort from "./Sort";
import { toast } from 'react-toastify';
import { api } from "@/lib/apiUtils";

export default function ActivityTable({ needFilter, resourceFilter }) {
    const [filters, setFilters] = useState({})
    const [resources, setResources] = useState([]);
    const [needs, setNeeds] = useState([]);
    const [selectedKeys, setSelectedKeys] = useState(new Set(["text"]));
    const { isOpen, onOpen, onOpenChange } = useDisclosure();
    const [activity, setActivity] = useState({});
    const [order, setOrder] = useState({});
    const getResources = async () => {
        const response = await fetch('/api/resource', { method: 'GET', headers: { "Content-Type": "application/json" } });
        let res = await response.json();
        if (response.ok) {

            setResources(res.resources)
        } else {
            // toast.error("An unexpected error occurred while saving, please try again")
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
            let my_filter = new URLSearchParams(filters).toString()
           
            response = await api.get(`/api/needs/?${my_filter}`, {headers: { "Content-Type": "application/json" } });
            let res =  response.data;
            if (response.status === 200) {
                setNeeds(res.needs)
            } else {
                // unknown error
                toast.error("An unexpected error occurred while saving, please try again")
            }
        }
        if (resourceFilter) {
            let my_filter = new URLSearchParams(filters).toString()
           
            response = await api.get(`/api/resources/?${my_filter}`, {headers: { "Content-Type": "application/json" } });
            let res =  response.data;
        
            setResources(res.resources)
            if (response.status === 200){
                setResources(res.resources)
                console.log(resources)
            } else {
                // unknown error
                toast.error("An unexpected error occurred while saving, please try again")
            }
        }
    }
    return (

        <div class="w-full">
            <div className=' '>
            <Filter setFilters={setFilters} filters={filters} filterActivities={filterActivities} />
            <Sort needFilter={needFilter} resourceFilter={resourceFilter} filterActivities={filterActivities}  setFilters={setFilters} filters={filters} />
            </div>
                
            <ActivityModal isOpen={isOpen} onOpenChange={onOpenChange} activity={activity} activityType= {resourceFilter ? "resources" :"needs"} />

            <Table
            isHeaderSticky
                selectionMode="single"
                aria-label="activity table"
                className='flex w-full overflow-x-auto shadow-md sm:rounded max-h-[300px] overflow-scroll'
            >
                <TableHeader>
                    <TableColumn>Type</TableColumn>
                    <TableColumn>Location</TableColumn>
                    <TableColumn>Created by</TableColumn>
                    <TableColumn>Created At</TableColumn>
                    <TableColumn>Description</TableColumn>

                </TableHeader>
                <TableBody>
                    {resourceFilter && resources && resources.map((resource, index) => (
                        <TableRow key={index} onClick={()=>{setActivity(resource); onOpen()}} >
                            <TableCell>{resource.type}</TableCell>
                            <TableCell>{resource.x} : {resource.y}</TableCell>
                            <TableCell>{resource.created_by}</TableCell>
                            <TableCell>{resource.created_at}</TableCell>
                            <TableCell>{resource.description}</TableCell>


                        </TableRow>
                    ))}
                    {needFilter && needs && needs.map((need, index) => (
                        <TableRow key={index} onClick={()=>{setActivity(need); onOpen()}}>
                            <TableCell>{need.type}</TableCell>
                            <TableCell>{need.x} : {need.y}</TableCell>
                            <TableCell>{need.created_by}</TableCell>
                            <TableCell>{need.created_at}</TableCell>
                            <TableCell>{need.description}</TableCell>
                           
                        </TableRow>
                    ))}
                </TableBody>
            </Table>
        </div>

    );
}