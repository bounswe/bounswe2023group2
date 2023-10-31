import { api } from '@/lib/apiUtils';
import sessionConfig from '@/lib/sessionConfig';
import { withIronSessionApiRoute } from 'iron-session/next';

export default withIronSessionApiRoute(loginRoute, sessionConfig);

  async function loginRoute(req, res) {
    try {
      const { username, password } = JSON.parse(req.body);
      const { data } = await api.post('/api/users/login', { "username_or_email_or_phone": username, password });
      req.session.user = {
        accessToken: data?.access_token,
        refreshToken: data?.refresh_token,
        languages: 'tr'
      };
      await req.session.save();
      res.status(200).json(data);
    } catch (error) {
      console.log('error', error);
      res.status(error?.response?.status ?? 403).json({ error });
    }
  }



