import needService from "@/services/needService"
import sessionConfig from '@/lib/sessionConfig';
import { withIronSessionApiRoute } from 'iron-session/next';

export default withIronSessionApiRoute(needRoute, sessionConfig);

  async function needRoute(req, res) {
    try {
      const result = await needService.createNeed(req.body, req.session.user.accessToken)
      if(result.status === 201 || result.status === 200)
        res.status(result.status).json(result.payload);
      else{
        res.status(result.status).json(result.payload);
      }
    } catch (error) {
      console.log(error);
      res.status(error?.response?.status ?? 403).json({...error.data });
    }
  }

