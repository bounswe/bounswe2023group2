import needService from "@/services/needService"
import sessionConfig from '@/lib/sessionConfig';
import { withIronSessionApiRoute } from 'iron-session/next';

export default withIronSessionApiRoute(download, sessionConfig);

  async function download(req, res) {
    try{
      let my_filter = new URLSearchParams(filters).toString();
      response = await api.post(`/api/donwloadfile/?activity_type=Need`, {
        headers: { Authorization: `Bearer ${accessToken}`,
        'Content-Type': 'application/json' },
      });
      console.log(response)
      response = await api.post(`/api/donwloadfile/?activity_type=Resource`, {
        headers: { Authorization: `Bearer ${accessToken}`,
        'Content-Type': 'application/json' },
      });
      console.log(response)

    }
    catch (error) {
      // Handle unexpected errors
      console.error("Error:", error);
    }
  }
