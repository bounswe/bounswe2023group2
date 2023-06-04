import dynamic from 'next/dynamic'
import {Table} from "@nextui-org/react";

function HomePage() {

    return (
        <div className="vh-100 d-flex flex-column justify-content-center align-items-center">
            <h1> All forms</h1>

            <p>
                <a href="/alerts">Alerts </a>
            </p>
            <p>
                <a href="/emailreport">E-mail report </a>
            </p>
            <p>
                <a href="/filtersort">Filter sort </a>
            </p>
            <p>
                <a href="/location">Location </a>
            </p>
            <p>
                <a href="/login">Login </a>
            </p>
            <p>
                <a href="/news">News </a>
            </p>
            <p>
                <a href="/notifications">Notifications </a>
            </p>
            <p>
                <a href="/registration">Registration </a>
            </p>
            <p>
                <a href="/timezone">Timezone </a>
            </p>
            <p>
                <a href="/on-twitter-listevents">Twitter API </a>
            </p>

        </div>
    )

}

export default HomePage