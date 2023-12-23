import { api } from "@/lib/apiUtils";
import { getToken } from "@/lib/localStorageUtils";

const resourceService = {
  needList: async (params) => {
    // const accessToken = getToken('accessToken');
    try {
      const data = await api.get(`api/resources/`, { params, headers: { Authorization: `Bearer ${accessToken}` } });
      return Promise.resolve(data?.data);
    } catch (error) {
      return Promise.reject(error);
    }
  },
  show: async (id) => {
    // const accessToken = getToken('accessToken');
    try {
      const data = await api.get(`/api/resources/${id}`, { headers: { Authorization: `Bearer ${accessToken}`,  'Content-Type': 'application/json' } });
      return { status:200, payload: {data: data?.data , message: 'Success'} };
    } catch (error) {
      return { status:400 , message: 'Error' };
    }
  },
  createNeed: async (body, accessToken) => {
   
    try {
      console.log(body)
      const data = await api.post(`api/resources/`, body, {
         headers: { Authorization: `Bearer ${accessToken}`,
         'Content-Type': 'application/json' },
         
      });
      return { status:200, payload: {data: data?.data , message: 'Success'} };
    } catch (error) {
      
      return { status:400 , message: 'Error' };
    }
  },
  deleteNeed: async (id, body) => {
    try {
      // const accessToken = localStorage.getItem('accessToken');
      const data = await api.delete(`/api/resources/${id}`, { headers: { Authorization: `Bearer ${accessToken}` } });
      return Promise.resolve(data?.data);
    } catch (error) {
      return Promise.reject(error);
    }
  },
};

export default resourceService;