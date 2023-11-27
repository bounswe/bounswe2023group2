import sessionConfig from "@/lib/sessionConfig";
import { withIronSessionApiRoute } from "iron-session/next";
import { api } from '@/lib/apiUtils';

export default withIronSessionApiRoute(reportRoute, sessionConfig);

async function reportRoute(req, res) {
  const user = req?.session?.user;
  if (!user) {
    res.status(401).json({});
    return;
  }

  const body = {
    "description": req.body.reason,
    "type": "user",
    "details": {
      "reported_user_id": req.body.reported
    }
  };
  
  try {
    await api.post(`/api/reports`, body, {
      headers: {
        'Authorization': `Bearer ${user.accessToken}` 
      }
    });
  } catch (error) {
    console.log('Error in report-user.js: ', error);
    res.status(error?.response?.status ?? 403).json({});
  }
  res.status(200).json({});
}