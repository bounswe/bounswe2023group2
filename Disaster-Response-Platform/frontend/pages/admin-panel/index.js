import MainLayout from '@/layouts/MainLayout';
import { withIronSessionSsr } from 'iron-session/next';
import sessionConfig from '@/lib/sessionConfig';
import { Button, Table, TableHeader, TableColumn, TableBody, TableRow, TableCell, getKeyValue, Tab, Tabs } from "@nextui-org/react";
import { useState, useEffect } from "react";
import { useRouter } from 'next/navigation';
import { api } from '@/lib/apiUtils';
import getLabels from '@/lib/getLabels';
import Link from 'next/link';
import { ToastContainer, toast } from 'react-toastify';

export default function AdminPanel({ unauthorized, allReports, userList, bannedList, labels }) {
  const [chosenReportType, setChosenReportType] = useState("users");
  const router = useRouter();
  if (unauthorized) {
    useEffect(() => {setTimeout(() => {router.push("/")}, 2000)});
    return (
      <div class="text-center text-xl">
        <br /><br /><br />
        {labels.admin.no_access}
      </div>
    );
  }

  async function reject(_id, report_type) {
    const response = await fetch('/api/reports/reject', {
      method: 'POST',
      headers: {
        "Content-Type": "application/json",
      },
      body: {_id}
    });
    if (!response.ok) {
      toast(labels.feedback.failure);
      return;
    }
    toast(labels.admin.report_rejected);
    setReports[report_type](list => list.filter(elem => elem._id !== _id));
  }

  async function accept(_id, report_type, report_type_id) {
    await fetch('/api/reports/accept', {
      method: 'POST',
      headers: {
        "Content-Type": "application/json",
      },
      body: {_id, report_type, report_type_id}
    });
    if (!response.ok) {
      toast(labels.feedback.failure);
      return;
    }
    toast(labels.admin.report_accepted);
    setReports[report_type](list => list.filter(elem => elem._id !== _id));
  }

  const rejectButton = (...args) => <Button onPress={() => reject(...args)} className="mx-1 block text-white bg-gray-400 hover:bg-gray-500 focus:ring-4 focus:outline-none focus:ring-gray-200 rounded-lg px-2 text-center dark:bg-gray-300 dark:hover:bg-gray-400 dark:focus:ring-gray-600"> {labels.admin.reject} </Button>;
  const acceptButton = (...args) => <Button onPress={() => accept(...args)} className="mx-1 block text-white bg-red-600 hover:bg-red-700 focus:ring-4 focus:outline-none focus:ring-red-200 rounded-lg px-2 text-center dark:bg-red-500 dark:hover:bg-red-600 dark:focus:ring-red-700"> {labels.admin.accept} </Button>;
  const report_types = ["users", "needs", "resources", "actions", "events"];

  let reports = {};
  let setReports = {};
  for (const report_type of report_types) {
    [reports[report_type], setReports[report_type]] = useState(
          allReports.filter(elem => (elem.report_type === report_type && elem.status === "undefined"))
            .map(
              (
                {created_by: reporter, description, _id, report_type_id: reported}
              ) => (
                {
                  "key": _id,
                  "reporter": <Link href={`user/${reporter}`} className="text-blue-600">{reporter}</Link>,
                  description,
                  "reported": report_type === "users" ? <Link href={`user/${reported}`} className="text-blue-600">{reported}</Link> : reported,
                  "actions": <div class="flex">{rejectButton(_id, report_type)}{acceptButton(_id, report_type, reported)}</div>
                }
              )
            )
    );
  }

  return (
    <main>
      <div class="my-10">
        <h3 class="object-top text-center text-xl mb-3"> {labels.admin.reports} </h3>
        <div className="text-center">
          <Tabs
              selectedKey={chosenReportType}
              onSelectionChange={setChosenReportType}
              size="lg"
              color="primary"
              variant="underlined"
              classNames={{tab: "py-8"}}
          >
            <Tab key="users" titleValue={labels.admin.users} title={(
              <div className="bg-red-400 dark:bg-red-500 px-2 py-1.5 rounded-xl text-black">
                {labels.admin.users}
              </div>
            )}/>
            <Tab key="needs" titleValue={labels.activities.needs} title={(
              <div className="bg-need dark:bg-need-dark px-2 py-1.5 rounded-xl text-black">
                {labels.activities.needs}
              </div>
            )}/>
            <Tab key="resources" titleValue={labels.activities.resources} title={(
              <div className="bg-resource dark:bg-resource-dark px-2 py-1.5 rounded-xl text-black">
                {labels.activities.resources}
              </div>
            )}/>
            <Tab key="events" titleValue={labels.activities.events} title={(
              <div className="bg-event dark:bg-event-dark px-2 py-1.5 rounded-xl text-black">
                {labels.activities.events}
              </div>
            )}/>
            <Tab key="actions" titleValue={labels.activities.actions} title={(
              <div className="bg-action dark:bg-action-dark px-2 py-1.5 rounded-xl text-white">
                {labels.activities.actions}
              </div>
            )}/>
          </Tabs>
        </div>
        <Table aria-label="Reports">
          <TableHeader>
            <TableColumn key="reporter">{labels.admin.reporter}</TableColumn>
            <TableColumn key="reported">{labels.admin.reported}</TableColumn>
            <TableColumn key="description">{labels.admin.description}</TableColumn>
            <TableColumn key="actions">{labels.admin.actions}</TableColumn>
          </TableHeader>
          <TableBody items={reports[chosenReportType]}>
            {(item) => (
              <TableRow key={item.key}>
                {(columnKey) => <TableCell>{getKeyValue(item, columnKey)}</TableCell>}
              </TableRow>
            )}
          </TableBody>
        </Table>
      </div>
      <div class="my-10">
        <h3 class="object-top text-center text-xl mb-3"> {labels.admin.banned_users} </h3>
        <Table aria-label="Engellenmiş Kullanıcı Listesi">
          <TableHeader>
            <TableColumn key="username">{labels.profile.username}</TableColumn>
            <TableColumn key="actions">{labels.admin.actions}</TableColumn>
          </TableHeader>
          <TableBody items={bannedList}>
            {(item) => (
              <TableRow key={item.key}>
                {(columnKey) => <TableCell>{getKeyValue(item, columnKey)}</TableCell>}
              </TableRow>
            )}
          </TableBody>
        </Table>
      </div>
      <ToastContainer />
    </main>
  );
}
AdminPanel.getLayout = function getLayout(page) {
  return <MainLayout>{page}</MainLayout>;
};

export const getServerSideProps = withIronSessionSsr(
  async function getServerSideProps({ req }) {

    let user;
    const labels = await getLabels(req.session.language);

    try {
      user = req.session.user;
    } catch (error) {
      console.log('Error @ admin-panel/index.js: ', error);
      return { notFound: true };
    }

    if (!user?.accessToken) {
      console.log("A guest is trying to view admin panel");
      return { props: { unauthorized: true, labels}};
    }

    let allReports;

    try {
      ({ data: { reports: allReports } } = await api.get('/api/reports/', {
        headers: {
          'Authorization': `Bearer ${user.accessToken}` 
        }
      }));
    } catch (AxiosError) {
      console.log("Either the token expired or the user is not an admin");
      console.log(AxiosError);
      return { props: { unauthorized: true, labels}};
    }

    const userList = {};
    const bannedList = [{"key": 0, ...["a", "b"]}];
    return {"props": {
      allReports,
      userList,
      bannedList,
      labels
    }};
  },
  sessionConfig
)