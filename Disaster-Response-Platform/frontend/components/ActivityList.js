import { Button, Card, CardBody, CardFooter, CardHeader, Divider, Link, Tab, Tabs, useDisclosure } from '@nextui-org/react';
import React, { useEffect, useState } from 'react';
import ListItem from './ListItem';
import { toast } from 'react-toastify';
import Filter from './Filter';
import Sort from './Sort';
import ActivityModal from './ActivityModal';
import { api } from '@/lib/apiUtils';


const list =[
 {
   "event_type": "Debris",
   "event_time": "2023-12-11 21:58:57.818000",
   "end_time": "2023-12-11 21:58:57.818000",
   "is_active": true,
   "max_distance_x": 1,
   "max_distance_y": 1,
   "created_time": "2023-12-11 21:58:57.818000",
   "created_by_user": "admin5",
   "last_confirmed_time": null,
   "confirmed_by_user": null,
   "upvote": 0,
   "downvote": 1,
   "short_description": "bu bir deneme olayıdır",
   "note": "bu bir deneme notudur",
   "_id": "65778650f80ee07b3ef351cf"
},
{
   "event_type": "Debris",
   "event_time": "2023-12-11 21:58:57.818000",
   "end_time": "2023-12-11 21:58:57.818000",
   "is_active": true,
   "max_distance_x": 1,
   "max_distance_y": 1,
   "created_time": "2023-12-11 21:58:57.818000",
   "created_by_user": "admin5",
   "last_confirmed_time": null,
   "confirmed_by_user": null,
   "upvote": 0,
   "downvote": 2,
   "short_description": "bu bir deneme olayıdır",
   "note": "bu bir deneme notudur",
   "_id": "65778679f80ee07b3ef351d0"
},
{
   "event_type": "Debris",
   "event_time": "2023-12-11 21:58:57.818000",
   "end_time": "2023-12-11 21:58:57.818000",
   "is_active": true,
   "max_distance_x": 1,
   "max_distance_y": 1,
   "created_time": "2023-12-11 21:58:57.818000",
   "created_by_user": "admin5",
   "last_confirmed_time": null,
   "confirmed_by_user": null,
   "upvote": 1,
   "downvote": 0,
   "short_description": "bu bir deneme olayıdır3",
   "note": "bu bir deneme notudur3",
   "_id": "657786d4f80ee07b3ef351d1"
},
{
   "event_type": "Debris",
   "event_time": "2023-12-11 21:58:57.818000",
   "end_time": null,
   "is_active": true,
   "max_distance_x": 1,
   "max_distance_y": 1,
   "created_time": "2023-12-11 21:58:57.818000",
   "created_by_user": "admin5",
   "last_confirmed_time": null,
   "confirmed_by_user": null,
   "upvote": 0,
   "downvote": 1,
   "short_description": "bu bir deneme olayıdır4",
   "note": "bu bir deneme notudur4",
   "_id": "65778d99f80ee07b3ef351d4"
},
{
   "event_type": "Debris",
   "event_time": "2023-12-11 21:58:57.818000",
   "end_time": null,
   "is_active": true,
   "max_distance_x": null,
   "max_distance_y": null,
   "created_time": "2023-12-11 21:58:57.818000",
   "created_by_user": "admin5",
   "last_confirmed_time": null,
   "confirmed_by_user": null,
   "upvote": 0,
   "downvote": 0,
   "short_description": "bu bir max distance'ı olmayan bir deneme olayıdır",
   "note": "bu bir deneme notudur5",
   "_id": "65779261f80ee07b3ef351d5"
},
{
   "event_type": "Debris",
   "event_time": "2023-12-11 21:58:57.818000",
   "end_time": null,
   "is_active": true,
   "max_distance_x": null,
   "max_distance_y": null,
   "created_time": "2023-12-11 21:58:57.818000",
   "created_by_user": "admin5",
   "last_confirmed_time": null,
   "confirmed_by_user": null,
   "upvote": 0,
   "downvote": 0,
   "short_description": "bu bir max distance'ı olmayan bir deneme olayıdır",
   "note": "bu bir deneme notudur6",
   "_id": "65779689f80ee07b3ef351d6"
},
{
   "event_type": "Debris",
   "event_time": "2023-12-11 21:58:57.818000",
   "end_time": null,
   "is_active": false,
   "max_distance_x": null,
   "max_distance_y": null,
   "created_time": "2023-12-11 21:58:57.818000",
   "created_by_user": "admin5",
   "last_confirmed_time": null,
   "confirmed_by_user": null,
   "upvote": 0,
   "downvote": 0,
   "short_description": "bu bir max distance'ı olmayan bir deneme olayıdır",
   "note": "bu bir deneme notudur7",
   "_id": "657796a5f80ee07b3ef351d7"
},
{
   "event_type": "Debris",
   "event_time": "2023-12-11 23:45:00",
   "end_time": "2023-12-11 23:24:31.690000",
   "is_active": true,
   "max_distance_x": 0,
   "max_distance_y": 0,
   "created_time": "2023-12-11 23:24:31.690000",
   "created_by_user": "admin5",
   "last_confirmed_time": "2023-12-11 23:24:31.690000",
   "confirmed_by_user": "string",
   "upvote": 0,
   "downvote": 0,
   "short_description": "event time denemesi",
   "note": "string",
   "_id": "65779b54f80ee07b3ef351d8"
},
{
   "event_type": "Help-Arrived",
   "event_time": "2323-02-23 23:23:00",
   "end_time": null,
   "is_active": false,
   "max_distance_x": 0.0011922768154812803,
   "max_distance_y": 0.008998227349212205,
   "created_time": "2023-12-12 00:12:59.782000",
   "created_by_user": "admin5",
   "last_confirmed_time": null,
   "confirmed_by_user": null,
   "upvote": 0,
   "downvote": 0,
   "short_description": "Bu bir deneme olayıdır...\nİstanbul'da I gibi gözükmesi lazım",
   "note": "",
   "_id": "6577a58df80ee07b3ef351d9"
},
{
   "event_type": "Help-Arrived",
   "event_time": "2023-12-06 15:51:00",
   "end_time": null,
   "is_active": true,
   "max_distance_x": 1,
   "max_distance_y": 4,
   "created_time": "2023-12-15 21:41:01",
   "created_by_user": "egecans",
   "last_confirmed_time": null,
   "confirmed_by_user": null,
   "upvote": 0,
   "downvote": 0,
   "short_description": "corba dagitimiz",
   "note": "Mansur Lavas Corba Dagitiyor",
   "_id": "657865816615139557c33d78"
},
{
   "event_type": "Debris",
   "event_time": "2023-12-14 02:00:00",
   "end_time": null,
   "is_active": true,
   "max_distance_x": 25,
   "max_distance_y": 32,
   "created_time": "2023-12-15 22:33:49",
   "created_by_user": "halil",
   "last_confirmed_time": null,
   "confirmed_by_user": null,
   "upvote": 0,
   "downvote": 0,
   "short_description": "not",
   "note": "ek not",
   "_id": "657caa1ee5e22bb2350cdc19"
},
{
   "event_type": "Disaster",
   "event_time": "2023-12-19 00:20:00",
   "end_time": null,
   "is_active": true,
   "x": null,
   "y": null,
   "max_distance_x": 10,
   "max_distance_y": 10,
   "created_time": "2023-12-19 00:21:16",
   "created_by_user": "hasan1234",
   "last_confirmed_time": null,
   "confirmed_by_user": null,
   "upvote": 0,
   "downvote": 0,
   "short_description": "hellooo",
   "note": "no",
   "_id": "6580b7d01ee9364e45208972"
},
{
   "event_type": "Debris",
   "event_time": "2023-12-18 21:32:05.235000",
   "end_time": "2023-12-18 21:32:05.235000",
   "is_active": true,
   "x": 40,
   "y": 40,
   "max_distance_x": 1,
   "max_distance_y": 1,
   "created_time": "2023-12-18 21:32:05.235000",
   "created_by_user": "user",
   "last_confirmed_time": null,
   "confirmed_by_user": null,
   "upvote": 0,
   "downvote": 0,
   "short_description": "yeni event koordinatlarını deneme olayıdır",
   "note": "h",
   "_id": "6580ba851ee9364e45208976"
},
{
   "event_type": "Help-Arrived",
   "event_time": "2023-12-15 06:54:00",
   "end_time": null,
   "is_active": false,
   "x": 29,
   "y": 41,
   "max_distance_x": 0.011922768154812803,
   "max_distance_y": 0.008998227349212205,
   "created_time": "2023-12-19 01:52:00",
   "created_by_user": "user",
   "last_confirmed_time": null,
   "confirmed_by_user": null,
   "upvote": 0,
   "downvote": 0,
   "short_description": "Yeni event formunu deniyorum agam",
   "note": "metin girin",
   "_id": "6580f77b1ee9364e45208988"
},
{
   "event_type": "Infrastructure",
   "event_time": null,
   "end_time": null,
   "is_active": true,
   "x": 0,
   "y": 0,
   "max_distance_x": null,
   "max_distance_y": null,
   "created_time": "2023-12-23 13:42:08",
   "created_by_user": "egecans",
   "last_confirmed_time": null,
   "confirmed_by_user": null,
   "upvote": 0,
   "downvote": 0,
   "short_description": "yolda gocuk var",
   "note": "No Internet Connection dikkatli olun address: kahramanmaras",
   "_id": "6586b9827e360e36a4da4a21"
},
{
   "event_type": "Infrastructure",
   "event_time": null,
   "end_time": null,
   "is_active": true,
   "x": 0,
   "y": 0,
   "max_distance_x": null,
   "max_distance_y": null,
   "created_time": "2023-12-23 13:43:07",
   "created_by_user": "egecans",
   "last_confirmed_time": null,
   "confirmed_by_user": null,
   "upvote": 0,
   "downvote": 0,
   "short_description": "yol coktu",
   "note": "No Internet Connection dikkat edin address: uskudar",
   "_id": "6586b9bd7e360e36a4da4a22"
},
{
   "event_type": "Infrastructure",
   "event_time": null,
   "end_time": null,
   "is_active": true,
   "x": 0,
   "y": 0,
   "max_distance_x": null,
   "max_distance_y": null,
   "created_time": "2023-12-24 14:56:30",
   "created_by_user": "ccahid",
   "last_confirmed_time": null,
   "confirmed_by_user": null,
   "upvote": 0,
   "downvote": 0,
   "short_description": "jdjd",
   "note": "No Internet Connection dndmd address: krld",
   "_id": "65881c6fd356cd841863c0cf"
}
]

const ActivityList = ({labels,  userFilter}) => {
  const [chosenActivityType, setChosenActivityType] = useState("resource");
  const [filters, setFilters] = useState({})

  const [resources, setResources] = useState([{ _id: "loading" }]);
  const [needs, setNeeds] = useState([{ _id: "loading" }]);
  const [events, setEvents] = useState([{ _id: "loading" }]);
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
                    radius = 'full'
                    classNames={{
                        cursor: `bg-${chosenActivityType}`,
                        tab: `basis-2/3`,
                    }}
                >
                    <Tab key="need" titleValue={labels.activities.needs} title={labels.activities.needs}/>
                    <Tab key="resource" titleValue={labels.activities.resources} title={labels.activities.resources}/>
                    <Tab key="event" titleValue={labels.activities.events} title={labels.activities.events} />   
                </Tabs>
            </div>
            <div class="w-full">

                <div className=' '>
                  
                </div>

                <ActivityModal isOpen={isOpen} onOpenChange={onOpenChange} activity={activity} activityType={chosenActivityType} />

</div>
  <div className=' max-h-[700px] bg-slate-500 overflow-y-auto w-4/5 p-2'>
    <div className="text-end ">

  <Filter setFilters={setFilters} filters={filters} filterActivities={filterActivities} labels={labels} />
  <Sort chosenActivityType={chosenActivityType} filterActivities={filterActivities} setFilters={setFilters} filters={filters} labels={labels} />
  </div>
    {chosenActivityType === "resource" && resources.map((resource, index) => (
      <ListItem activityType={'resource'} activity={resource}/>
    ))}
    {chosenActivityType === "need" && needs.map((need, index) => (
    <ListItem activityType={'need'} activity={need}/>
    ))}
    {chosenActivityType === "event" && events.map((event, index) => (
    <ListItem activityType={'event'} activity={event}/>
    ))}
    </div>
  </>
}


export default ActivityList;
