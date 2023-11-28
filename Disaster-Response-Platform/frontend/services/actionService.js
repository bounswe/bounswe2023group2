import { api } from "@/lib/apiUtils";


const actionService = {
  needList: async (params) => {
    // const accessToken = getToken('accessToken');
    try {
      const data = await api.get(`api/actions/`, { params, headers: { Authorization: `Bearer ${accessToken}` , "Content-Type": "application/json" } });
      return Promise.resolve(data?.data);
    } catch (error) {
      return Promise.reject(error);
    }
  },
  show: async (id) => {
    // const accessToken = getToken('accessToken');
    try {
      const data = await api.get(`/api/actions/${id}`, { headers: { Authorization: `Bearer ${accessToken}`, 'Content-Type': 'application/json' } });
      return { status:200, payload: {data: data?.data , message: 'Success'} };
    } catch (error) {
      return { status:400 , message: 'Error' };
    }
  },
  createAction: async (body, accessToken) => {
   
    try {
      console.log(body)
      const data = await api.post(`api/actions/`, body, {
         headers: { Authorization: `Bearer ${accessToken}`,
         'Content-Type': 'application/json' },
         
      });
      return { status:200, payload: {data: data?.data , message: 'Success'} };
    } catch (error) {
      
      return { status:400 , message: 'Error' };
    }
  },
  deleteAction: async (id, body, accessToken) => {
    try {
      const data = await api.delete(`/api/actions/${id}`, { headers: { Authorization: `Bearer ${accessToken}`, "Content-Type": 'application/json' } });
      return Promise.resolve(data?.data);
    } catch (error) {
      return Promise.reject(error);
    }
  },
};

export default actionService;