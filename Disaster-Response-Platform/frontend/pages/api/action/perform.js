import needService from "@/services/needService"
import sessionConfig from '@/lib/sessionConfig';
import { withIronSessionApiRoute } from 'iron-session/next';
import actionService from "@/services/actionService";

export default withIronSessionApiRoute(actionRoute, sessionConfig);

  async function actionRoute(req, res) {
    try {
      const result = await actionService.perform(req.query.id,req.body, req.session.user.accessToken)
      console.log(result)
      const doAction = await actionService.doAction(result.payload.data.action_id, req.session.user.accessToken)
      console.log(doAction ,"doAction")
      res.status(result.status).json(result.payload);
    } catch (error) {
      console.log('error', error);
      res.status(error?.response?.status ?? 403).json({...error.data });
    }
  }
