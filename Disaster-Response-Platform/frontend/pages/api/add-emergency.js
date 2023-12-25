import sessionConfig from "@/lib/sessionConfig";
import { withIronSessionApiRoute } from "iron-session/next";
import { api } from '@/lib/apiUtils';

export default withIronSessionApiRoute(addEmergencyRoute, sessionConfig);

async function addEmergencyRoute(req, res) {
    const user = req?.session?.user;

    const headers = user ? {'Authorization': `Bearer ${user.accessToken}`} : {};

    try {
        await api.post(`/api/emergencies${user ? "" : "/anonymous"}`, req.body, {
            headers: headers
        });
    } catch (error) {
        console.log('Error in event/add-emergency.js: ', error);
        res.status(error?.response?.status ?? 403).json({});
    }
    res.status(200).json({});
}