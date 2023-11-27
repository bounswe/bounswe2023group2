import React from "react";
import {Progress} from "@nextui-org/react";

export default function Status({value, initial, current}) {

  return (
    <Progress
      aria-label="progress bar"
      label={`${(initial- current)}/${initial}`}
      size="lg"
      value={((initial-current)/current)*100}
      
      color="success"
      showValueLabel={true}

      className="max-w-md"
    />
  );
}
