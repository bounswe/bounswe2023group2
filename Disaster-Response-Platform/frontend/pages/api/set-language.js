import { api } from '@/lib/apiUtils';
import sessionConfig from '@/lib/sessionConfig';
import { withIronSessionApiRoute } from 'iron-session/next';

export default withIronSessionApiRoute(setLanguageRoute, sessionConfig);

async function setLanguageRoute(req, res) {
  req.session.language = req.body.language;
  await req.session.save();
  res.status(200).json({});
}



