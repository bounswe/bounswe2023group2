import { api } from "@/lib/apiUtils";


const actionService = {

  show: async (id) => {
    // const accessToken = getToken('accessToken');
    try {
      const data = await api.get(`/api/actions/${id}`, { headers: { Authorization: `Bearer ${accessToken}`, 'Content-Type': 'application/json' } });
      return { status:200, payload: {data: data?.data , message: 'Success'} };
    } catch (error) {
      return { status:400 , message: 'Error' };
    }
  },
  needList: async (resource_id) => {
    try {
      const result = await api.get(`/api/actions/need_list/${resource_id}`);
      if(result.status === 200){
        return { status:200, payload: result?.data };
      }
      if(result.status === 422){
        return { status:422 , message: 'Request fields error' };
      }
      return { status:500 , message: 'Internal' };
     
    } catch (error) {
      return { status:500 , message: 'Frontend error' };
    }
  },
  resourceList: async (need_id) => {
    try {
      const result = await api.get(`/api/actions/resource_list/${need_id}`);
      if(result.status === 200){
        return { status:200, payload: result?.data };
      }
      if(result.status === 422){
        return { status:422 , message: 'Request fields error' };
      }
      return { status:500 , message: 'Internal' };
     
    } catch (error) {
      return { status:500 , message: 'Frontend error' };
    }
  },
  createAction: async (body, accessToken) => {
   
    try {
      console.log(body)
      const data = await api.post(`api/actions/`, body, {
         headers: { 
          Authorization: `Bearer ${accessToken}`,
         'Content-Type': 'application/json' 
        },
         
      });
      console.log(data)
      return { status:200, payload: {data: data?.data , message: 'Success'} };
    } catch (error) {
      console.log(error)
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
  perform: async (action_id,match, accessToken) => {
    try {
      
      const data = await api.post(`/api/actions/perform_action/${action_id}`, match,{
         headers: { 
          Authorization: `Bearer ${accessToken}`,
         'Content-Type': 'application/json' 
        },
         
      });
      console.log(data)
      return { status:200, payload: {data: data?.data , message: 'Success'} };
    } catch (error) {
      console.log(error)
      return { status:400 , message: 'Error' };
    }
  },
  getMatchlist: async (action_id, accessToken) => {
    try {
      const data = await api.get(`/api/actions/perform_action/${action_id}`, { headers: { Authorization: `Bearer ${accessToken}`, "Content-Type": 'application/json' } });
      console.log(data)
      return { status:200, payload: {data: data?.data , message: 'Success'} };
    } catch (error) {
      console.log(error)
      return { status:400 , message: 'Error' };
    }
  }
};

export default actionService;