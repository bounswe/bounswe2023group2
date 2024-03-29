import { Button, Card, CardBody, CardFooter, CardHeader, Divider, Link, useDisclosure } from '@nextui-org/react';
import React from 'react';
import { BiDownvote, BiUpvote } from "react-icons/bi";
import AddActionForm from './AddAction';
import ActivityModal from './ActivityModal';
const ActivitySimple = ({ activityType, activity, ...props }) => {
const { isOpen, onOpen, onOpenChange } = useDisclosure();
  return <>
    {activityType === "event" &&
      <Card shadow='none' isHoverable isPressable  className='border-1 p-2 m-auto mb-3' >
        <CardHeader className="flex justify-between px-5">
          <div> {activity?.event_type} </div>
        </CardHeader>
        <Divider />
        <CardBody>
          <div>
            <div> {activity?.short_description}</div>
            <div> {activity?.note}</div>
          </div>
          <div className="flex justify-between items-center">
            <div>Address <Link href={`https://www.google.com/maps/place/${activity.x},${activity.y}`}>{activity?.open_address} {activity.x}:{activity.y} </Link> </div>
          </div>

        </CardBody>
        <Divider />
        <CardFooter className="flex justify-between px-5">
          <div> date: {activity?.event_time ?? activity?.created_time} </div >  
        </CardFooter>
      </Card>
    }
    {activityType === "resource" &&
      <Card shadow='none' isHoverable isPressable  {...props}  onPress={console.log('alooo')} className='border-1 m-auto mb-3'>
        <CardHeader className="flex justify-between px-5">
          <div> {activity?.type} </div>
        </CardHeader>
        <Divider />
        <CardBody>
          <div>
            <div> {activity?.description}</div>
            <div> {activity?.note}</div>
          </div>
          <div className="flex justify-between items-center">
            Address <Link href={`https://www.google.com/maps/place/${activity.x},${activity.y}`}>{activity?.open_address} {activity.x}:{activity.y} </Link> 
          </div>

        </CardBody>
        <Divider />
        <CardFooter className="flex justify-between px-5">
          <div> date: {activity?.occur_at ?? activity?.created_at} </div>
        </CardFooter>
      </Card>
    }
    {activityType === "need" && 
      <Card shadow='none'  isPressable  {...props}   className='border-1 m-3 mb-3' >
        <CardHeader className="flex justify-between px-5">
          <div> {activity?.type} </div>
        </CardHeader>
        <Divider />
        <CardBody>
          <div>
            <p> {activity?.description}</p>
          </div>
          <div className="flex justify-between items-center">
            <div>Address <Link href={`https://www.google.com/maps/place/${activity.x},${activity.y}`}>{activity?.open_address} {activity.x}:{activity.y} </Link> </div>
          </div>

        </CardBody>
        <Divider />
        <CardFooter className="flex justify-between px-5">
          date: {activity?.occur_at ?? activity?.created_at}
        </CardFooter>
      </Card>
} 
  </>
};

export default ActivitySimple;
