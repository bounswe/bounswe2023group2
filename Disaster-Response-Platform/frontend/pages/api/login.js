import { api } from '@/lib/apiUtils';
import sessionConfig from '@/lib/sessionConfig';

import { withIronSessionApiRoute } from 'iron-session/next';

export default withIronSessionApiRoute(
  async function loginRoute(req, res) {
    try {
      const { username, password } = req.body;
      const { data } = await api.post('/login', { body: { username, password } });
      req.session.user = {
        accessToken: data?.payload?.tokens?.accessToken,
        refreshToken: data?.payload?.tokens?.refreshToken,
        languages: 'tr'
      };
      await req.session.save();

      res.status(200).json(data);
    } catch (error) {
      console.log('error', error);
      res.status(error?.response?.status ?? 403).json({ error });
    }
  },
  sessionConfig
);
