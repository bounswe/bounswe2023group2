import React, { useState } from "react";
import {Button, Checkbox, CheckboxGroup, Divider, Popover, PopoverContent, PopoverTrigger, Radio, RadioGroup } from "@nextui-org/react";
import { FaSort } from "react-icons/fa";

export default function Sort({ needFilter, resourceFilter, filters, setFilters, filterActivities}) {
  const needSorts = ['urgency', 'status', 'occur_at', 'type', 'upvote']
  const resourceSorts = ['currentQuantity', 'status', 'upvote', 'type']
  return (
    <Popover placement="bottom" showArrow offset={10}>  
    <PopoverTrigger>
      <Button color="primary">
        <FaSort/>Sırala
        </Button>
    </PopoverTrigger>
    <PopoverContent className="w-[300px]">

      <div className="px-1 py-2 w-full">
        <p className="text-small font-bold text-foreground" >
          Sırala
        </p>
        <div className="mt-2 flex flex-col gap-2 w-full">
          
        <Divider />
        
            <CheckboxGroup
              label="Siralamak istediginiz kriteri seciniz"
              orientation="horizontal"
              color="secondary"
              defaultValue={[]}
              onValueChange={(e) => { setFilters({ ...filters, sort_by: e }) }}
            >
             {needFilter && needSorts.map((type)=>  <Checkbox value={type}>{type}</Checkbox>)}  
              {resourceFilter && resourceSorts.map((type)=>  <Checkbox value={type}>{type}</Checkbox>)}
            </CheckboxGroup>

            <RadioGroup
              label="Siralama cesidini seciniz"
              orientation="horizontal"
              color="secondary"
              onValueChange={(e) => { setFilters({ ...filters, order: e }) }}
            >
             {['ASC', "DESC"].map((stat)=>  <Radio value={stat}>{stat}</Radio>)}  
            </RadioGroup>
            <Button onClick={()=>{filterActivities()}}> Sırala </Button>

  

      </div>
        </div>
    </PopoverContent>
  </Popover>
);
}

