import MainLayout from '@/layouts/MainLayout';
import { withIronSessionSsr } from 'iron-session/next';
import sessionConfig from '@/lib/sessionConfig';
import {Table, TableHeader, TableColumn, TableBody, TableRow, TableCell, getKeyValue} from "@nextui-org/react";

export default function AdminPanel({ userReports, activityReports, userList, bannedList }) {
    return (
        <main>
            <div class="my-10">
                <h3 class="object-top text-center text-xl mb-3"> Kullanıcı Raporları </h3>
                <Table aria-label="">
                    <TableHeader>
                        <TableColumn key={0}>Raporlayan</TableColumn>
                        <TableColumn key={1}>Raporlanan</TableColumn>
                        <TableColumn key={2}>Detaylar</TableColumn>
                        <TableColumn key={3}>Aksiyonlar</TableColumn>
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
                <Table aria-label="">
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
                <Table aria-label="">
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
        const userReports = [{"key": 0, ...["a", "b", "c", "d"]}];
        const activityReports = [{"key": 0, ...["a", "b", "c", "e"]}];
        const userList = {};
        const bannedList = [{"key": 0, ...["a", "b"]}];
        return {"props": {
            userReports,
            activityReports,
            userList,
            bannedList
        }}
    },
    sessionConfig
)