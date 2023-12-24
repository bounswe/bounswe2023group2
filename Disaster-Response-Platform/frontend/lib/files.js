import { toast } from 'react-toastify';
import { api } from '@/lib/apiUtils';

/*
  Normally, this is better suited for an API route, as making the accessToken accessible to the client may be a security risk
  However, Next.js with the pages router doesn't implement req.formData() or req.json() from the fetch API, which are needed to parse requests
  I have tried alternatives like passing the requests unaltered,
  but I have not been able to make it work, getting errors like "Missing boundary in multipart."
  - Can Bora
*/


export async function uploadFile(file, filename, accessToken) {
  const renamed_file = new File([file], filename, {type: file.type});

  const body = new FormData();
  body.set("file", renamed_file);

  const response = await api.post("/api/uploadfile", body, {
      headers: {
        'Authorization': `Bearer ${accessToken}`,
        'Content-Type': 'multipart/form-data'
      }
  });

  if (response.status !== 200) {
    toast(`${labels.feedback.failure} (certificate upload: ${response.statusText})`);
  }

  return response;
}