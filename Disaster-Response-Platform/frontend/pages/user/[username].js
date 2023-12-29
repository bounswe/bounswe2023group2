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
import getLabels from '@/lib/getLabels';

export default function OtherProfile({ unauthorized, self_role, main_info, optional_info, list_info, labels }) {
  const router = useRouter();
  const { isOpen, onOpen, onOpenChange } = useDisclosure();

  if (unauthorized) {
    useEffect(() => {router.push("/login")});
    return (
      <div class="text-center text-xl">
        <br /><br /><br />
        {labels.auth.redirect_to_login}
      </div>
    );
  }

  const visibilityEnum = {
    PRIVATE: 0,
    DEFAULT: 1,
    ADMIN: 2
  }

  let visibility = (self_role === "ADMIN"
                    ? visibilityEnum.ADMIN
                    : main_info.private_account
                      ? visibilityEnum.PRIVATE
                      : visibilityEnum.DEFAULT);


  const username = router.query.username;
  const {professions, languages, "socialmedia-links": social, skills} = list_info;
  const optional_info_labels = Object.entries(optional_info)
                                     .filter(([key, val]) => key !== "profile_picture")
                                     .map(([key, val]) => [labels.profile[key], val]);
  optional_info_labels.sort();
  return (
  <>
    <main>
      <div class="flex justify-around space-x-8">
        <MainInfo className={visibility >= visibilityEnum.ADMIN ? "w-60" : "w-64"} info={main_info} img={optional_info.profile_picture} onOpen={onOpen} contact={visibility >= visibilityEnum.DEFAULT} labels={labels} report/>
        {visibility >= visibilityEnum.ADMIN
          ? <OptionalInfo className="w-80" fields={optional_info_labels} labels={labels} />
          : null
        }
        {visibility >= visibilityEnum.DEFAULT
          ? (
            <div>
              <SkillList list={social.list} topic={social.topic} username={username} wide={visibility < visibilityEnum.ADMIN} labels={labels} noedit/>
              <SkillList list={skills.list} topic={skills.topic} username={username} wide={visibility < visibilityEnum.ADMIN} labels={labels} noedit/>
              <SkillList list={languages.list} topic={languages.topic} username={username} wide={visibility < visibilityEnum.ADMIN} labels={labels} noedit/>
              <SkillList list={professions.list} topic={professions.topic} username={username} wide={visibility < visibilityEnum.ADMIN} labels={labels} noedit/>
            </div>
          )
          : null
        }
      </div>
      <ActivityTable labels={labels} userFilter={username} />
      <ReportModal isOpen={isOpen} onOpenChange={onOpenChange} reported={username} reported_type="users" labels={labels}/>
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

    const labels = await getLabels(req.session.language);

    try {
      self = req.session.user;
    } catch (error) {
      console.log('Error @ user/[username].js: ', error);
      return { notFound: true };
    }

    if (!self?.accessToken) {
      console.log("A guest is trying to view a profile");
      return { props: { unauthorized: true, labels } };
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
      return { props: { expired: true, labels } };
    }

    let self_info;
    try {
      ({ data: self_info } = await api.get('/api/users/me', {
        headers: {
          'Authorization': `Bearer ${self.accessToken}` 
        }
      }));
    } catch (AxiosError) {
      console.log("A token expired");
      return { props: { expired: true, labels } };
    }

    const self_role = self_info.user_role;

    let optional_info_list = [];
    if (self_role === "ADMIN") {
      ({ data: { user_optional_infos: optional_info_list } } = await api.get('/api/profiles/user-optional-infos', {
        params: {
          'anyuser': params.username
        },
        headers: {
          'Authorization': `Bearer ${self.accessToken}`
        }
      }));
    }
    const optional_info = optional_info_list.length > 0 ? optional_info_list[0] : {};
    delete optional_info.username;

    let list_info = {}

    const topics = [
      {"api_url": "professions", "key": "user_professions",
        "title": "Meslekler", "primary": "profession", "secondary": "profession_level", "is_link": false,
        "post": "professions/add-profession", "delete": "delete-professions",
        "options": ["amateur", "pro", "certified pro"]},
      {"api_url": "languages", "key": "user_languages",
        "title": "Diller", "primary": "language", "secondary": "language_level", "is_link": false,
        "post": "languages/add-language", "delete": "languages/delete-language",
        "options": ["beginner", "intermediate", "advanced", "native"]},
      {"api_url": "socialmedia-links", "key": "user_socialmedia_links",
        "title": "Sosyal Medya", "primary": "platform_name", "secondary": "profile_URL", "is_link": true,
        "post": "socialmedia-links/add-socialmedia-link", "delete": "delete-socialmedia-links"},
      {"api_url": "skills", "key": "user_skills",
        "title": "Yetenekler", "primary": "skill_definition", "secondary": "skill_level", certificate: "skill_document", "is_link": false,
        "post": "skills/add-skill", "delete": "delete-skill",
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

    return {
      props: {
        main_info,
        optional_info,
        list_info,
        self_role,
        labels
      },
    };
  },
  sessionConfig
)