import sessionConfig from "@/lib/sessionConfig";
import { withIronSessionApiRoute } from "iron-session/next";
import { api } from '@/lib/apiUtils';

export default withIronSessionApiRoute(addSkillRoute, sessionConfig);

async function addSkillRoute(req, res) {
    const user = req?.session?.user;
    if (!user) {
        res.status(401).json({});
        return;
    }
    const {url, skill} = req.body;
    await api.post(`/api/profiles/${url}`, skill, {
        headers: {
            'Authorization': `Bearer ${user.accessToken}` 
        }
    });
    res.status(200).json({})
}