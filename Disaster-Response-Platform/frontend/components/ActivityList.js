import { Button, Card, CardBody, CardFooter, CardHeader, Divider, Link, Tab, Tabs, useDisclosure } from '@nextui-org/react';
import React, { useEffect, useState } from 'react';
import ListItem from './ListItem';
import { toast } from 'react-toastify';
import Filter from './Filter';
import Sort from './Sort';
import ActivityModal from './ActivityModal';
import { api } from '@/lib/apiUtils';
import Search from './Search';
import AddActionForm from './AddAction';
import get from '@/pages/api/resource/get';
import { set } from 'react-hook-form';
import emergencyService from '@/services/emergencyService';
import actionService from '@/services/actionService';

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
    const [emergencies, setEmergencies] = useState([{ _id: "loading" }]);
    const [actions, setActions] = useState([{ _id: "loading" }]);
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
    const getEmergencies = async (data) => {
        const response = await emergencyService.getAll();
        console.log(response)
        if (response.status === 200) {
            // successful
            setEmergencies(response.payload.emergencies)
            toast.success('Success')
            // Usage!
        } else {
            // unknown error
            toast.error('Error')
        }
    }
    const getActions = async (data) => {
        const response = await actionService.getAll();
        console.log(response)
        if (response.status === 200) {
            // successful
            setActions(response.payload.actions)
            toast.success('Success')
            // Usage!
        }
        else {
            // unknown error
            toast.error('Error')
        }
    }
    useEffect(() => {
        getResources();
        getNeeds();
        getEvents();
        getEmergencies();
        getActions();
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
        <div className="text-center flex flex-row justify-between max-h-[700px] overflow-y-auto w-4/5 p-2">
            <Search search={search} setSearch={setSearch} chosenActivityType={chosenActivityType} activities={activities} setActivities={setActivities} />
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
                <Tab key="emergency" titleValue={'Emergency'} title={'Emergency'} />
                <Tab key="action" titleValue={'Actions'} title={'Actions'} />
            </Tabs>
            <p className=''>

                <Filter setFilters={setFilters} filters={filters} filterActivities={filterActivities} labels={labels} />
                <Sort chosenActivityType={chosenActivityType} filterActivities={filterActivities} setFilters={setFilters} filters={filters} labels={labels} />
            </p>
        </div>
        <div class="w-full">



            <ActivityModal isOpen={isOpen} onOpenChange={onOpenChange} activity={activity} activityType={chosenActivityType} labels={labels} />

        </div>
        <div className=' max-h-[700px] bg-slate-100 overflow-y-auto w-4/5 p-2 round-sm'>
            <div className="text-end ">

            </div>

            {(search !== "" ) && activities.map((activity, index) => (
                <ListItem activityType={'resource'} activity={activity} labels={labels} />
            ))}

            {chosenActivityType === "resource" && search === "" && resources.map((resource, index) => (
                <ListItem activityType={'resource'} activity={resource} labels={labels} />

            ))}
            {chosenActivityType === "need" && needs.map((need, index) => (
                <ListItem activityType={'need'} activity={need} labels={labels} />
            ))}
            {chosenActivityType === "event" && events.map((event, index) => (
                <ListItem activityType={'event'} activity={event} labels={labels} />
            ))}
            {chosenActivityType === "emergency" && emergencies.map((emergency, index) => (
                <ListItem activityType={'emergency'} activity={emergency} />
            ))}
             {chosenActivityType === "action" && actions.map((action, index) => (
                <ListItem activityType={'action'} activity={action} />
            ))}
        </div>

    </>
}


export default ActivityList;
