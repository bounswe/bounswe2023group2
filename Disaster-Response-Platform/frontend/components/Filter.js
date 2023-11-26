import React from "react";
import { Popover, PopoverTrigger, PopoverContent, Button, Input, CheckboxGroup, Checkbox, Divider } from "@nextui-org/react";
import { FaFilter, FaSort } from "react-icons/fa";

export default function Filter({ filters, setFilters, filterActivities }) {
  const types = ['shelter', 'food', 'water', 'medical', 'rescue', 'other']
  const urgency = ['low', 'medium', 'high']
  const status = ['active', 'inactive']
  
  return (

    <Popover placement="bottom" showArrow offset={10}>  
      <PopoverTrigger>
        <Button color="primary">
          <FaFilter/>Filter
          </Button>
      </PopoverTrigger>
      <PopoverContent className="w-[300px]">

        <div className="px-1 py-2 w-full">
          <p className="text-small font-bold text-foreground" >
            Filtreler
          </p>
          <div className="mt-2 flex flex-col gap-2 w-full">
            
          <Divider />
          
              <CheckboxGroup
                label="Ürün Seçiniz"
                orientation="horizontal"
                color="secondary"
                defaultValue={[]}
                onValueChange={(e) => { setFilters({ ...filters, type: e }) }}
              >
               {types.map((type)=>  <Checkbox value={type}>{type}</Checkbox>)}  
              </CheckboxGroup>

              <CheckboxGroup
                label="Acil Durum Seçiniz"
                orientation="horizontal"
                color="secondary"
                defaultValue={[]}
                onValueChange={(e) => { setFilters({ ...filters, urgency: e}) }}

              >
               {urgency.map((urgent)=>  <Checkbox value={urgent}>{urgent}</Checkbox>)}  
              </CheckboxGroup>

              <CheckboxGroup
                label="Status Seçiniz"
                orientation="horizontal"
                color="secondary"
                defaultValue={[]}
                onValueChange={(e) => { setFilters({ ...filters, urgency: e}) }}
              >
               {status.map((stat)=>  <Checkbox value={stat}>{stat}</Checkbox>)}  
              </CheckboxGroup>
              <Button onClick={(()=>{filterActivities})}>  </Button>

    

        </div>
          </div>
      </PopoverContent>
    </Popover>
  );
}
