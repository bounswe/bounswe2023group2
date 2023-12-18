import { Button, TableBody, TableCell, TableColumn, TableHeader, TableRow, useDisclosure, Table } from "@nextui-org/react";
import React, { useEffect, useState } from "react";
import ActivityModal from "./ActivityModal";
import Filter from "./Filter";
import Sort from "./Sort";
import { toast } from 'react-toastify';
import { api } from "@/lib/apiUtils";
import AddActionForm from "./AddAction";
import { GrTransaction } from "react-icons/gr";


export default function ActivityTable({ chosenActivityType, labels }) {
    const [filters, setFilters] = useState({})
    const [resources, setResources] = useState([]);
    const [needs, setNeeds] = useState([]);
    const [events, setEvents] = useState([]);
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
        const response = await fetch('/api/resource/get', { method: 'GET', headers: { "Content-Type": "application/json" } });
        let res = await response.json();
        if (response.ok) {

            setResources(res.resources)
        } else {
            toast.error(labels.feedback.failure);
        }
    }
    const getNeeds = async () => {
        const response = await fetch('/api/need/get', { method: 'GET', headers: { "Content-Type": "application/json" } });
        let res = await response.json();
        if (response.ok) {
            setNeeds(res.needs)
        } else {
            toast.error(labels.feedback.failure);
        }
    }
    const getEvents = async () => {
        const response = await fetch('/api/event/get', { method: 'GET', headers: { "Content-Type": "application/json" } });
        let res = await response.json();
        console.log(res)
        if (response.ok) {
            setEvents(res.events);
        } else {
            toast.error(labels.feedback.failure);
        }
    }
    useEffect(() => {
        getResources();
        getNeeds();
        getEvents();
    }, [])

    const filterActivities = async () => {
        let response
        if (chosenActivityType === "needs") {
            let my_filter = new URLSearchParams(filters).toString()

            response = await api.get(`/api/needs/?${my_filter}`, { headers: { "Content-Type": "application/json" } });
            let res = response.data;
            if (response.status === 200) {
                setNeeds(res.needs)
            } else {
                toast.error(labels.feedback.failure);
            }
        } else if (chosenActivityType === "resources") {
            let my_filter = new URLSearchParams(filters).toString()

            response = await api.get(`/api/resources/?${my_filter}`, { headers: { "Content-Type": "application/json" } });
            let res = response.data;

            setResources(res.resources)
            if (response.status === 200) {
                setResources(res.resources)
                console.log(resources)
            } else {
                toast.error(labels.feedback.failure);
            }
        }
    }
    return (

        <div class="w-full">
            <div className=' '>
                <Filter setFilters={setFilters} filters={filters} filterActivities={filterActivities} labels={labels} />
                <Sort chosenActivityType={chosenActivityType} filterActivities={filterActivities} setFilters={setFilters} filters={filters} labels={labels} />
            </div>

            <ActivityModal isOpen={isOpen} onOpenChange={onOpenChange} activity={activity} activityType={chosenActivityType} />

            <Table
                isHeaderSticky
                selectionMode="single"
                aria-label="activity table"
                className='flex w-full overflow-x-auto shadow-md sm:rounded max-h-[300px] overflow-scroll'
            >
                <TableHeader>
                    <TableColumn>{labels.activity_table.type}</TableColumn>
                    <TableColumn>{labels.activity_table.location}</TableColumn>
                    <TableColumn>{labels.activity_table.created_by}</TableColumn>
                    <TableColumn>{labels.activity_table.created_at}</TableColumn>
                    <TableColumn>{labels.activity_table.description}</TableColumn>
                    <TableColumn>{labels.activity_table.take_action}</TableColumn>
                </TableHeader>
                <TableBody>
                    {chosenActivityType === "resources" && resources && resources.map((resource, index) => (
                        <TableRow key={index} >
                            <TableCell onClick={() => { setActivity(resource); onOpen() }} >{resource.type}</TableCell>
                            <TableCell onClick={() => { setActivity(resource); onOpen() }} >{resource.x} : {resource.y}</TableCell>
                            <TableCell onClick={() => { setActivity(resource); onOpen() }} >{resource.created_by}</TableCell>
                            <TableCell onClick={() => { setActivity(resource); onOpen() }} >{resource.created_at}</TableCell>
                            <TableCell onClick={() => { setActivity(resource); onOpen() }} >{resource.description}</TableCell>
                            <TableCell >
                                </TableCell>
                        </TableRow>
                    ))}
                    {chosenActivityType === "needs" && needs && needs.map((need, index) => (
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
                    {chosenActivityType === "events" && events && events.map((event, index) => (
                        <TableRow key={index} >
                            <TableCell onClick={() => { setActivity(event); onOpen() }} >{event.event_type}</TableCell>
                            <TableCell onClick={() => { setActivity(event); onOpen() }}>{event.x} : {event.y}</TableCell>
                            <TableCell onClick={() => { setActivity(event); onOpen() }}>{event.created_by_user}</TableCell>
                            <TableCell onClick={() => { setActivity(event); onOpen() }}>{event.created_time}</TableCell>
                            <TableCell onClick={() => { setActivity(event); onOpen() }}>{event.short_description}</TableCell>
                            <TableCell onClick={() => { setActivity(event); }} > </TableCell>
                        </TableRow>
                    ))}
                </TableBody>
            </Table>
            <AddActionForm onOpenChange={onOpenChangeNeedModal} isOpen={isNeedModalOpen} table_need={activity} need_type={activity.type} labels={labels}/>
        </div>

    );
}