import { api } from '@/lib/apiUtils';
import sessionConfig from '@/lib/sessionConfig';
import { withIronSessionApiRoute } from 'iron-session/next';

export default withIronSessionApiRoute(ResourceRoute, sessionConfig);

async function ResourceRoute(req, res) {
    try {
      const {x,y} = req.query;
      const { data } = await api.get('/api/resources/');
      res.status(200).json(data);
    } catch (error) {
      console.error('error', error);
      res.status(error?.response?.status ?? 403).json({...error.data });
    }
}



