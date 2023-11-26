import MainLayout from '@/layouts/MainLayout';
import { withIronSessionSsr } from 'iron-session/next';
import sessionConfig from '@/lib/sessionConfig';

export default function AdminPanel({ }) {
    return (<>
        This will be an admin page with:
        <ul>
            <li>two report tables (one for activities, one for users)</li>
            <li>a way to search non-banned users and give/take credibility from them or ban them</li>
            <li>a table of banned users and a button to unban them</li>
        </ul>
    </>);
}
AdminPanel.getLayout = function getLayout(page) {
    return <MainLayout>{page}</MainLayout>;
};

export const getServerSideProps = withIronSessionSsr(
    async function getServerSideProps({ req }) {
        return {"props": {

        }}
    },
    sessionConfig
)