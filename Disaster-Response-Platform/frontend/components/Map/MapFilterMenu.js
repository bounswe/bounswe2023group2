import styles from "./MapFilterMenu.module.scss";
import { FaSearch } from "react-icons/fa";
import CreateActivityButton from "../NewActivityButton";


import {ToastContainer, toast} from "react-toastify";
import 'react-toastify/dist/ReactToastify.css';

export default function MapFilterMenu({activateClick,isMapSelected,selectMap}) {
  const notify = () => {
    toast.info('Haritadan lokasyon seçiniz', {
      position: 'top-center',
      autoClose: 5000, // Auto close the notification after 3 seconds (adjust as needed)
    });
  };
  const popup = () => {
    selectMap();
    onOpen();
    []
  }

  

  return (
    <div className={styles.main}>
      <div className={styles.searchBox}>
        <div className={styles.search}>
          <input
            type="text"
            className={styles.searchTerm}
            placeholder="Ne arıyorsunuz ?"
          ></input>
          <button type="submit" className={styles.searchButton}>
            <FaSearch />
          </button>
        </div>
      </div>
      <div className="CheckBox">
        <div className={styles.header}>Filtreler</div>
        <hr
          style={{
            background: "gray",
            borderColor: "gray",
            height: "1px",
            alignSelf: "center",
          }}
        />
        <div>
          <input type="checkbox" className={styles.checkbox} />
          Resource
        </div>
        <div>
          <input type="checkbox" className={styles.checkbox} />
          Event
        </div>
        <div>
          <input type="checkbox" className={styles.checkbox} />
          Actions
        </div>
        <div>
          <input type="checkbox" className={styles.checkbox} />
          Needs
        </div>
        <button onClick={() => {activateClick(); notify();}} className="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded w-full
                          absolute bottom-0 right-0.5">
            İlan Oluştur
        </button>
        {isMapSelected ? popup() : <></>}
        <ToastContainer/>
        
      </div>
    </div>
  );
}
