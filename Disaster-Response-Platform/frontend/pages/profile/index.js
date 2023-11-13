
import { Inter } from 'next/font/google';
import MainLayout from '@/layouts/MainLayout';
import MainInfo from '@/components/profile/MainInfo';
import OptionalInfo from '@/components/profile/OptionalInfo';
import ActivityTable from '@/components/ActivityTable';
import SkillList from '@/components/profile/SkillList';
import GrayBox from '@/components/GrayBox';
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
  const {professions, languages, user_socialmedia_links: social, skills} = list_info;
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
          <GrayBox class="w=36">
            <SkillList list={social.list} topic={social.topic}/>
            <SkillList list={skills.list} topic={skills.topic} />
            <SkillList list={languages.list} topic={languages.topic} />
            <SkillList list={professions.list} topic={professions.topic} />
          </GrayBox>
        </div>
        <div class="my-10 w-full text-center">
          <Link href='/profile/edit'>
            <button type="submit" class="mx-auto w-1/2 text-white bg-blue-700 hover:bg-blue-800 focus:ring-4 focus:outline-none focus:ring-blue-300 font-medium rounded-lg text-m w-full sm:w-auto px-5 py-2.5 text-center dark:bg-blue-600 dark:hover:bg-blue-700 dark:focus:ring-blue-800">Düzenle</button>
          </Link>
        </div>
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

    const { data: { user_optional_infos: optional_info_list } } = await api.get('/api/profiles/user-optional-infos', {
      headers: {
        'Authorization': `Bearer ${user.accessToken}` 
      }
    });
    const optional_info = optional_info_list.length > 0 ? optional_info_list[0] : {};

    let list_info = {}

    const topics = [
      {"api_url": "professions", "key": "professions",
        "title": "Meslekler", "primary": "profession", "secondary": "profession_level", "is_link": false,
        "post": "/add-profession", "delete": ""},
      {"api_url": "languages", "key": "languages",
        "title": "Diller", "primary": "language", "secondary": "language_level", "is_link": false,
        "post": "/add-language", "delete": "/delete-language"},
      {"api_url": "socialmedia-links", "key": "user_socialmedia_links",
        "title": "Sosyal Medya", "text": "platform_name", "url": "profile_URL", "is_link": true,
        "post": "/add-social-media-link", "delete": ""},
      {"api_url": "skills", "key": "skills",
        "title": "Yetenekler", "primary": "skill_definition", "secondary": "skill_level", "is_link": false,
        "post": "/add-skill", "delete": ""},
    ]

    for (let topic of topics) {
      const { data: { [topic.key]: fields } } = await api.get(`/api/profiles/${topic.api_url}`, {
        headers: {
          'Authorization': `Bearer ${user.accessToken}` 
        }
      });
      list_info[topic.key] = {"list": fields ? fields : [], "topic": topic};
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