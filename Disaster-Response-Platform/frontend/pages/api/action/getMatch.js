import needService from "@/services/needService"
import sessionConfig from '@/lib/sessionConfig';
import { withIronSessionApiRoute } from 'iron-session/next';
import actionService from "@/services/actionService";

export default withIronSessionApiRoute(actionRoute, sessionConfig);

  async function actionRoute(req, res) {
    try {
      console.log(req.query.id, req.body)
      const result = await actionService.getMatchlist(req.query.id,  req.session.user.accessToken)
      console.log(result)
      res.status(result.status).json(result.payload);
    } catch (error) {
      console.log('error', error);
      res.status(error?.response?.status ?? 403).json({...error.data });
    }
  }
