import styles from "./MapFilterMenu.module.scss";
import { FaSearch } from "react-icons/fa";
export default function MapFilterMenu() {
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
      </div>
    </div>
  );
}
