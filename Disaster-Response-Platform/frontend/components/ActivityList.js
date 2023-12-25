import { Button, Card, CardBody, CardFooter, CardHeader, Divider, Link, Tab, Tabs, useDisclosure } from '@nextui-org/react';
import React, { useEffect, useState } from 'react';
import ListItem from './ListItem';
import { toast } from 'react-toastify';
import Filter from './Filter';
import Sort from './Sort';
import ActivityModal from './ActivityModal';
import { api } from '@/lib/apiUtils';
import Search from './Search';

const ActivityList = ({ labels, userFilter }) => {
    const [activities, setActivities] = useState([]); // [{_id: "loading"}
    const [chosenActivityType, setChosenActivityType] = useState("resource");
    const [filters, setFilters] = useState({})
    const [search, setSearch] = useState("");
    const [resources, setResources] = useState([{ _id: "loading" }]);
    const [needs, setNeeds] = useState([{ _id: "loading" }]);
    const [events, setEvents] = useState([{ _id: "loading" }]);
    const { isOpen, onOpen, onOpenChange } = useDisclosure();
    const [activity, setActivity] = useState({});

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
        if (chosenActivityType === "need") {
            let my_filter = new URLSearchParams(filters).toString()

            response = await api.get(`/api/needs/?${my_filter}`, { headers: { "Content-Type": "application/json" } });
            let res = response.data;
            if (response.status === 200) {
                setNeeds(res.needs)
            } else {
                toast.error(labels.feedback.failure);
            }
        } else if (chosenActivityType === "resource") {
            let my_filter = new URLSearchParams(filters).toString()

            response = await api.get(`/api/resources/?${my_filter}`, { headers: { "Content-Type": "application/json" } });
            let res = response.data;

            setResources(res.resources)
            if (response.status === 200) {
                setResources(res.resources)
            } else {
                toast.error(labels.feedback.failure);
            }
        }
    }

    return <>
        <div className="text-center  ">
            <Tabs
                selectedKey={chosenActivityType}
                onSelectionChange={setChosenActivityType}
                size="lg"
                radius='full'
                classNames={{
                    cursor: `bg-${chosenActivityType}`,
                    tab: `basis-2/3`,
                }}
            >
                <Tab key="need" titleValue={labels.activities.needs} title={labels.activities.needs} />
                <Tab key="resource" titleValue={labels.activities.resources} title={labels.activities.resources} />
                <Tab key="event" titleValue={labels.activities.events} title={labels.activities.events} />
            </Tabs>
        </div>
        <div class="w-full">



            <ActivityModal isOpen={isOpen} onOpenChange={onOpenChange} activity={activity} activityType={chosenActivityType} />

        </div>
        <div className=' max-h-[700px] bg-slate-500 overflow-y-auto w-4/5 p-2'>
            <div className="text-end ">
                <Search search={search} setSearch={setSearch} chosenActivityType={chosenActivityType} activities={activities} setActivities={setActivities} />
                <Filter setFilters={setFilters} filters={filters} filterActivities={filterActivities} labels={labels} />
                <Sort chosenActivityType={chosenActivityType} filterActivities={filterActivities} setFilters={setFilters} filters={filters} labels={labels} />
            </div>
            {(search !== "" )&& activities.map((activity, index) => (
                <ListItem activityType={'resource'} activity={activity} />
            ))}

            {chosenActivityType === "resource" && search=== "" && resources.map((resource, index) => (
                <ListItem activityType={'resource'} activity={resource} />
            ))}
            {chosenActivityType === "need" && needs.map((need, index) => (
                <ListItem activityType={'need'} activity={need} />
            ))}
            {chosenActivityType === "event" && events.map((event, index) => (
                <ListItem activityType={'event'} activity={event} />
            ))}
        </div>
    </>
}


export default ActivityList;
