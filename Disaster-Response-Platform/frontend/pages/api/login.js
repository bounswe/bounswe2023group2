import { api } from '@/lib/apiUtils';
import sessionConfig from '@/lib/sessionConfig';

import { withIronSessionApiRoute } from 'iron-session/next';

export default withIronSessionApiRoute(loginRoute, sessionConfig);

  async function loginRoute(req, res) {
    try {
      const user = { isLoggedIn: true, ...req.body, };
      req.session.user = user;
      await req.session.save();
      const { data } = await api.post('/api/users/login', req.body );
      // req.session.user = {
      //   accessToken: data?.payload?.tokens?.accessToken,
      //   refreshToken: data?.payload?.tokens?.refreshToken,
      //   languages: 'tr'
      // };
      // await req.session.save();
      res.status(200).json();
    } catch (error) {
      console.log('error', error);
      res.status(error?.response?.status ?? 403).json({ error });
    }
  }



