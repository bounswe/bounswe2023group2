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
    const {_id, voteType, vote} = req.body;
    
    try {
        await api.post(`/api/vote/`, {
            _id, voteType, vote
            
        }, {
            headers: {
                'Authorization': `Bearer ${user.accessToken}` 
            }
        });
    } catch (error) {
        console.log('Error in add-skill.js: ', error);
        res.status(error?.response?.status ?? 403).json({});
    }
    res.status(200).json({});
}