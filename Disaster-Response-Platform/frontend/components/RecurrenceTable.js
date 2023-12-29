import { Button, TableBody, TableCell, TableColumn, TableHeader, TableRow, useDisclosure, Table, getKeyValue } from "@nextui-org/react";
import React, { useEffect, useState } from "react";
import ActivityModal from "./ActivityModal";
import Filter from "./Filter";
import Sort from "./Sort";
import { toast } from 'react-toastify';
import { api } from "@/lib/apiUtils";
import AddActionForm from "./AddAction";
import { GrTransaction } from "react-icons/gr";
import recurrenceService from "@/services/recurrenceService";

export default function Recurrence({ chosenActivityType, labels }) {
    const [filters, setFilters] = useState({})
    const [recurrence, setRecurrence] = useState([]);
    const [selectedKeys, setSelectedKeys] = useState(new Set(["text"]));
    const { isOpen, onOpen, onOpenChange } = useDisclosure();
    const [activity, setActivity] = useState({});
    const {
        isOpen: isRecurrenceModalOpen,
        onOpen: onOpenRecurrenceModal,
        onOpenChange: onOpenChangeRecurrenceModal,
    } = useDisclosure()
    const getRecurrences = async () => {
        const response = await recurrenceService.getRecurrences();
        let res = await response.json();
        if (response.ok) {

            setResources(res.resources)
        } else {
            toast.error(labels.feedback.failure);
        }
    }

    useEffect(() => {
        getRecurrences();
        
    }, [])
    
    const columns = {
        "resources": ["type", "location", "created_by", "created_at", "description"],
        "needs": ["type", "location", "created_by", "created_at", "occur_at", "description", "recurrence_rate", "take_action"],
        "events": ["event_type", "location", "created_by_user", "event_time", "short_description", "is_active"],
        "recurrences": ["type", "location", "created_by", "description", "recurrence_rate", "take_action" ]
    }

    function getColumns() {
        let result = [];
        for (let column of columns[chosenActivityType]) {
            result.push({"key": column, "label": labels.activity_table[column]});
        }
        return result;
    }

    return (

        <div class="w-full">
            <div className=' '>
                <Filter setFilters={setFilters} filters={filters} filterActivities={filterActivities} labels={labels} />
                <Sort chosenActivityType={chosenActivityType} filterActivities={filterActivities} setFilters={setFilters} filters={filters} labels={labels} />
            </div>

            <ActivityModal isOpen={isOpen} onOpenChange={onOpenChange} activity={activity} activityType={chosenActivityType} labels={labels} />

            <Table
                isHeaderSticky
                selectionMode="single"
                aria-label="activity table"
                className='flex w-full overflow-x-auto shadow-md sm:rounded max-h-[300px]'
            >
                <TableHeader columns={getColumns()} emptyContent={labels.activity_table.no_content}>
                    {(column) => <TableColumn key={column.key}>{column.label}</TableColumn>}
                </TableHeader>
                <TableBody items={getRows()}>
                    {item => (
                        <TableRow key={item._id}>
                            {(columnKey) => {
                                if (item._id === "loading") {
                                    return <TableCell> {labels.UI.loading} </TableCell>;
                                }
                                let content = "";
                                switch (columnKey) {
                                case "location":
                                    content = `${item.x} : ${item.y}`;
                                    break;
                                case "take_action":
                                    content = (
                                        <span className="text-lg text-default-400 cursor-pointer active:opacity-50">
                                            <GrTransaction />
                                        </span>
                                    );
                                    break;
                                case "is_active":
                                    content = item.is_active ? "✔" : "❌";
                                    break;
                                default:
                                    content = item[columnKey];
                                }
                                return (
                                    <TableCell onClick={() => {
                                        setActivity(item);
                                        if (columnKey === "take_action") {
                                            onOpenNeedModal();
                                        } else {
                                            onOpen();
                                        }}} >
                                        {content}
                                    </TableCell>
                                );
                            }}
                        </TableRow>
                    )}
                </TableBody>
            </Table>
            {/* <AddActionForm onOpenChange={onOpenChangeNeedModal} isOpen={isNeedModalOpen} table_need={activity} need_type={activity.type} labels={labels}/> */}
        </div>

    );
}