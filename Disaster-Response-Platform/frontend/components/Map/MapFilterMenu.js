import styles from "./MapFilterMenu.module.scss";
import { FaSearch } from "react-icons/fa";
import { useState, useEffect } from "react";
import { ToastContainer, toast } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";
import Search from "../Search";
import { Input } from "@nextui-org/react";

import { api } from "@/lib/apiUtils";
import { Tab, Tabs } from "@nextui-org/react";

export default function MapFilterMenu({
  activateClick,
  setResourceApiData,
  setNeedApiData,
  setEventApiData,
  isMapSelected,
  selectMap,
  labels,
  bounds,
  chosenActivityType,
  setChosenActivityType
}) {
  const [resourceChecked, setResourceChecked] = useState(false);
  const [aktifChecked, setAktifChecked] = useState(false);
  const [eventChecked, setEventChecked] = useState(false);
  const [actionsChecked, setActionsChecked] = useState(false);
  const [needsChecked, setNeedsChecked] = useState(false);
  
  const [search, setSearch] = useState("");
  const [activities, setActivities] = useState([]); // [{_id: "loading"}

  useEffect(() => {
    fetchResources();
    fetchNeeds();
    fetchEvents();
  }, []);

  const notifyFilter = () => {
    toast.info(labels.map.choose_scan_center, {
      position: "top-center",
      autoClose: 5000, // Auto close the notification after 3 seconds (adjust as needed)
    });
  };

  const notifyAdd = () => {
    toast.info(labels.map.choose_location, {
      position: "top-center",
      autoClose: 5000, // Auto close the notification after 3 seconds (adjust as needed)
    });
  };
  const fetchResources = async () => {
    try {
      // Make API call to filter resources based on the search term
      //const searchTerm = /* Get the search term from your input field */;
      const resourceResponse = await api.get(
        `/api/resources/?sort_by=created_at&order=asc`,
        {
          method: "GET",
          headers: {
            "Content-Type": "application/json",
          },
          // Additional headers or credentials if needed
        }
      );
      console.log("consolelog:", resourceResponse.status);
      if (resourceResponse.status == 200) {
        const resources = await resourceResponse.data;
        // Process the data as needed
        console.log("Filtered Resources:", resources);
        setResourceApiData(resources.resources);
        resources.resources.forEach((resource) => {
          const xValue = resource.x;
          const yValue = resource.y;
          console.log(
            `Resource ID: ${resource._id}, X: ${xValue}, Y: ${yValue}`
          );
        });
      } else {
        // Handle errors
        console.error(
          "Error fetching filtered resources:",
          response.statusText
        );
      }
    } catch (error) {
      // Handle unexpected errors
      console.error("Error:", error);
    }
  };

  const fetchNeeds = async () => {
    const needResponse = await api.get(
      `/api/needs/?sort_by=created_at&order=asc`,
      {
        method: "GET",
        headers: {
          "Content-Type": "application/json",
        },
        // Additional headers or credentials if needed
      }
    );
    console.log("consolelog:", needResponse.status);
    if (needResponse.status == 200) {
      const needs = await needResponse.data;
      // Process the data as needed
      console.log("Filtered Needs:", needs);
      setNeedApiData(needs.needs);
      needs.needs.forEach((resource) => {
        const xValue = resource.x;
        const yValue = resource.y;
        console.log(`Resource ID: ${resource._id}, X: ${xValue}, Y: ${yValue}`);
      });
    } else {
      // Handle errors
      console.error("Error fetching filtered resources:", needs.statusText);
    }
  };
  const fetchEvents = async () => {
    const eventResponse = await api.get(`/api/events/`, {
      method: "GET",
      headers: {
        "Content-Type": "application/json",
      },
      // Additional headers or credentials if needed
    });
    console.log("consolelog:", eventResponse.status);
    if (eventResponse.status == 200) {
      const events = await eventResponse.data;
      // Process the data as needed
      console.log("Filtered events:", events);
      setEventApiData(events.events);
      events.events.forEach((event) => {
        const xValue = event.x;
        const yValue = event.y;
        console.log(`Event ID: ${event._id}, x: ${xValue}, y: ${yValue}`);
      });
    } else {
      // Handle errors
      console.error(
        "Error fetching filtered resources:",
        eventResponse.statusText
      );
    }
  };

  const handleFilterClick = async () => {
    console.log("I am in handleFilterClick");
    try {
      // Make API call to filter resources based on the search term
      //const searchTerm = /* Get the search term from your input field */;

      const eventResponse = await api.get(
        `/api/resources/?sort_by=created_at&order=asc`,
        {
          method: "GET",
          headers: {
            "Content-Type": "application/json",
          },
          // Additional headers or credentials if needed
        }
      );
      console.log("consolelog:", eventResponse.status);
      if (eventResponse.status == 200) {
        const events = await eventResponse.data;
        // Process the data as needed
        console.log("Filtered Resources:", events);
        setResourceApiData(events.events);
        events.events.forEach((event) => {
          const xValue = event.x;
          const yValue = event.y;
          console.log(`Resource ID: ${event._id}, X: ${xValue}, Y: ${yValue}`);
        });
      } else {
        // Handle errors
        console.error(
          "Error fetching filtered resources:",
          eventResponse.statusText
        );
      }

      const resourceResponse = await api.get(
        `/api/resources/?sort_by=created_at&order=asc`,
        {
          method: "GET",
          headers: {
            "Content-Type": "application/json",
          },
          // Additional headers or credentials if needed
        }
      );
      console.log("consolelog:", resourceResponse.status);
      if (resourceResponse.status == 200) {
        const resources = await resourceResponse.data;
        // Process the data as needed
        console.log("Filtered Resources:", resources);
        setResourceApiData(resources.resources);
        resources.resources.forEach((resource) => {
          const xValue = resource.x;
          const yValue = resource.y;
          console.log(
            `Resource ID: ${resource._id}, X: ${xValue}, Y: ${yValue}`
          );
        });
      } else {
        // Handle errors
        console.error(
          "Error fetching filtered resources:",
          response.statusText
        );
      }

      const needResponse = await api.get(
        `/api/needs/?sort_by=created_at&order=asc`,
        {
          method: "GET",
          headers: {
            "Content-Type": "application/json",
          },
          // Additional headers or credentials if needed
        }
      );
      console.log("consolelog:", needResponse.status);
      if (needResponse.status == 200) {
        const needs = await needResponse.data;
        // Process the data as needed
        console.log("Filtered Needs:", needs);
        setNeedApiData(needs.needs);
        needs.needs.forEach((resource) => {
          const xValue = resource.x;
          const yValue = resource.y;
          console.log(
            `Resource ID: ${resource._id}, X: ${xValue}, Y: ${yValue}`
          );
        });
      } else {
        // Handle errors
        console.error("Error fetching filtered resources:", needs.statusText);
      }
    } catch (error) {
      // Handle unexpected errors
      console.error("Error:", error);
    }
  };

  const getCursorColor = (type) => {
    switch (type) {
      case "need":
        return "bg-blue-500"; // Replace with your desired blue color
      case "resource":
        return "bg-green-500"; // Replace with your desired green color
      case "event":
        return "bg-red-500"; // Replace with your desired red color
      default:
        return "bg-yellow-400"; // Default color
    }
  };

  const handleSubmit = async (text) => {
    text.preventDefault();
    console.log(search);
    if (chosenActivityType === undefined) {
      toast.error("Please choose an activity type");
      return;
    }
    const response = await fetch(`/api/search`, {
      method: "POST",
      body: JSON.stringify({ query: search, activityType: chosenActivityType }),
      headers: {
        "Content-Type": "application/json",
      },
    });
    let payload = await response.json();
    if (response.status !== 200) {
      toast.error(payload?.message);
      return;
    }
    const results = payload.results;
    setActivities(results);
  };

  const popup = () => {
    selectMap();
    onOpen();
    [];
  };

  return (
    <div className={styles.main}>
      <div className={styles.searchBox}>
        <div className={styles.search}>
          {/* <input
            type="text"
            className={styles.searchTerm}
            placeholder={labels.placeholders.search_bar}
          ></input> */}
          <Input
            className={styles.searchTerm}
            id="search"
            placeholder="Type to search..."
            size="sm"
            onChange={(text) => setSearch(text.target.value)}
            // startContent={<FaSearch className={styles.searchTerm} />}
            type="search"
          />
          <button type="submit" className={styles.searchButton}>
            <FaSearch />
          </button>
        </div>
      </div>
      <div className="CheckBox">
        <div className={styles.header}>{labels.sort_filter.filters}</div>
        <hr
          style={{
            background: "gray",
            borderColor: "gray",
            height: "1px",
            alignSelf: "center",
          }}
        />
        <button
          onClick={() => {
            notifyFilter();
          }}
          className="bg-yellow-500 hover:bg-yellow-700 text-white 
                          font-bold py-1 px-2 rounded w-full "
        >
          {labels.map.scan_certain_area}
        </button>

        <Tabs
          selectedKey={chosenActivityType}
          onSelectionChange={setChosenActivityType}
          size="lg"
          radius="full"
          classNames={{
            cursor: getCursorColor(chosenActivityType),
            tab: `flex`,
          }}
        >
          <Tab
            key="need"
            className={`${
              chosenActivityType === "need" ? "bg-blue-400 z-10" : "bg-gray-200"
            } text-slate-50 flex justify-center items-center px-2 py-1 text-xs max-w-[25%]`}
            titleValue={labels.activities.needs}
            title={labels.activities.needs}
          />
          <Tab
            key="resource"
            className={`${
              chosenActivityType === "resource"
                ? "bg-green-400 z-10"
                : "bg-gray-200"
            } text-slate-50 flex justify-center items-center px-2 py-1 text-xs max-w-[25%]`}
            titleValue={labels.activities.resources}
            title={labels.activities.resources}
          />
          <Tab
            key="event"
            className={`${
              chosenActivityType === "event" ? "bg-red-400 z-10" : "bg-gray-200"
            } text-slate-50 flex justify-center items-center px-2 py-1 text-xs max-w-[25%] `}
            titleValue={labels.activities.events}
            title={labels.activities.events}
          />
          <Tab
            key="all"
            className={`${
              chosenActivityType === "all" ? "bg-yellow-400 z-10" : "bg-gray-200"
            } text-slate-50 flex justify-center items-center px-2 py-1 text-xs max-w-[25%]`}
            titleValue={labels.activities.all}
            title={labels.activities.all}
          />
        </Tabs>
        <div>
          <form action="#">
            <label for="types">{labels.sort_criteria.type}</label>
            <select name="types" id="types">
              <option value="need"> {labels.activities.needs} </option>
              <option value="resource"> {labels.activities.resource} </option>
              <option value="event"> {labels.activities.events} </option>
            </select>
          </form>
        </div>
        <div>
          <form action="#">
            <label for="types">{labels.sort_criteria.type}</label>
            <select name="types" id="types">
              <option value="a"> anaaaa </option>
            </select>
          </form>
        </div>

        <div>
          <form action="#">
            <label for="types">{labels.sort_criteria.subtype}</label>
            <select name="types" id="types">
              <option value="a"> a </option>
            </select>
          </form>
        </div>
        <div>
          <input
            type="checkbox"
            className={styles.checkbox}
            checked={aktifChecked}
            onChange={() => setAktifChecked(!aktifChecked)}
          />
          {labels.sort_filter.active_only}
        </div>
        <div>
          <input type="checkbox" className={styles.checkbox} />
          {labels.activities.resources}
        </div>
        <div>
          <input type="checkbox" className={styles.checkbox} />
          {labels.activities.events}
        </div>
        <div>
          <input type="checkbox" className={styles.checkbox} />
          {labels.activities.actions}
        </div>
        <div>
          <input type="checkbox" className={styles.checkbox} />
          {labels.activities.needs}
        </div>
        <div>
          <input type="checkbox" className={styles.checkbox} />
          {labels.activities.emergencies}
        </div>
        <button
          onClick={() => {
            handleFilterClick();
          }}
          className="bg-green-500 hover:bg-green-700 text-white font-bold py-2 px-4 rounded w-full
                          absolute bottom-10 right-0.5"
        >
          {labels.sort_filter.filter}
        </button>
        <button
          onClick={() => {
            activateClick();
            notifyAdd();
          }}
          className="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded w-full
                          absolute bottom-0 right-0.5"
        >
          {labels.activities.add_resource}
        </button>
        {isMapSelected ? popup() : <></>}
        <ToastContainer />
      </div>
    </div>
  );
}
