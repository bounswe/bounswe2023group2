import { withIronSessionSsr } from 'iron-session/next';
import sessionConfig from '@/lib/sessionConfig';
import MainLayout from '@/layouts/MainLayout';
import MainInfo from '@/components/profile/MainInfo';
import OptionalInfo from '@/components/profile/OptionalInfo';
import ActivityTable from '@/components/ActivityTable';
import SkillList from '@/components/profile/SkillList';
import { useRouter } from 'next/router';
import { useEffect } from "react";
import { api } from '@/lib/apiUtils';
import { useDisclosure } from "@nextui-org/react";
import ReportModal from '@/components/profile/ReportModal';
import { Button } from "@nextui-org/react";
import { ToastContainer } from 'react-toastify';

export default function OtherProfile({ unauthorized, admin, main_info, optional_info, list_info }) {
  const router = useRouter();
  const { isOpen, onOpen, onOpenChange } = useDisclosure();

  if (unauthorized) {
    useEffect(() => {router.push("/login")});
    return (
      <div class="text-center text-xl">
        <br /><br /><br />
        Oturum açmaya yönlendiriliyorsunuz...
      </div>
    );
  }

  const visibilityEnum = {
    MINIMAL: 0,
    CONTACT: 1,
    ALL: 2
  }

  let visibility = (admin
                    ? visibilityEnum.ALL
                    : main_info.private_account
                      ? visibilityEnum.MINIMAL
                      : visibilityEnum.CONTACT);


  const username = router.query.username;
  const {professions, languages, "socialmedia-links": social, skills} = list_info;
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
  const optional_info_tr = Object.entries(optional_info).map(([key, val]) => [dictionary_tr[key], val]);
  optional_info_tr.sort();
  return (
  <>
    <main>
      <div class="flex justify-around space-x-8">
        <MainInfo className="w-60" info={main_info} onOpen={onOpen} contact={visibility >= visibilityEnum.CONTACT} report/>
        {visibility >= visibilityEnum.ALL
          ? <OptionalInfo className="w-80" fields={optional_info_tr} />
          : null
        }
        <div>
          <SkillList list={social.list} topic={social.topic} username={username} noedit/>
          <SkillList list={skills.list} topic={skills.topic} username={username} noedit/>
          <SkillList list={languages.list} topic={languages.topic} username={username} noedit/>
          <SkillList list={professions.list} topic={professions.topic} username={username} noedit/>
        </div>
      </div>
      <ActivityTable />
      <ReportModal isOpen={isOpen} onOpenChange={onOpenChange} reported={username}/>
      <ToastContainer />
    </main>
  </>
  )
};
OtherProfile.getLayout = function getLayout(page) {
  return <MainLayout>{page}</MainLayout>;
};

export const getServerSideProps = withIronSessionSsr(
  async function getServerSideProps({ req, locale, params }) {
    let self;

    try {
      self = req.session.user;
    } catch (error) {
      console.log('Error @ user/[username].js: ', error);
      return { notFound: true };
    }

    if (!self?.accessToken) {
      console.log("A guest is trying to view a profile");
      return { props: { unauthorized: true } };
    }

    let main_info;
    try {
    ({ data: main_info } = await api.get(`/api/users/${params.username}`, {
      headers: {
        'Authorization': `Bearer ${self.accessToken}` 
      }
    }));
    } catch (AxiosError) {
      console.log("A token expired");
      return { props: { unauthorized: true } };
    }

    const { data: { user_optional_infos: optional_info_list } } = await api.get('/api/profiles/user-optional-infos', {
      params: {
        'anyuser': params.username
      },
      headers: {
        'Authorization': `Bearer ${self.accessToken}`
      }
    });
    const optional_info = optional_info_list.length > 0 ? optional_info_list[0] : {};
    delete optional_info.username;

    let list_info = {}

    const topics = [
      {"api_url": "professions", "key": "user_professions",
        "title": "Meslekler", "primary": "profession", "secondary": "profession_level", "is_link": false,
        "add_title": "Meslek Ekle", "primary_label": "Meslek", "secondary_label": "Seviye",
        "post": "/add-profession", "delete": "",
        "options": ["amateur", "pro", "certified pro"]},
      {"api_url": "languages", "key": "user_languages",
        "title": "Diller", "primary": "language", "secondary": "language_level", "is_link": false,
        "add_title": "Dil Ekle", "primary_label": "Dil", "secondary_label": "Seviye",
        "post": "/add-language", "delete": "/delete-language",
        "options": ["beginner", "intermediate", "advanced", "native"]},
      {"api_url": "socialmedia-links", "key": "user_socialmedia_links",
        "title": "Sosyal Medya", "primary": "platform_name", "secondary": "profile_URL", "is_link": true,
        "add_title": "Sosyal Medya Hesabı Ekle", "primary_label": "Site ismi", "secondary_label": "Profil linki",
        "post": "/add-socialmedia-link", "delete": ""},
      {"api_url": "skills", "key": "user_skills",
        "title": "Yetenekler", "primary": "skill_definition", "secondary": "skill_level", "is_link": false,
        "add_title": "Yetenek Ekle", "primary_label": "Yetenek tanımı", "secondary_label": "Seviye",
        "post": "/add-skill", "delete": "",
        "options": ["beginner", "basic", "intermediate", "skilled", "expert"]},
    ]

    for (let topic of topics) {
      const { data: { [topic.key]: fields } } = await api.get(`/api/profiles/${topic.api_url}`, {
        params: {
          'anyuser': params.username
        },
        headers: {
            'Authorization': `Bearer ${self.accessToken}` 
          }
      });
      list_info[topic.api_url] = {"list": fields ? fields : [], "topic": topic};
    }

    const admin = true;

    return {
      props: {
        main_info,
        optional_info,
        list_info,
        admin
      },
    };
  },
  sessionConfig
)