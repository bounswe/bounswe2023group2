import { api } from "@/lib/apiUtils";
import { getToken } from "@/lib/localStorageUtils";

const downloadService = {
  download: async (body,filter, accessToken) => {
    // const accessToken = getToken('accessToken');
    try {
        let my_filter = new URLSearchParams(filter).toString();
        const data = await api.post(`api/downloadfile/?${my_filter}`, {
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