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
    const {url, skill} = req.body;

    try {
        await api.post(`/api/profiles/${url}`, skill, {
            headers: {
                'Authorization': `Bearer ${user.accessToken}`,
                'Content-Type': 'application/json',
            }
        });
    } catch (error) {
        console.log('Error in delete-skill.js: ', error);
        res.status(error?.response?.status ?? 403).json({});
    }
    res.status(200).json({});
}