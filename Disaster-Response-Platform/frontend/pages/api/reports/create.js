import sessionConfig from "@/lib/sessionConfig";
import { withIronSessionApiRoute } from "iron-session/next";
import { api } from '@/lib/apiUtils';

export default withIronSessionApiRoute(reportRoute, sessionConfig);

async function reportRoute(req, res) {

  if (req.method === "POST") {
    const user = req?.session?.user;
    if (!user) {
      res.status(401).json({});
      return;
    }

    /*
      Fields:
      "description": Description given in form
      "report_type": What kind of thing is being reported - one of 'users', 'needs', 'resources', 'actions', 'events'
      "report_type_id": The ID of the thing being reported
      "details": Currently unused but cannot be sent as an empty object
      "status": Whether the report is accepted or rejected, must be set "undefined" during creation
    */

    let body = {
      details: {"date": new Date().toISOString()},
      ...req.body,
      status: "undefined"
    }
    
    try {
      await api.post(`/api/reports`, body, {
        headers: {
          'Authorization': `Bearer ${user.accessToken}` 
        }
      });
    } catch (error) {
      console.log('Error in reports/create.js: ', error);
      res.status(error?.response?.status ?? 403).json(error.response);
    }
    res.status(200).json({});
  }
}