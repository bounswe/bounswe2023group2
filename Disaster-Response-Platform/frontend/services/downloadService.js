import { api } from "@/lib/apiUtils";
import { getToken } from "@/lib/localStorageUtils";

const downloadService = {
  download: async (body,filter, accessToken) => {
    // const accessToken = getToken('accessToken');
    try {
        
        const data = await api.post(`api/downloadfile/?${filter}`, {
            headers: { Authorization: `Bearer ${accessToken}`,
            'Content-Type': 'application/json' },
         });
      return Promise.resolve(data?.data);
    } catch (error) {
      return Promise.reject(error);
    }
  }
};

export default downloadService;