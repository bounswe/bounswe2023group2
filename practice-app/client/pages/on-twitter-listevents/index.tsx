'use client';
import {ReactDOM, useEffect} from 'react';

import {useState} from 'react';

import {useFormik} from 'formik';
import type {NextPage} from 'next';
import * as yup from 'yup';

import axios from 'axios';

import useStorage from "@/pages/useStorage";
import {Table} from '@nextui-org/react';
import {hydrateRoot} from "react-dom/client";
import {hydrate} from "next/client";

import 'bootstrap/dist/css/bootstrap.min.css';

const ListAllTweets: NextPage = () => {
    const [message, setMessage] = useState(''); // This will be used to show a message if the submission is successful
    const [submitted, setSubmitted] = useState(false);
    let {getItem} = useStorage();
    let usernameLogin = ""
    let password = getItem("password");
    let isDBA = getItem("isDBA");

    let apiBaseURL = getItem("apiBaseURL");
    let {setItem} = useStorage();

    const [userTitle, setUser] = useState('Not logged in')
    // During hydration `useEffect` is called. `window` is available in `useEffect`. In this case because we know we're in the browser checking for window is not needed. If you need to read something from window that is fine.
    // By calling `setColor` in `useEffect` a render is triggered after hydrating, this causes the "browser specific" value to be available. In this case 'red'.
    useEffect(() => setUser(getItem("username")), [])
    useEffect(() => {
        (async () => {
            handleSubmit();
        })()
    }, [])

    const columns = [
        {
            key: "event_date",
            label: "Event Date",
            align: 'right',
        },
        {
            key: "event_summary",
            label: "Event summary",
            style: {whiteSpace: "normal"}
        },
        {
            key: "related_twits",
            label: "Related Tweets",
        },
        {
            key: "verified",
            label: "Verified",
        },
        {
            key: "event_id",
            label: "Event ID",
        },
    ];

    const [data, setData] = useState([]);
    const handleSubmit2 = async (event: any) => {
        event.preventDefault();
        event.preventDefault();
        let eventId = document.getElementsByName("eventid").values().next().value.value;
        try {
            let baseUrl = `${process.env.NEXT_PUBLIC_BACKEND_URL}/on-twitter/deletePublishedTweets/`;

            let dataPart = `${eventId}`
            baseUrl = `${baseUrl}${dataPart}`;

            const response = await axios.get(baseUrl);

            const result = JSON.stringify(response.data);
            window.alert(`Tweets deleted for event: ${eventId}`)
            handleSubmit()
            // eslint-disable-next-line react-hooks/rules-of-hooks
        } catch (err: any) {
            if (typeof window !== "undefined") {
                if (err.response.status == 404) {
                    window.alert("Event not found");
                } else if (err.response.status == 418) {
                    window.alert("Cant'do - I'm a teapot");
                } else
                    window.alert(err.response.status);
                return;
            }
        }
    }

    const handleSubmit3 = async (event: any) => {
        event.preventDefault();
        let eventId = document.getElementsByName("eventid").values().next().value.value;
        try {
            let baseUrl = `${process.env.NEXT_PUBLIC_BACKEND_URL}/on-twitter/publishTweets/`;

            let dataPart = `${eventId}`
            baseUrl = `${baseUrl}${dataPart}`;

            const response = await axios.post(baseUrl);

            const result = JSON.stringify(response.data);
            window.alert(`Tweets created for event: ${eventId}`)
            handleSubmit()
            // eslint-disable-next-line react-hooks/rules-of-hooks
        } catch (err: any) {
            if (typeof window !== "undefined") {
                if (typeof window !== "undefined") {
                    if (err.response.status == 404) {
                        window.alert("Event not found");
                    } else if (err.response.status == 418) {
                        window.alert("Cant'do - I'm a teapot");
                    } else
                        window.alert(err.response.status);
                    return;
                }
            }
        }
    };

    async function handleSubmit() {
        try {
            let baseUrl = `${process.env.NEXT_PUBLIC_BACKEND_URL}/on-twitter/getEvents`;

            //let dataPart = `?eventId=`
            baseUrl = `${baseUrl}`;
            //baseUrl = `${baseUrl}${dataPart}`;

            const response = await axios.get(baseUrl);

            const result = JSON.stringify(response.data);
            const eventsData = JSON.parse(result)["Events"];
            if (eventsData != null) {
                setData(eventsData);
            }

            // eslint-disable-next-line react-hooks/rules-of-hooks
        } catch (err: any) {
            if (typeof window !== "undefined") {
                if (err.response.status == 401) {
                    window.alert("Not authorized for this");
                } else
                    window.alert(err.response.status);
                return;
            }
        }
    };

    return (
        <div className="vh-100 d-flex flex-column justify-content-center align-items-center">
            <div hidden={!submitted} className="alert alert-primary" role="alert">
                {message}
            </div>
            <h1> All Events</h1>
            <Table
                id="EventsTable"
                aria-label="Events"
                css={{
                    height: "auto",
                    minWidth: "100%",
                    whiteSpace: "normal",
                }}
            >
                <Table.Header columns={columns}>
                    {(column) => (
                        <Table.Column key={column.key}>{column.label}</Table.Column>
                    )}
                </Table.Header>
                <Table.Body items={data}>
                    {(item) => (
                        <Table.Row key={item["key"]}>
                            {(columnKey) => <Table.Cell css={{
                                height: "auto",
                                minWidth: "100%",
                                whiteSpace: "break-spaces",
                            }}
                            >{item[columnKey]}</Table.Cell>}
                        </Table.Row>
                    )}
                </Table.Body>
            </Table>
            <form className="w-50" onSubmit={handleSubmit2}>
                <div className="name block">
                    <div>
                        <label htmlFor="eventid" className="form-label">
                            Event ID
                        </label>
                        <input
                            type="text"
                            id="eventid"
                            name="eventid"
                            className="form-control"
                            placeholder=""
                        />
                        <div>

                            <button type="submit" className="btn btn-primary" style={{
                                margin: "10px",
                            }}>
                                Delete twits for event
                            </button>

                            <button type="submit" className="btn btn-primary" style={{
                                margin: "10px",
                            }} onClick={handleSubmit3}>
                                Create twits for event
                            </button>

                        </div>
                    </div>
                </div>
            </form>
            {/* eslint-disable-next-line @next/next/no-html-link-for-pages */}
            <a href="/">Home
            </a>
        </div>
    );
};

export default ListAllTweets;

