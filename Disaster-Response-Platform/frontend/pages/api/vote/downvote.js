import sessionConfig from "@/lib/sessionConfig";
import { withIronSessionApiRoute } from "iron-session/next";
import { api } from '@/lib/apiUtils';

export default withIronSessionApiRoute(VoteRoute, sessionConfig);

async function VoteRoute(req, res) {
    const user = req?.session?.user;
    if (!user) {
        res.status(401).json({});
        return;
    }
    const {entityID, entityType} = req.body;
    
    try {

        await api.put(`/api/feedback/downvote`, {
              entityID, entityType
        }, {
            headers: {
                'Authorization': `Bearer ${user.accessToken}`,
                'Content-Type': 'application/json'
            }
        });
    } catch (error) {
        console.log('Error in ', error);
        res.status(error?.response?.status ?? 403).json({});
    }
    res.status(200).json({});
}