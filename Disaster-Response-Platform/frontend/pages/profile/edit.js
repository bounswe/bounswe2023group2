import MainLayout from '@/layouts/MainLayout';
import GrayBox from '@/components/GrayBox';
import InputField from '@/components/InputField';
import { useRouter } from 'next/navigation';
import { api } from '@/lib/apiUtils';
import { withIronSessionSsr } from 'iron-session/next';
import sessionConfig from '@/lib/sessionConfig';
import { useState } from "react";

export default function Edit({ guest, expired, current_main_fields, current_optional_fields, accessToken }) {

  const router = useRouter();
  const [checked, setChecked] = useState(current_main_fields.private_account);

  async function onSubmit(event) {
    event.preventDefault();
    const form = new FormData(event.target);
    const formData = Object.fromEntries(form.entries());
    if (formData["email"] === "" && formData["phone_number"] === "") {
      alert("You must have a phone number or email!");
      return false;
    }
    let mainData = {}, optionalData = {};
    for (let [key, val] of Object.entries(formData)) {
      if (key === "private_account" || val === "" || val === current_main_fields[key] || val === current_optional_fields[key]) continue;
        if (["first_name", "last_name", "email", "phone_number"].includes(key)) {
          mainData[key] = val;
        } else {
          optionalData[key] = val;
        }
    }
    mainData.private_account = "private_account" in formData

    if (Object.keys(mainData).length > 0) {
      await api.put("/api/users/update-user", mainData, {
        headers: {
          'Authorization': `Bearer ${accessToken}`
        }
      });
    }

    if (Object.keys(optionalData).length > 0) {
      await api.post("/api/profiles/user-optional-infos/add-user-optional-info", optionalData, {
        headers: {
          'Authorization': `Bearer ${accessToken}`
        }
      });
    }

    router.push('/profile');
  }

  if (guest || expired) {
    useEffect(() => router.push("/login"));
    return (
      <div class="text-center text-xl">
        <br /><br /><br />
        Oturum açmaya yönlendiriliyorsunuz...
      </div>
    );
  }

  const main_tr = {
    "first_name": "Ad",
    "last_name": "Soyad",
    "email": "Email",
    "phone_number": "Telefon No"
  }

  const optional_tr = {
    "date_of_birth": "Doğum Tarihi",
    "nationality": "Ülke",
    "identity_number": "Kimlik No",
    "education": "Öğrenim",
    "health_condition": "Sağlık Durumu",
    "blood_type": "Kan Grubu",
    "Address": "Adres"
  }

  if ("date_of_birth" in current_optional_fields)
    current_optional_fields["date_of_birth"] = current_optional_fields["date_of_birth"].substring(0, 10);

  return (
    <main>
      <form onSubmit={onSubmit}>
        <div class="flex justify-around space-x-8">
          <GrayBox className="w-96">
            <h3 class="object-top text-center text-xl"> Ana Bilgiler </h3>
            {Object.entries(main_tr).map(([key, title]) => InputField({
                    'key': key,
                    'title': title,
                    'placeholder': key in current_main_fields ? current_main_fields[key] : "",
                    'type': (key === "phone_number") ? 'number' : 'text',
                    'required': false}))}
            <div key='private_account' class="my-3">
              <input name='private_account' id='private_account' type='checkbox' defaultChecked={checked} onChange={() => setChecked((state) => !state)} />
              <label htmlFor='private_account' class="ml-2">İletişim bilgileri ve yan bilgiler gizlensin</label>
            </div>
          </GrayBox>
          <GrayBox className="w-96">
            <h3 class="object-top text-center text-xl"> Diğer Bilgiler </h3>
            {Object.entries(optional_tr).map(([key, title]) => InputField({
                    'key': key,
                    'title': title,
                    'placeholder': key in current_optional_fields ? current_optional_fields[key] : "",
                    'type': (key === "identity_number") ? 'number' : (key === "date_of_birth") ? 'date' : 'text',
                    'required': (key === "username")}))}
          </GrayBox>
        </div>
        <div class="my-3 w-full text-center">
          <button type="submit" class="mx-auto w-1/2 text-white bg-blue-700 hover:bg-blue-800 focus:ring-4 focus:outline-none focus:ring-blue-300 font-medium rounded-lg text-m w-full sm:w-auto px-5 py-2.5 text-center dark:bg-blue-600 dark:hover:bg-blue-700 dark:focus:ring-blue-800">Güncelle</button>
        </div>
      </form>
    </main>
  )
}
Edit.getLayout = function getLayout(page) {
  return <MainLayout>{page}</MainLayout>;
};

export const getServerSideProps = withIronSessionSsr(
  async function getServerSideProps({ req, locale }) {

    let user;

    try {
      user = req.session.user;
    } catch (error) {
      console.log('Error @ profile/edit.js: ', error);
      return { notFound: true };
    }

    if (!user?.accessToken) {
      console.log("A guest is trying to edit");
      return { props: {guest: true} };
    }

    let current_main_fields;
    try {
      ({ data: current_main_fields } = await api.get('/api/users/me', {
        headers: {
          'Authorization': `Bearer ${user.accessToken}`
        }
      }));
    } catch (AxiosError) {
      console.log("A token expired");
      return { props: { expired: true } };
    }

    const { data: { user_optional_infos: current_optional_fields_list } } = await api.get('/api/profiles/user-optional-infos', {
      headers: {
        'Authorization': `Bearer ${user.accessToken}`
      }
    });
    const current_optional_fields = current_optional_fields_list.length > 0 ? current_optional_fields_list[0] : {}

    return {"props":
      {
        current_main_fields,
        current_optional_fields,
        accessToken: user.accessToken
      }
    };
  },
  sessionConfig
)