// pages/api/logout.ts

import sessionConfig from "@/lib/sessionConfig";
import { withIronSessionApiRoute } from "iron-session/next";

export default withIronSessionApiRoute(
  function logoutRoute(req, res) {
    req.session.destroy();
    res.send({ ok: true });
  },
  sessionConfig
);