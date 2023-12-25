import sessionConfig from "@/lib/sessionConfig";
import { withIronSessionApiRoute } from "iron-session/next";
import { api } from '@/lib/apiUtils';

export default withIronSessionApiRoute(setCredibilityRoute, sessionConfig);

async function setCredibilityRoute(req, res) {
    const user = req?.session?.user;
    if (!user) {
        res.status(401).json({});
        return;
    }
    
    try {
        await api.put(`/api/users/${req.body.newValue ? "" : "un"}verify`, {"username": req.body.username}, {
            headers: {
                'Authorization': `Bearer ${user.accessToken}` 
            }
        });
    } catch (error) {
        console.log('Error in set-credibility.js: ', error);
        res.status(error?.response?.status ?? 403).json(error.response);
    }
    res.status(200).json({});
}