import { api } from '@/lib/apiUtils';
import sessionConfig from '@/lib/sessionConfig';
import { withIronSessionApiRoute } from 'iron-session/next';


 export default  async function ResourceRoute(req, res) {
    try {
      
      const { data } = await api.get('/api/resource/');
      res.status(200).json(data);
    } catch (error) {
      console.log('error', error);
      res.status(error?.response?.status ?? 403).json({...error.data });
    }
  }



