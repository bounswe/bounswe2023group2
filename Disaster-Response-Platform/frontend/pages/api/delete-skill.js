import sessionConfig from "@/lib/sessionConfig";
import { withIronSessionApiRoute } from "iron-session/next";
import { api } from '@/lib/apiUtils';

export default withIronSessionApiRoute(deleteSkillRoute, sessionConfig);

async function deleteSkillRoute(req, res) {
    const user = req?.session?.user;
    if (!user) {
        res.status(401).json({});
        return;
    }
    await api.delete(`/api/profiles/${topic.api_url}${topic.delete}`, req.body, {
        headers: {
            'Authorization': `Bearer ${user.accessToken}` 
        }
    });
    res.status(200).json({});
}