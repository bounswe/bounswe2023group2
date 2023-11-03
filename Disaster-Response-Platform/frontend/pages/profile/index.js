
import { Inter } from 'next/font/google';
import MainLayout from '@/layouts/MainLayout';
import MainInfo from '@/components/profile/MainInfo';
import OptionalInfo from '@/components/profile/OptionalInfo';
import ActivityTable from '@/components/ActivityTable';
import InfoList from '@/components/profile/InfoList';
import { api } from '@/lib/apiUtils';
import { withIronSessionSsr } from 'iron-session/next';
import sessionConfig from '@/lib/sessionConfig';
import { useRouter } from 'next/navigation'
import Link from 'next/link'
import { useEffect } from "react";

export default function Profile({guest, expired, main_info, optional_info, list_info }) {
  const router = useRouter();
  if (guest || expired) {
    useEffect(() => router.push("/login"));
    return (
      <div class="text-center text-xl">
        <br /><br /><br />
        Oturum açmaya yönlendiriliyorsunuz...
      </div>
    );
  }

  // const {misc, activities} = TODO;
  const {professions, languages, "socialmedia-links": social, skills} = list_info;
  const prof_str = professions.map(prof => `${prof.profession} (${prof.profession_level})`);
  const lang_str = languages.map(lang => `${lang.language} (${lang.language_level})`);
  const skill_str = skills.map(skill => `${skill.skill_definition} (${skill.skill_level})`);
  const social_str = social.map(social => <a href={social.profile_URL}>{platform_name}</a>);
  const dictionary_tr = {
    "username": "Kullanıcı Adı",
    "date_of_birth": "Doğum Tarihi",
    "nationality": "Ülke",
    "identity_number": "Kimlik No",
    "education": "Öğrenim",
    "health_condition": "Sağlık Durumu",
    "blood_type": "Kan Grubu",
    "Address": "Adres"
  }
  const optional_info_tr = Object.entries(optional_info).map(([key, val]) => [dictionary_tr[key], val])
  optional_info_tr.sort();
  return (
    <>
      <main>
        <div class="flex justify-around space-x-8">
          <MainInfo className="w-60" info={main_info}/>
          <OptionalInfo className="w-96" fields={optional_info_tr} />
        </div>
        <div class="my-10 w-full text-center">
          <Link href='/profile/edit'>
            <button type="submit" class="mx-auto w-1/2 text-white bg-blue-700 hover:bg-blue-800 focus:ring-4 focus:outline-none focus:ring-blue-300 font-medium rounded-lg text-m w-full sm:w-auto px-5 py-2.5 text-center dark:bg-blue-600 dark:hover:bg-blue-700 dark:focus:ring-blue-800">Düzenle</button>
          </Link>
        </div>
        <InfoList list={social_str} />
        <InfoList list={skill_str} />
        <InfoList list={lang_str} />
        <InfoList list={prof_str} />
        <ActivityTable />
      </main>
    </>
  )
}
Profile.getLayout = function getLayout(page) {
  return <MainLayout>{page}</MainLayout>;
};

export const getServerSideProps = withIronSessionSsr(
  async function getServerSideProps({ req, locale }) {

    let user;

    try {
      user = req.session.user;
    } catch (error) {
      console.log('Error @ profile/index.js: ', error);
      return { notFound: true };
    }

    if (!user?.accessToken) {
      console.log("A guest is trying to view own profile");
      return { props: { guest: true } };
    }

    let main_info;
    try {
      ({ data: main_info } = await api.get('/api/users/me', {
        headers: {
          'Authorization': `Bearer ${user.accessToken}` 
        }
      }));
    } catch (AxiosError) {
      console.log("A token expired");
      return { props: { expired: true } };
    }

    const { data: { user_optional_infos: optional_info_list } } = await api.get('/api/profiles/get-user-optional-info', {
      headers: {
        'Authorization': `Bearer ${user.accessToken}` 
      }
    });
    const optional_info = optional_info_list.length > 0 ? optional_info_list[0] : {};

    let list_info = {}

    for (let topic of ["professions", "languages", "socialmedia-links", "skills"]) {
      let key = `user_${topic.replaceAll("-", "_")}`;
      const { data: { [key]: fields } } = await api.get(`/api/profiles/${topic}`, {
        headers: {
          'Authorization': `Bearer ${user.accessToken}` 
        }
      });
      list_info[topic] = fields;
    }

    return {
      props: {
        main_info,
        optional_info,
        list_info
      },
    };
  },
  sessionConfig
)