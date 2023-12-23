import sessionConfig from "@/lib/sessionConfig";
import { withIronSessionApiRoute } from "iron-session/next";


export default withIronSessionApiRoute(logoutRoute, sessionConfig);

async function logoutRoute(req, res) {
  // Destroying the req.session cookie would also remove the preferred language
  // and we don't want the site changing languages for no reason
  delete req.session.user;
  await req.session.save();
  res.json({ isLoggedIn: false, login: ""  });
}