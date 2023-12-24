import MainLayout from '@/layouts/MainLayout';
import { withIronSessionSsr } from 'iron-session/next';
import sessionConfig from '@/lib/sessionConfig';
import { Button, Table, TableHeader, TableColumn, TableBody, TableRow, TableCell, getKeyValue } from "@nextui-org/react";
import { useState, useEffect } from "react";
import { useRouter } from 'next/navigation';
import { api } from '@/lib/apiUtils';
import getLabels from '@/lib/getLabels';

export default function AdminPanel({ unauthorized, allReports, userList, bannedList, labels }) {
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

  function reject(report_id) {

  }

  function accept(report_id) {

  }

  const rejectButton = report_id => <Button onPress={() => reject(report_id)} className="mx-1 block text-white bg-gray-400 hover:bg-gray-500 focus:ring-4 focus:outline-none focus:ring-gray-200 rounded-lg px-2 text-center dark:bg-gray-300 dark:hover:bg-gray-400 dark:focus:ring-gray-600"> {labels.admin.reject} </Button>;
  const acceptButton = report_id => <Button onPress={() => accept(report_id)} className="mx-1 block text-white bg-red-600 hover:bg-red-700 focus:ring-4 focus:outline-none focus:ring-red-200 rounded-lg px-2 text-center dark:bg-red-500 dark:hover:bg-red-600 dark:focus:ring-red-700"> {labels.admin.accept} </Button>;
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
                {"key": _id, "reporter": <Link href={`user/${reporter}`}>{reporter}</Link>, description, "reported": <Link href={`user/${reported}`}>{reported}</Link>, "actions": <div class="flex">{rejectButton(_id)}{acceptButton(_id)}</div>}
              )
            )
    );
  }

  return (
    <main>
      <div class="my-10">
        <h3 class="object-top text-center text-xl mb-3"> {labels.admin.user_reports} </h3>
        <Table aria-label="Reports">
          <TableHeader>
            <TableColumn key="reporter">{labels.admin.reporter}</TableColumn>
            <TableColumn key="reported">{labels.admin.reported}</TableColumn>
            <TableColumn key="description">{labels.admin.description}</TableColumn>
            <TableColumn key="actions">{labels.admin.actions}</TableColumn>
          </TableHeader>
          <TableBody items={reports["users"]}>
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