import downloadService from "@/services/downloadService";
import sessionConfig from "@/lib/sessionConfig";
import { withIronSessionApiRoute } from "iron-session/next";

export default withIronSessionApiRoute(downloadRoute, sessionConfig);

async function downloadRoute(req, res) {
  try {
    const { filter } = JSON.parse(req.body);
    const result = await downloadService.download(
      req.body,
      filter,
      req.session.user.accessToken
    );
    if (result.status === 201 || result.status === 200)
      res.status(result.status).json(result.payload);
    else {
      res.status(result.status).json(result.payload);
    }
  } catch (error) {
    console.log(error);
    res.status(error?.response?.status ?? 403).json({ ...error.data });
  }
}
