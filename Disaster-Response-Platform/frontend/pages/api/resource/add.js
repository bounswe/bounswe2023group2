import needService from "@/services/needService"
import sessionConfig from '@/lib/sessionConfig';
import { withIronSessionApiRoute } from 'iron-session/next';
import resourceService from "@/services/resourceService";

export default withIronSessionApiRoute(resourceRoute, sessionConfig);

  async function resourceRoute(req, res) {
    try {
      const result = await resourceService.createNeed(req.body, req.session.user.accessToken)
      res.status(result.status).json(result.payload);
    } catch (error) {
      console.log('error', error);
      res.status(error?.response?.status ?? 403).json({...error.data });
    }
  }
