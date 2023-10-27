import { api } from '@/lib/apiUtils';
import sessionConfig from '@/lib/sessionConfig';

import { withIronSessionApiRoute } from 'iron-session/next';

export default withIronSessionApiRoute(
  async function loginRoute(req, res) {
    try {
      const { username, password } = req.body;
      const { data } = await api.post('/register', { body: { username, password } });
    if(data?.payload?.success){
      res.status(200).json(data);
    }else{
      res.status(400).json(data);
    }
    } catch (error) {
      console.log('error', error);
      res.status(error?.response?.status ?? 403).json({ error });
    }
  },
  sessionConfig
);
