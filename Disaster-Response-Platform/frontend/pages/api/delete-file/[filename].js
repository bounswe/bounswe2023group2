import sessionConfig from "@/lib/sessionConfig";
import { withIronSessionApiRoute } from "iron-session/next";
import { api } from '@/lib/apiUtils';

export default withIronSessionApiRoute(deleteFileRoute, sessionConfig);

async function deleteFileRoute(req, res) {
    const user = req?.session?.user;
    if (!user) {
        res.status(401).json({});
        return;
    }

    try {
        await api.delete(`/api/deletefile/${req.query.filename}`, {
            headers: {
                'Authorization': `Bearer ${user.accessToken}`,
                'Content-Type': 'application/json',
            }
        });
    } catch (error) {
        console.log('Error in delete-file/[filename].js: ', error);
        res.status(error?.response?.status ?? 403).json(error.response);
    }
    res.status(200).json({});
}