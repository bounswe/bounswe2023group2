import { Button, TableBody, TableCell, TableColumn, TableHeader, TableRow, useDisclosure, Table } from "@nextui-org/react";
import React, { useEffect, useState } from "react";
import ActivityModal from "./ActivityModal";
import Filter from "./Filter";
import Sort from "./Sort";



export default function ActivityTable({ needFilter, resourceFilter }) {
    const [filters, setFilters] = useState({})
    const [resources, setResources] = useState([]);
    const [needs, setNeeds] = useState([]);
    const [selectedKeys, setSelectedKeys] = useState(new Set(["text"]));
    const { isOpen, onOpen, onOpenChange } = useDisclosure();
    const [activity, setActivity] = useState({});

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

            <ActivityModal isOpen={isOpen} onOpenChange={onOpenChange} activity={activity} />

            <Filter setFilters={setFilters} filters={filters} filterActivities={filterActivities} />
            <Sort needFilter={needFilter} resourceFilter={resourceFilter} setSelectedKeys={setSelectedKeys} selectedKeys={selectedKeys} filterActivities={filterActivities} />
            <Table
                selectionMode="single"
                defaultSelectedKeys={["2"]}
                aria-label="activity table"
            >
                <TableHeader>
                    <TableColumn>Type</TableColumn>
                    <TableColumn>Location</TableColumn>
                    <TableColumn>Created by</TableColumn>
                    <TableColumn>Details</TableColumn>
                </TableHeader>
                <TableBody>
                    {resourceFilter && resources && resources.map((resource, index) => (
                        <TableRow key={index} onClick={()=>{setActivity(resource); onOpen()}} >
                            <TableCell>{resource.type}</TableCell>
                            <TableCell>{resource.location}</TableCell>
                            <TableCell>{resource.created_by}</TableCell>
                            <TableCell>{resource.details.subtype ?? resource.details.tool_type}</TableCell>
                        </TableRow>
                    ))}
                    {needFilter && needs && needs.map((need, index) => (
                        <TableRow key={index} onClick={()=>{setActivity(need); onOpen()}}>
                            <TableCell>{need.type}</TableCell>
                            <TableCell>{need.location}</TableCell>
                            <TableCell>{need.created_by}</TableCell>
                            <TableCell>{need.details.subtype ?? need.details.tool_type}</TableCell>
                        </TableRow>
                    ))}
                </TableBody>
            </Table>
        </div>

    );
}