import { api } from '@/lib/apiUtils';
import sessionConfig from '@/lib/sessionConfig';
import { table } from '@nextui-org/react';
import { withIronSessionApiRoute } from 'iron-session/next';

export default withIronSessionApiRoute(loginRoute, sessionConfig);

  async function loginRoute(req, res) {
    try {
      const { username, password } =req.body;
      const response = await api.post('/api/users/login', req.body);
      if(response.status == 200) {
        req.session.user = {
          accessToken: response.data?.access_token,
          refreshToken: response.data?.refresh_token,
          user_role: response.data?.user_role ?? "AUTHENTICATED",
          proficiency: response.data?.profenciency ?? [],
          languages: 'tr'
        };
        await req.session.save();
        res.status(200).json(response.data);
      }
      res.status(response.status).json(response.data?.ErrorMessage)

    } catch (error) {
     
      res.status(500).json({ error});
    }
  }



