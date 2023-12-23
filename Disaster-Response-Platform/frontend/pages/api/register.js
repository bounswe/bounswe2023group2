import { api } from '@/lib/apiUtils';
import sessionConfig from '@/lib/sessionConfig';

import { withIronSessionApiRoute } from 'iron-session/next';

export default withIronSessionApiRoute(
 async function RegisterRoute(req, res) {
    try {

      let body = req.body
      if (body.phone_number == '') delete body.phone_number 
      if(body.email == "") delete body.email
      const response = await api.post('/api/users/signup',  body );
      if(response.status == 200 | response.status == 201)
        res.status(200).json(response.data);
      else
        res.status(response.status).json(response)

    } catch (error) {
      res.status(error?.response?.status ?? 400).json({ error });
    }
  },
  sessionConfig

)
