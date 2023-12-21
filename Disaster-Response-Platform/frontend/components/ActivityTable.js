import { Button, TableBody, TableCell, TableColumn, TableHeader, TableRow, useDisclosure, Table, Tabs, Tab } from "@nextui-org/react";
import React, { useEffect, useState } from "react";
import ActivityModal from "./ActivityModal";
import Filter from "./Filter";
import Sort from "./Sort";
import { toast } from 'react-toastify';
import { api } from "@/lib/apiUtils";
import AddActionForm from "./AddAction";
import { GrTransaction } from "react-icons/gr";


export default function ActivityTable({ labels, userFilter }) {
    const [chosenActivityType, setChosenActivityType] = useState("resources");
    const [filters, setFilters] = useState({})
    const [resources, setResources] = useState([{_id: "loading"}]);
    const [needs, setNeeds] = useState([{_id: "loading"}]);
    const [events, setEvents] = useState([{_id: "loading"}]);
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
            if (userFilter === undefined) {
                setResources(res.resources);
            } else {
                setResources(res.resources.filter(entry => entry.created_by === userFilter));
            }
        } else {
            toast.error(labels.feedback.failure);
        }
    }
    const getNeeds = async () => {
        const response = await fetch('/api/need/get', { method: 'GET', headers: { "Content-Type": "application/json" } });
        let res = await response.json();
        if (response.ok) {
            if (userFilter === undefined) {
                setNeeds(res.needs);
            } else {
                setNeeds(res.needs.filter(entry => entry.created_by === userFilter));
            }
        } else {
            toast.error(labels.feedback.failure);
        }
    }
    const getEvents = async () => {
        const response = await fetch('/api/event/get', { method: 'GET', headers: { "Content-Type": "application/json" } });
        let res = await response.json();
        console.log(res)
        if (response.ok) {
            if (userFilter === undefined) {
                setEvents(res.events);
            } else {
                setEvents(res.events.filter(entry => entry.created_by_user === userFilter));
            }
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

    function getRows() {
        switch (chosenActivityType) {
            case "resources": return resources;
            case "needs": return needs;
            case "events": return events;
            default: return [];
        }
    }

    const columns = {
        "resources": ["type", "location", "created_by", "created_at", "description"],
        "needs": ["type", "location", "created_by", "created_at", "occur_at", "description", "recurrence_rate", "take_action"],
        "events": ["event_type", "location", "created_by_user", "event_time", "short_description", "is_active"]
    }

    function getColumns() {
        let result = [];
        for (let column of columns[chosenActivityType]) {
            result.push({"key": column, "label": labels.activity_table[column]});
        }
        return result;
    }

    return (
        <div>
            <div className="text-center">
                <Tabs
                    selectedKey={chosenActivityType}
                    onSelectionChange={setChosenActivityType}
                    size="lg"
                    color="primary"
                    variant="underlined"
                    classNames={{tab: "py-8"}}
                >
                    <Tab key="needs" titleValue={labels.activities.needs} title={(
                        <div className="bg-need dark:bg-need-dark px-2 py-1.5 rounded-xl text-black">
                            {labels.activities.needs}
                        </div>
                    )}/>
                    <Tab key="resources" titleValue={labels.activities.resources} title={(
                        <div className="bg-resource dark:bg-resource-dark px-2 py-1.5 rounded-xl text-black">
                            {labels.activities.resources}
                        </div>
                    )}/>
                    <Tab key="events" titleValue={labels.activities.events} title={(
                        <div className="bg-event dark:bg-event-dark px-2 py-1.5 rounded-xl text-black">
                            {labels.activities.events}
                        </div>
                    )}/>
                </Tabs>
            </div>
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
                    className='flex w-full overflow-x-auto shadow-md sm:rounded max-h-[300px]'
                >
                    <TableHeader columns={getColumns()} emptyContent={labels.activity_table.no_content}>
                        {(column) => <TableColumn key={column.key}>{column.label}</TableColumn>}
                    </TableHeader>
                    <TableBody items={getRows()}>
                        {item => (
                            <TableRow key={item._id}>
                                {(columnKey) => {
                                    if (item._id === "loading") {
                                        return <TableCell> {labels.UI.loading} </TableCell>;
                                    }
                                    let content = "";
                                    switch (columnKey) {
                                    case "location":
                                        content = `${item.x} : ${item.y}`;
                                        break;
                                    case "take_action":
                                        content = (
                                            <span className="text-lg text-default-400 cursor-pointer active:opacity-50">
                                                <GrTransaction />
                                            </span>
                                        );
                                        break;
                                    case "is_active":
                                        content = item.is_active ? "✔" : "❌";
                                        break;
                                    default:
                                        content = item[columnKey];
                                    }
                                    return (
                                        <TableCell onClick={() => {
                                            setActivity(item);
                                            if (columnKey === "take_action") {
                                                onOpenNeedModal();
                                            } else {
                                                onOpen();
                                            }}} >
                                            {content}
                                        </TableCell>
                                    );
                                }}
                            </TableRow>
                        )}
                    </TableBody>
                </Table>
                <AddActionForm onOpenChange={onOpenChangeNeedModal} isOpen={isNeedModalOpen} table_need={activity} need_type={activity.type} labels={labels}/>
            </div>
        </div>
    );
}