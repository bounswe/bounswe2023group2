import styles from "./MapFilterMenu.module.scss";
import { FaSearch } from "react-icons/fa";
import { useState } from "react";
import { ToastContainer, toast } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";
import { api } from "@/lib/apiUtils";

export default function MapFilterMenu({
  activateClick,
  setResourceApiData,
  isMapSelected,
  selectMap,
  labels
}) {
  const [resourceChecked, setResourceChecked] = useState(false);
  const [aktifChecked, setAktifChecked] = useState(false);
  const [eventChecked, setEventChecked] = useState(false);
  const [actionsChecked, setActionsChecked] = useState(false);
  const [needsChecked, setNeedsChecked] = useState(false);

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

  const handleFilterClick = async () => {
    console.log("I am in handleFilterClick");
    try {
      // Make API call to filter resources based on the search term
      //const searchTerm = /* Get the search term from your input field */;
      const response = await api.get(
        `/api/resources/?sort_by=created_at&order=asc`,
        {
          method: "GET",
          headers: {
            "Content-Type": "application/json",
          },
          // Additional headers or credentials if needed
        }
        
      );
      console.log("consolelog:",response.status);
      if (response.status == 200) {
         const resources = await response.data;
        // Process the data as needed
        console.log("Filtered Resources:", resources);
        setResourceApiData(resources.resources);
        resources.resources.forEach(resource => {
          const xValue = resource.x;
          const yValue = resource.y;
          console.log(`Resource ID: ${resource._id}, X: ${xValue}, Y: ${yValue}`);
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

  const popup = () => {
    selectMap();
    onOpen();
    [];
  };

  return (
    <div className={styles.main}>
      <div className={styles.searchBox}>
        <div className={styles.search}>
          <input
            type="text"
            className={styles.searchTerm}
            placeholder={labels.placeholders.search_bar}
          ></input>
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

        <div>
          <input className=""></input>
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
