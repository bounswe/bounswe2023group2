import React, { useState } from "react";
import { Dropdown, DropdownTrigger, DropdownMenu, DropdownItem, Button } from "@nextui-org/react";
import { FaSort } from "react-icons/fa";

export default function Sort({ needFilter, resourceFilter, selectedKeys, setSelectedKeys, filterActivities }) {
  const needSorts = ['urgency', 'status', 'date', 'type']
  const resourceSorts = ['quantity', 'status', 'date', 'type']
  return (
    <Dropdown className='text-black'>
      <DropdownTrigger>
        <Button
          className="capitalize"
        >
          <FaSort />
          Sort
        </Button>
      </DropdownTrigger>
      <DropdownMenu
        aria-label="Sort selection"
        // variant="flat"
        closeOnSelect={false}
        disallowEmptySelection
        selectionMode="multiple"
        selectedKeys={selectedKeys}
        onSelectionChange={setSelectedKeys}
        onClose={() => {filterActivities}}
      >
        {needFilter && needSorts.map((sort) => <DropdownItem key={sort}>{sort}</DropdownItem>)}
        {resourceFilter && resourceSorts.map((sort) => <DropdownItem key={sort}>{sort}</DropdownItem>)}
      </DropdownMenu>
    </Dropdown>
  );
}
