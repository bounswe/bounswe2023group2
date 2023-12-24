import sessionConfig from "@/lib/sessionConfig";
import { withIronSessionApiRoute } from "iron-session/next";
import { api } from '@/lib/apiUtils';

export default withIronSessionApiRoute(deleteUserRoute, sessionConfig);

async function deleteUserRoute(req, res) {
    const user = req?.session?.user;
    if (!user) {
        res.status(401).json({});
        return;
    }

    try {
        await api.delete(`/api/users`, {
            headers: {
                'Authorization': `Bearer ${user.accessToken}`
            }
        });
    } catch (error) {
        console.log('Error in delete-user.js: ', error);
        res.status(error?.response?.status ?? 403).json(error.response);
    }
    res.status(200).json({});
}