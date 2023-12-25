import React from "react";
import {
  Popover,
  PopoverTrigger,
  PopoverContent,
  Button,
  Input,
  CheckboxGroup,
  Checkbox,
  Divider,
} from "@nextui-org/react";
import { FaFilter, FaSort } from "react-icons/fa";

export default function Filter({
  filters,
  setFilters,
  filterActivities,
  labels,
}) {
  const types = [
    "cloth",
    "food",
    "drink",
    "shelter",
    "medication",
    "transportation",
    "tool",
    "human",
    "other",
  ];
  const urgency = [1, 2, 3, 4, 5];
  const status = ["active", "inactive"];

  return (
    <Popover placement="bottom" showArrow offset={10}>
      <PopoverTrigger>
        <Button color="primary" className="mx-3">
          <FaFilter />
          {labels.sort_filter.filter}
        </Button>
      </PopoverTrigger>
      <PopoverContent className="w-[300px]">
        <div className="px-3 py-2 w-full">
          <p className="text-small font-bold text-foreground">
            {labels.sort_filter.filter}
          </p>
          <div className="mt-2 flex flex-col gap-2 w-full">
            <Divider />

            <CheckboxGroup
              label={labels.sort_filter.types}
              orientation="horizontal"
              color="secondary"
              defaultValue={[]}
              onValueChange={(e) => {
                setFilters({ ...filters, types: e });
              }}
            >
              {types.map((type) => (
                <Checkbox value={type}>{type}</Checkbox>
              ))}
            </CheckboxGroup>

            <CheckboxGroup
              label={labels.sort_filter.urgency}
              orientation="horizontal"
              color="secondary"
              defaultValue={[]}
              onValueChange={(e) => {
                setFilters({ ...filters, urgency: e });
              }}
            >
              {urgency.map((urgent) => (
                <Checkbox value={urgent}>{urgent}</Checkbox>
              ))}
            </CheckboxGroup>

            <CheckboxGroup
              label={labels.sort_filter.status}
              orientation="horizontal"
              color="secondary"
              defaultValue={[]}
              onValueChange={(e) => {
                setFilters({ ...filters, status: e });
              }}
            >
              {status.map((stat) => (
                <Checkbox value={stat}>{labels.sort_filter[stat]}</Checkbox>
              ))}
            </CheckboxGroup>
            <Button
              onClick={() => {
                filterActivities();
              }}
            >
              {" "}
              {labels.sort_filter.filter}{" "}
            </Button>
          </div>
        </div>
      </PopoverContent>
    </Popover>
  );
}
