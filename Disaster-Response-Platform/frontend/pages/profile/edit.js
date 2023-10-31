import MainLayout from '@/layouts/MainLayout';
import GrayBox from '@/components/GrayBox';
import InputField from '@/components/InputField';
import { useRouter } from 'next/navigation';
import { api } from '@/lib/apiUtils';
import { withIronSessionSsr } from 'iron-session/next';
import sessionConfig from '@/lib/sessionConfig';

export default function Edit({ current_main_fields, current_optional_fields, accessToken }) {

  const router = useRouter();

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
      if (val === "" || val === current_main_fields[key] || val === current_optional_fields[key]) continue;
        if (["first_name", "last_name", "email", "phone_number"].includes(key)) {
          mainData[key] = val;
        } else {
          optionalData[key] = val;
        }
    }

    if (Object.keys(mainData).length > 0) {
      await api.put("/api/users/update-user", mainData, {
        headers: {
          'Authorization': `Bearer ${accessToken}`
        }
      });
    }

    if (Object.keys(optionalData).length > 0) {
      await api.post("/api/profiles/set-user-optional-info", optionalData, {
        headers: {
          'Authorization': `Bearer ${accessToken}`
        }
      });
    }

    router.push('/profile');
  }

  const main_tr = {
    "first_name": "Ad",
    "last_name": "Soyad",
    "email": "Email",
    "phone_number": "Telefon No"
  }

  const optional_tr = {
    "username": "Kullanıcı Adı",
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
        <div class="my-6 w-full text-center">
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
      return { guest: true };
    }

    const { data: current_main_fields } = await api.get('/api/users/me', {
      headers: {
        'Authorization': `Bearer ${user.accessToken}`
      }
    });

    const { data: { user_optional_infos: [current_optional_fields] } } = await api.get('/api/profiles/get-user-optional-info', {
      headers: {
        'Authorization': `Bearer ${user.accessToken}`
      }
    });

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