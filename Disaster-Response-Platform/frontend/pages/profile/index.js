import { Inter } from 'next/font/google';
import MainLayout from '@/layouts/MainLayout';
import MainInfo from '@/components/profile/MainInfo';
import OptionalInfo from '@/components/profile/OptionalInfo';
import ActivityTable from '@/components/ActivityTable';
import SkillList from '@/components/profile/SkillList';
import SkillModal from '@/components/profile/SkillModal';
import { api } from '@/lib/apiUtils';
import { withIronSessionSsr } from 'iron-session/next';
import sessionConfig from '@/lib/sessionConfig';
import { useRouter } from 'next/navigation';
import Link from 'next/link';
import { useState, useEffect } from "react";
import { useDisclosure } from "@nextui-org/react";
import { ToastContainer } from 'react-toastify';
import getLabels from '@/lib/getLabels';

export default function Profile({guest, expired, main_info, optional_info, list_info, labels }) {
  const router = useRouter();
  const { isOpen, onOpen, onOpenChange } = useDisclosure();
  const [ modalState, setModalState ] = useState({options:[]});

  if (guest || expired) {
    useEffect(() => {router.push("/login")});
    return (
      <div class="text-center text-xl">
        <br /><br /><br />
        {labels.auth.redirect_to_login}
      </div>
    );
  }

  const username = main_info.username;
  const {professions, languages, "socialmedia-links": social, skills} = list_info;
  const optional_info_tr = Object.entries(optional_info).map(([key, val]) => [labels.profile[key], val]);
  optional_info_tr.sort();
  return (
    <>
      <main>
        <div class="flex justify-around space-x-8">
          <MainInfo className="w-60" info={main_info} contact={true} labels={labels} />
          <OptionalInfo className="w-80" fields={optional_info_tr} labels={labels} />
          <div>
            <SkillList list={social.list} topic={social.topic} username={username} onOpen={onOpen} setModalState={setModalState} labels={labels} />
            <SkillList list={skills.list} topic={skills.topic} username={username} onOpen={onOpen} setModalState={setModalState} labels={labels} />
            <SkillList list={languages.list} topic={languages.topic} username={username} onOpen={onOpen} setModalState={setModalState} labels={labels} />
            <SkillList list={professions.list} topic={professions.topic} username={username} onOpen={onOpen} setModalState={setModalState} labels={labels} />
          </div>
        </div>
        <div class="my-10 w-full text-center">
          <Link href='/profile/edit'>
            <button type="submit" class="mx-auto w-1/2 text-white bg-blue-700 hover:bg-blue-800 focus:ring-4 focus:outline-none focus:ring-blue-300 font-medium rounded-lg text-m w-full sm:w-auto px-5 py-2.5 text-center dark:bg-blue-600 dark:hover:bg-blue-700 dark:focus:ring-blue-800">{labels.UI.edit}</button>
          </Link>
        </div>
        <ActivityTable labels={labels} />
        <SkillModal isOpen={isOpen} onOpenChange={onOpenChange} topic={modalState} labels={labels} />
        <ToastContainer />
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

    const labels = await getLabels(req.session.language);

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
    delete optional_info.username;

    let list_info = {}

    const topics = [
      {"api_url": "professions", "key": "user_professions",
        "title": "Meslekler", "primary": "profession", "secondary": "profession_level", "is_link": false,
        "post": "/add-profession", "delete": "",
        "options": ["amateur", "pro", "certified pro"]},
      {"api_url": "languages", "key": "user_languages",
        "title": "Diller", "primary": "language", "secondary": "language_level", "is_link": false,
        "post": "/add-language", "delete": "/delete-language",
        "options": ["beginner", "intermediate", "advanced", "native"]},
      {"api_url": "socialmedia-links", "key": "user_socialmedia_links",
        "title": "Sosyal Medya", "primary": "platform_name", "secondary": "profile_URL", "is_link": true,
        "post": "/add-socialmedia-link", "delete": ""},
      {"api_url": "skills", "key": "user_skills",
        "title": "Yetenekler", "primary": "skill_definition", "secondary": "skill_level", "is_link": false,
        "post": "/add-skill", "delete": "",
        "options": ["beginner", "basic", "intermediate", "skilled", "expert"]},
    ]

    for (let topic of topics) {
      const { data: { [topic.key]: fields } } = await api.get(`/api/profiles/${topic.api_url}`, {
        headers: {
          'Authorization': `Bearer ${user.accessToken}` 
        }
      });
      list_info[topic.api_url] = {"list": fields ? fields : [], "topic": topic};
    }

    return {
      props: {
        main_info,
        optional_info,
        list_info,
        labels
      },
    };
  },
  sessionConfig
)