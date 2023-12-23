import sessionConfig from "@/lib/sessionConfig";
import { withIronSessionApiRoute } from "iron-session/next";
import { api } from '@/lib/apiUtils';

export default withIronSessionApiRoute(addEventRoute, sessionConfig);

async function addEventRoute(req, res) {
    const user = req?.session?.user;
    if (!user) {
        res.status(401).json({});
        return;
    }
    
    try {
        await api.post(`/api/events`, req.body, {
            headers: {
                'Authorization': `Bearer ${user.accessToken}` 
            }
        });
    } catch (error) {
        console.log('Error in event/add.js: ', error);
        res.status(error?.response?.status ?? 403).json({});
    }
    res.status(200).json({});
}