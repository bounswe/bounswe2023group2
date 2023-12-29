import React, { useEffect } from "react";
import { Modal, ModalContent, ModalHeader, ModalBody, ModalFooter, Button, useDisclosure, Divider, Avatar, Chip } from "@nextui-org/react";
import { BiDislike, BiSolidDislike, BiSolidLike } from "react-icons/bi";
import { BiLike } from "react-icons/bi";
import { FaAngleDown, FaAngleUp } from "react-icons/fa";
import Status from "./StatusBar";
import Link from "next/link";
import { api } from "@/lib/apiUtils";
export default function ActivityModal({ isOpen, onOpen, onOpenChange, activity, activityType, labels }) {
  const [like, setLike] = React.useState(false);
  const [dislike, setDislike] = React.useState(false);
  const [display, setDisplay] = React.useState({});
  const handleLike = async (e) => {
    try {
      if ((e.voteType === 'like' && e.vote === true) || (e.voteType === 'dislike' && e.vote === false)) {

        const result = await fetch(`/api/vote/upvote`, {
          method: 'POST',
          headers: {
            "Content-Type": "application/json",
          }, body: JSON.stringify({ entityID: activity._id, entityType: activityType })
        })
        const data = await result.json()
      }

      if ((e.voteType === 'like' && e.vote === false) || (e.voteType === 'dislike' && e.vote === true)) {
        const result = await fetch(`/api/vote/downvote`, {
          method: 'POST',
          headers: {
            "Content-Type": "application/json",
          }, body: JSON.stringify({ entityID: activity._id, entityType: activityType })
        })
        const data = await result.json()
      }
    } catch (error) {
      console.log(error)
    }
  }
  const show_list = {'created_at': 'Olusturulma tarihi' }
return (
    <>
      <Modal isOpen={isOpen} onOpenChange={onOpenChange} className='text-black'>
        <ModalContent>
          {(onClose) => (
            <>
              <ModalHeader className="flex flex-col gap-1 ">{activity.type}</ModalHeader>
              <Divider />
              <ModalBody>

                {Object.keys(activity).map((key) => {
                  if (Object.keys(show_list).includes(key))
                  return <p> {activity[key]}: {activity[key]}</p>
                }
                )}

                {activity.details && Object.keys(activity.details).map((key) => {
                  return <p>
                    {key}: {activity.details[key] ?? " belirtilmemis"}
                  </p>
                })}
                { activity.condition &&
                
                <span className=" flex flex-row gap-1 items-center ">
                  {['new', 'used'].map((condition) => {
                    if (condition === activity.condition)
                      return <Chip size='lg' color="warning" >{condition}</Chip>
                    else
                      return <Chip color="default" >{condition}</Chip>
                  })
                  }
                </span>
}
                {activityType == 'resources' && <Status value={(activity.initialQuantity - activity.currentQuantity) * 100 / activity.initialQuantity} initial={activity.initialQuantity} current={activity.currentQuantity} />}
                {activityType == 'needs' && <Status value={(- activity.unsupliedQuantity + activity.initialQuantity) * 100 / activity.initialQuantity} initial={activity.initialQuantity} current={activity?.unsupliedQuantity ?? activity.initialQuantity } />}

                <p className='m-2 bg-sky-100 rounded p-2'>
                  {activity.description ?? "No description"}
                </p>
                <Divider />
                <div className='flex flex-row justify-between items-center'>
                  <div className='flex flex-row gap-1 items-center'>
                    <Button
                        className="hover:bg-gray-300"
                        onClick={(e) => { setLike(!like); setDislike(false); handleLike({ voteType: 'like', vote: like }) }}
                        variant="bordered"
                        startContent={!like ? <FaAngleUp className='w-8 h-8' /> : <FaAngleUp className='w-8 h-8' color="green" />}
                    >
                      reliable
                    </Button>
                    <Button
                        className="hover:bg-gray-300"
                        onClick={(e) => { setLike(false); setDislike(!dislike); handleLike({ voteType: 'dislike', vote: dislike }) }}
                        variant="bordered"
                        startContent={!dislike ? <FaAngleDown className='w-8 h-8' /> : <FaAngleDown className='w-8 h-8' color="red" />}
                    >
                     unreliable
                    </Button>
                  </div>
                  <Link href={`/user/${activity.created_by}`}>
                    <Chip
                      color="warning"
                      size="lg"
                      avatar={
                        <Avatar name={activity.created_by} size="lg" getInitials={(name) => name.charAt(0)} />
                      }
                    >
                      {activity.created_by}
                    </Chip>
                  </Link>
                </div>

              </ModalBody>
            </>
          )}
        </ModalContent>
      </Modal>
    </>
  );
}
