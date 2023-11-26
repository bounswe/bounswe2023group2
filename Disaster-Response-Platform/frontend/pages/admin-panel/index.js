import MainLayout from '@/layouts/MainLayout';
import { withIronSessionSsr } from 'iron-session/next';
import sessionConfig from '@/lib/sessionConfig';
import { Button, Table, TableHeader, TableColumn, TableBody, TableRow, TableCell, getKeyValue } from "@nextui-org/react";
import { useEffect } from "react";
import { useRouter } from 'next/navigation';
import { api } from '@/lib/apiUtils';

export default function AdminPanel({ unauthorized, reports, userList, bannedList }) {
  const router = useRouter();
  if (unauthorized) {
    useEffect(() => {setTimeout(() => {router.push("/")}, 2000)});
    return (
      <div class="text-center text-xl">
        <br /><br /><br />
        Admin paneline erişiminiz yok.
      </div>
    );
  }

  function dismiss(report_id) {

  }
  function ban(report_id, username) {

  }
  function remove(report_id, activity_id) {

  }

  const dismissButton = report_id => <Button onPress={() => dismiss(report_id)} className="mx-1 block text-white bg-gray-400 hover:bg-gray-500 focus:ring-4 focus:outline-none focus:ring-gray-200 rounded-lg px-2 text-center dark:bg-gray-300 dark:hover:bg-gray-400 dark:focus:ring-gray-600">Raporu reddet</Button>;
  const banButton = (report_id, username) => <Button onPress={() => ban(report_id, username)} className="mx-1 block text-white bg-red-600 hover:bg-red-700 focus:ring-4 focus:outline-none focus:ring-red-200 rounded-lg px-2 text-center dark:bg-red-500 dark:hover:bg-red-600 dark:focus:ring-red-700">Kullanıcıyı engelle</Button>;
  const removeButton = (report_id, activity_id) => <Button onPress={() => remove(report_id, activity_id)} className="mx-1 block text-white bg-red-600 hover:bg-red-700 focus:ring-4 focus:outline-none focus:ring-red-200 rounded-lg px-2 text-center dark:bg-red-500 dark:hover:bg-red-600 dark:focus:ring-red-700">Aktiviteyi kaldır</Button>;

  const userReports = (reports.filter(elem => elem.type === "user")
                              .map(
                                (
                                  {created_by: reporter, description, type, _id, "details": {reported_user_id: reported}}
                                ) => (
                                  {"key": _id, reporter, description, reported, "actions": <div class="flex">{banButton(_id, reported)}{dismissButton(_id)}</div>}
                                )
                              )
  );

  const activityReports = (reports.filter(elem => elem.type === "activity")
                                  .map(
                                    (
                                      {created_by: reporter, description, type, _id, "details": {reported_activity_id: reported}}
                                    ) => (
                                      {"key": _id, reporter, description, reported, "actions": <div class="flex">{removeButton(_id, reported)}{dismissButton(_id)}</div>}
                                    )
                                  )
  );

  return (
    <main>
      <div class="my-10">
        <h3 class="object-top text-center text-xl mb-3"> Kullanıcı Raporları </h3>
        <Table aria-label="Kullanıcı Raporları">
          <TableHeader>
            <TableColumn key="reporter">Raporlayan</TableColumn>
            <TableColumn key="reported">Raporlanan</TableColumn>
            <TableColumn key="description">Açıklama</TableColumn>
            <TableColumn key="actions">Aksiyonlar</TableColumn>
          </TableHeader>
          <TableBody items={userReports}>
            {(item) => (
              <TableRow key={item.key}>
                {(columnKey) => <TableCell>{getKeyValue(item, columnKey)}</TableCell>}
              </TableRow>
            )}
          </TableBody>
        </Table>
      </div>
      <div class="my-10">
        <h3 class="object-top text-center text-xl mb-3"> Aktivite Raporları </h3>
        <Table aria-label="Aktivite Raporları">
          <TableHeader>
            <TableColumn key={0}>Raporlayan</TableColumn>
            <TableColumn key={1}>Raporlanan</TableColumn>
            <TableColumn key={2}>Detaylar</TableColumn>
            <TableColumn key={3}>Aksiyonlar</TableColumn>
          </TableHeader>
          <TableBody items={activityReports}>
            {(item) => (
              <TableRow key={item.key}>
                {(columnKey) => <TableCell>{getKeyValue(item, columnKey)}</TableCell>}
              </TableRow>
            )}
          </TableBody>
        </Table>
      </div>
      <div class="my-10">
        <h3 class="object-top text-center text-xl mb-3"> Engellenmiş Kullanıcı Listesi </h3>
        <Table aria-label="Engellenmiş Kullanıcı Listesi">
          <TableHeader>
            <TableColumn key={0}>Kullanıcı ismi</TableColumn>
            <TableColumn key={1}>Aksiyonlar</TableColumn>
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

    try {
      user = req.session.user;
    } catch (error) {
      console.log('Error @ admin-panel/index.js: ', error);
      return { notFound: true };
    }

    if (!user?.accessToken) {
      console.log("A guest is trying to view own admin panel");
      return { props: { unauthorized: true } };
    }

    let reports;

    try {
      ({ data: { reports: reports } } = await api.get('/api/reports/', {
        headers: {
          'Authorization': `Bearer ${user.accessToken}` 
        }
      }));
    } catch (AxiosError) {
      console.log("A token expired");
      console.log(AxiosError);
      return { props: { unauthorized: true } };
    }

    const userList = {};
    const bannedList = [{"key": 0, ...["a", "b"]}];
    return {"props": {
      reports,
      userList,
      bannedList
    }};
  },
  sessionConfig
)