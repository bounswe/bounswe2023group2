import { Button, TableBody, TableCell, TableColumn, TableHeader, TableRow, useDisclosure, Table } from "@nextui-org/react";
import React, { useEffect, useState } from "react";
import ActivityModal from "./ActivityModal";
import Filter from "./Filter";
import Sort from "./Sort";
import { toast } from 'react-toastify';
import { api } from "@/lib/apiUtils";
import AddActionForm from "./AddAction";
import { GrTransaction } from "react-icons/gr";


export default function ActivityTable({ needFilter, resourceFilter }) {
    const [filters, setFilters] = useState({})
    const [resources, setResources] = useState([]);
    const [needs, setNeeds] = useState([]);
    const [selectedKeys, setSelectedKeys] = useState(new Set(["text"]));
    const { isOpen, onOpen, onOpenChange } = useDisclosure();
    const [activity, setActivity] = useState({});
    const [order, setOrder] = useState({});
    const {
        isOpen: isNeedModalOpen,
        onOpen: onOpenNeedModal,
        onOpenChange: onOpenChangeNeedModal,
    } = useDisclosure()
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

            response = await api.get(`/api/needs/?${my_filter}`, { headers: { "Content-Type": "application/json" } });
            let res = response.data;
            if (response.status === 200) {
                setNeeds(res.needs)
            } else {
                // unknown error
                toast.error("An unexpected error occurred while saving, please try again")
            }
        }
        if (resourceFilter) {
            let my_filter = new URLSearchParams(filters).toString()

            response = await api.get(`/api/resources/?${my_filter}`, { headers: { "Content-Type": "application/json" } });
            let res = response.data;

            setResources(res.resources)
            if (response.status === 200) {
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
                <Sort needFilter={needFilter} resourceFilter={resourceFilter} filterActivities={filterActivities} setFilters={setFilters} filters={filters} />
            </div>

            <ActivityModal isOpen={isOpen} onOpenChange={onOpenChange} activity={activity} activityType={resourceFilter ? "resources" : "needs"} />

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
                    <TableColumn>Take Action</TableColumn>

                </TableHeader>
                <TableBody>
                    {resourceFilter && resources && resources.map((resource, index) => (
                        <TableRow key={index} >
                            <TableCell onClick={() => { setActivity(resource); onOpen() }} >{resource.type}</TableCell>
                            <TableCell onClick={() => { setActivity(resource); onOpen() }} >{resource.x} : {resource.y}</TableCell>
                            <TableCell onClick={() => { setActivity(resource); onOpen() }} >{resource.created_by}</TableCell>
                            <TableCell onClick={() => { setActivity(resource); onOpen() }} >{resource.created_at}</TableCell>
                            <TableCell onClick={() => { setActivity(resource); onOpen() }}  >{resource.description}</TableCell>
                            <TableCell onClick={() => { onOpenNeedModal() }} >

                                <span className="text-lg text-default-400 cursor-pointer active:opacity-50">
                                    <GrTransaction />
                                </span>

                            </TableCell>


                        </TableRow>
                    ))}
                    {needFilter && needs && needs.map((need, index) => (
                        <TableRow key={index} >
                            <TableCell onClick={() => { setActivity(need); onOpen() }} >{need.type}</TableCell>
                            <TableCell onClick={() => { setActivity(need); onOpen() }}>{need.x} : {need.y}</TableCell>
                            <TableCell onClick={() => { setActivity(need); onOpen() }}>{need.created_by}</TableCell>
                            <TableCell onClick={() => { setActivity(need); onOpen() }}>{need.created_at}</TableCell>
                            <TableCell onClick={() => { setActivity(need); onOpen() }}>{need.description}</TableCell>
                            <TableCell onClick={() => { setActivity(need); onOpenNeedModal() }} >

                                <span className="text-lg text-default-400 cursor-pointer active:opacity-50">
                                    <GrTransaction />
                                </span>

                            </TableCell>
                        </TableRow>
                    ))}
                </TableBody>
            </Table>
            <Button onPress={onOpenNeedModal}>Aksiyon al</Button>
            <AddActionForm onOpenChange={onOpenChangeNeedModal} isOpen={isNeedModalOpen} table_need={activity} need_type={activity.type} />
        </div>

    );
}