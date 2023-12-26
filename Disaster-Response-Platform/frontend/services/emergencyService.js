import { api } from "@/lib/apiUtils";

const emergencyService = {
  getAll: async (adress) => {
    try {
      const result = await api.get(`/api/emergencies/`);
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
  XYToAddress: async (address) => {
    try {
      const result = await api.get(`/api/geocode/coordinates_to_address?latitude=${address.x}&longitude=${address.y}`);
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

  
};

export default emergencyService;