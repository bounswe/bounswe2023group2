import sessionConfig from '@/lib/sessionConfig';
import api from 'api';
import { withIronSessionApiRoute } from 'iron-session/next';

export default withIronSessionApiRoute(
  async function LoginRoute(req, res) {
    try {
      const { email, password } = req.body;
      const { data } = await api.post('/login', { body: { email, password } });
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
