import { api } from "@/lib/apiUtils";
import { getToken } from "@/lib/localStorageUtils";

const downloadService = {
  download: async (body, filter, accessToken) => {
    // const accessToken = getToken('accessToken');
    try {
      const data = await api.post(
        `api/downloadfile/?${filter}`,
        {},
        {
          headers: {
            Authorization: `Bearer ${accessToken}`,
            "Content-Type": "application/json",
          },
        }
      );
      console.log(data, "dkfgjdflkgjdfklgjf");
      if (data.status === 200) {
        return { status: 200, payload: data?.data };
      }
      if (data.status === 422) {
        return { status: 422, message: "Request fields error" };
      }
      return { status: 500, message: "Internal" };
    } catch (error) {
      return { status: 500, message: "Frontend error" };
    }
  },
};

export default downloadService;
