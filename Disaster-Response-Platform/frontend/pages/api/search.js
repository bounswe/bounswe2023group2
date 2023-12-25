import { api } from '@/lib/apiUtils';
import sessionConfig from '@/lib/sessionConfig';
import { withIronSessionApiRoute } from 'iron-session/next';

export default withIronSessionApiRoute(searchRoute, sessionConfig);

  async function searchRoute(req, res) {
    try {
     
      const result = await api.get(`/api/search/${req.body.activityType}s/${req.body.query}`,
      {headers: {
        'Authorization': `Bearer ${req.session.user.accessToken}`,
        "Content-Type": "application/json"
      }});
      if(result.status === 200){
        res.status(200).json(result?.data);
      }
      else if(result.status === 403){
        res.status(403).json(response.data);
      }
      if(result.status === 422){
        res.status(422).json({message: 'Request fields error' });
      }

    } catch (error) {
      res.status(500).json({message: 'Credential error' });
    }
  }



