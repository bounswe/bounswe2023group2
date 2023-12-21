import { api } from '@/lib/apiUtils';

export default async function GetEventRoute(req, res) {
    try {
      const { data } = await api.get('/api/events/');
      res.status(200).json(data);
    } catch (error) {
      console.error("error @ api/event/get.js", error);
      res.status(error?.response?.status ?? 403).json({...error.data });
    }
}
