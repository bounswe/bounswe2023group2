import sessionConfig from "@/lib/sessionConfig";
import { withIronSessionApiRoute } from "iron-session/next";
import { api } from '@/lib/apiUtils';

export default withIronSessionApiRoute(unauthorizeRoute, sessionConfig);

async function unauthorizeRoute(req, res) {
    const user = req?.session?.user;
    if (!user) {
        res.status(401).json({});
        return;
    }
    
    try {
        await api.put(`/api/users/unauthorize`, req.body, {
            headers: {
                'Authorization': `Bearer ${user.accessToken}` 
            }
        });
    } catch (error) {
        console.log('Error in unauthorize.js: ', error);
        res.status(error?.response?.status ?? 403).json(error.response);
    }
    res.status(200).json({});
}