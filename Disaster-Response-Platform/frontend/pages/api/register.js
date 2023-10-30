import { api } from '@/lib/apiUtils';
import sessionConfig from '@/lib/sessionConfig';

import { withIronSessionApiRoute } from 'iron-session/next';

export default withIronSessionApiRoute(
 async function RegisterRoute(req, res) {
    try {

      const { data } = await api.post('/api/users/signup',  req.body );
      req.session.user = {
        accessToken: data?.payload?.tokens?.accessToken,
        refreshToken: data?.payload?.tokens?.refreshToken,
        languages: 'tr'
      };
      await req.session.save();

      res.status(200).json(data);
    } catch (error) {
      res.status(error?.response?.status ?? 400).json({ error });
    }
  },
  sessionConfig

)
