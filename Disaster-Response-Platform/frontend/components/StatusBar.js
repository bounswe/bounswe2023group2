import React from "react";
import {Progress} from "@nextui-org/react";

export default function Status({value, initial, current}) {

  return (
    <Progress
      aria-label="progress bar"
      label={`${Math.abs(initial- current)}/${initial}`}
      size="lg"
      value={Math.abs(initial- current) * 100/initial}
      
      color="success"
      showValueLabel={true}

      className="max-w-md"
    />
  );
}
