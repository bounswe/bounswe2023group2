import sessionConfig from "@/lib/sessionConfig";
import { withIronSessionApiRoute } from "iron-session/next";
import { api } from '@/lib/apiUtils';

export default withIronSessionApiRoute(addProficiencyRoute, sessionConfig);

async function addProficiencyRoute(req, res) {
    const user = req?.session?.user;
    if (!user) {
        res.status(401).json({});
        return;
    }
    
    try {
        await api.post(`/api/userroles/proficiency-request`, req.body, {
            headers: {
                'Authorization': `Bearer ${user.accessToken}` 
            }
        });
    } catch (error) {
        console.log('Error in add-proficiency.js: ', error);
        res.status(error?.response?.status ?? 403).json({});
    }
    res.status(200).json({});
}