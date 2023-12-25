import { Button, Card, CardBody, CardFooter, CardHeader, Divider, Link } from '@nextui-org/react';
import React from 'react';
import { BiDownvote, BiUpvote } from "react-icons/bi";
const ListItem = ({ activityType, activity }) => {
  return <>
    {activityType === "event" &&
      <Card shadow='none' isHoverable isPressable className='border-1 w-3/4 m-auto mb-3'>
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
          <div> date: {activity?.event_time ?? activity?.created_time} </div ><div  className="flex justify-between items-center" ><BiUpvote /> <BiDownvote /> </div>
        </CardFooter>
      </Card>
    }
    {activityType === "resource" &&
      <Card shadow='none' isHoverable isPressable className='border-1 w-3/4 m-auto mb-3'>
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
            <div>Address <Link href={`https://www.google.com/maps/place/${activity.x},${activity.y}`}>{activity?.open_address} {activity.x}:{activity.y} </Link> </div>
          </div>

        </CardBody>
        <Divider />
        <CardFooter className="flex justify-between px-5">
          <div> date: {activity?.occur_at ?? activity?.created_at} </div><div><BiUpvote /> <BiDownvote /> </div>
        </CardFooter>
      </Card>
    }
    {activityType === "need" && 
      <Card shadow='none' isHoverable isPressable className='border-1 w-3/4 m-auto mb-3'>
        <CardHeader className="flex justify-between px-5">
          <div> {activity?.type} </div>
        </CardHeader>
        <Divider />
        <CardBody>
          <div>
            <div> {activity?.description}</div>
          </div>
          <div className="flex justify-between items-center">
            <div>Address <Link href={`https://www.google.com/maps/place/${activity.x},${activity.y}`}>{activity?.open_address} {activity.x}:{activity.y} </Link> </div>
          </div>

        </CardBody>
        <Divider />
        <CardFooter className="flex justify-between px-5">
          <div> date: {activity?.occur_at ?? activity?.created_at} </div><div> </div>
        </CardFooter>
      </Card>
}


  </>
};

export default ListItem;
