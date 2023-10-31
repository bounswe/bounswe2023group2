import sessionConfig from "@/lib/sessionConfig";
import { withIronSessionApiRoute } from "iron-session/next";


export default withIronSessionApiRoute(logoutRoute, sessionConfig);

async function logoutRoute(req, res) {
  req.session.destroy();
  res.json({ isLoggedIn: false, login: ""  });
}