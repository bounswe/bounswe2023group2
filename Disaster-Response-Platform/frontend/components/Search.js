
import { Button, Input} from '@nextui-org/react';
import { toast } from 'react-toastify';
import { useState } from 'react';
import { SearchIcon } from '@/components/SearchIcon';
export default function Search({search, setSearch, chosenActivityType, activities, setActivities}) {
  const handleSubmit = async (event) => {
   event.preventDefault();
   console.log(search)
    if(chosenActivityType === undefined){ 
      toast.error("Please choose an activity type");
      return;
    }
    const response = await fetch(`/api/search`, {
      method: 'POST',
      body: JSON.stringify({query: search, activityType: chosenActivityType}),
      headers: {
        'Content-Type': 'application/json'
      }
    });
    let payload = await response.json();
    if(response.status !== 200){
      toast.error(payload?.message);
      return;
    }
    const results = payload.results;
    setActivities(results);
  };
  return <form onSubmit={handleSubmit}>
    <Input
          classNames={{
            base: "max-w-full sm:max-w-[10rem] h-10",
            mainWrapper: "h-full",
            input: "text-small",
            inputWrapper: "h-full font-normal text-default-500 bg-default-400/20 dark:bg-default-500/20",
          }}
          id='search'
          placeholder="Type to search..."
          size="sm"
          onChange={event => setSearch(event.target.value)}
          startContent={<SearchIcon size={18} />}
          type="search"
        />
    </form>;
}