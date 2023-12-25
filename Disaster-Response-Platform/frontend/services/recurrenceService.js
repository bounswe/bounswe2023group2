import { api } from "@/lib/apiUtils";

const recurrenceService = {
  create: async (data, accessToken) => {
    try {
      const result = await api.post(`/api/recurrence/`, data,
        {
          headers: {
            Authorization: `Bearer ${accessToken}`,
            'Content-Type': 'application/json'
          }
        });
      if (result.status === 200) {
        return { status: 200, payload: result?.data };
      }
      if (result.status === 422) {
        return { status: 422, message: 'Request fields error' };
      }
      return { status: 500, message: 'Internal' };

    } catch (error) {
      return { status: 500, message: 'Frontend error' };
    }
  },
  list: async () => {
    try{
      const result = await api.get(`/api/recurrence/`,
        {
          headers: {
            'Content-Type': 'application/json'
          }
        });
      if (result.status === 200) {
        return { status: 200, payload: result?.data };
      }
      if (result.status === 422) {
        return { status: 422, message: 'Request fields error' };
      }
      return { status: 500, message: 'Internal' };
    }catch(error){
      return { status: 500, message: 'Frontend error' };
    }
  },
  start: async (id) => {
    try{
      const result = await api.get(`/api/recurrence/start/${id}`,
        {
          headers: {
            'Content-Type': 'application/json'
          }
        });
      if (result.status === 200) {
        return { status: 200, payload: result?.data };
      }
      if (result.status === 422) {
        return { status: 422, message: 'Request fields error' };
      }
      return { status: 500, message: 'Internal' };
    }catch(error){
      return { status: 500, message: 'Frontend error' };
    }
  },
  attach: async (data, accessToken) => {
    try{
      const result = await api.post(`/api/recurrence/attach_activity/`, data,
        {
          headers: {
            'Content-Type': 'application/json',
            Authorization: `Bearer ${accessToken}`,
          }
        });
      console.log(result)
      if (result.status === 200) {
        return { status: 200, payload: result?.data };
      }
      if (result.status === 422) {
        return { status: 422, message: 'Request fields error' };
      }
      return { status: 500, message: 'Internal' };
    }catch(error){
      return { status: 500, message: 'Frontend error' };
    }
  },
};

export default recurrenceService;