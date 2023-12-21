import React, { useState } from "react";
import {Button, Checkbox, CheckboxGroup, Divider, Popover, PopoverContent, PopoverTrigger, Radio, RadioGroup } from "@nextui-org/react";
import { FaSort } from "react-icons/fa";

export default function Sort({ chosenActivityType, filters, setFilters, filterActivities, labels}) {
  const needSorts = ['urgency', 'status', 'occur_at', 'type', 'upvote']
  const resourceSorts = ['currentQuantity', 'status', 'upvote', 'type']
  return (
    <Popover placement="bottom" showArrow offset={10}>  
    <PopoverTrigger>
      <Button color="primary">
        <FaSort/>{labels.sort_filter.sort}
        </Button>
    </PopoverTrigger>
    <PopoverContent className="w-[300px]">

      <div className="px-1 py-2 w-full">
        <p className="text-small font-bold text-foreground" >
          {labels.sort_filter.sort}
        </p>
        <div className="mt-2 flex flex-col gap-2 w-full">
          
        <Divider />
        
            <CheckboxGroup
              label={labels.sort_filter.sort_criterion}
              orientation="horizontal"
              color="secondary"
              defaultValue={[]}
              onValueChange={(e) => { setFilters({ ...filters, sort_by: e }) }}
            >
             {chosenActivityType === "needs" && needSorts.map((type)=>  <Checkbox value={type}>{labels.sort_criteria[type]}</Checkbox>)}  
              {chosenActivityType === "resources" && resourceSorts.map((type)=>  <Checkbox value={type}>{labels.sort_criteria[type]}</Checkbox>)}
            </CheckboxGroup>

            <RadioGroup
              label={labels.sort_filter.sort_direction}
              orientation="horizontal"
              color="secondary"
              onValueChange={(e) => { setFilters({ ...filters, order: e }) }}
            >
             {['ASC', "DESC"].map((stat)=>  <Radio value={stat}>{labels.sort_filter[stat]}</Radio>)}  
            </RadioGroup>
            <Button onClick={()=>{filterActivities()}}> {labels.sort_filter.sort} </Button>

  

      </div>
        </div>
    </PopoverContent>
  </Popover>
);
}

