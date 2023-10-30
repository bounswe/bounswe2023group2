
import { Inter } from 'next/font/google';
import MainLayout from '@/layouts/MainLayout';
import MainInfo from '@/components/profile/MainInfo';
import OptionalInfo from '@/components/profile/OptionalInfo';
import ActivityTable from '@/components/ActivityTable';
import InfoList from '@/components/profile/InfoList';
import { api } from '@/lib/apiUtils';
import { withIronSessionSsr } from 'iron-session/next';
import sessionConfig from '@/lib/sessionConfig';

export default function Profile({ main_info, optional_info, list_info }) {
  // const {misc, activities} = TODO;
  const {professions, languages, "socialmedia-links": social, skills} = list_info;
  const prof_str = professions.map(prof => `${prof.profession} (${prof.profession_level})`);
  const lang_str = languages.map(lang => `${lang.language} (${lang.language_level})`);
  const skill_str = skills.map(skill => `${skill.skill_definition} (${skill.skill_level})`);
  const social_str = social.map(social => <a href={social.profile_URL}>{platform_name}</a>);
  const dictionary_tr = {
    "date_of_birth": "Doğum Tarihi",
    "nationality": "Ülke",
    "identity_number": "Kimlik No",
    "education": "Öğrenim",
    "health_condition": "Sağlık Durumu",
    "blood_type": "Kan Grubu",
    "Address": "Adres"
  }
  if ("username" in optional_info) delete optional_info["username"];
  const optional_info_tr = Object.entries(optional_info).map(([key, val]) => [dictionary_tr[key], val])
  optional_info_tr.sort();
  return (
    <>
      <main>
        <div class="flex justify-around space-x-8">
          <MainInfo className="w-60" info={main_info}/>
          <OptionalInfo className="w-96" fields={optional_info_tr} />
        </div>
        <br />
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
      return { guest: true };
    }

    const { data: main_info } = await api.get('/api/users/me', {
      headers: {
        'Authorization': `Bearer ${user.accessToken}` 
      }
    });

    const { data: { user_optional_infos: [optional_info] } } = await api.get('/api/profiles/get-user-optional-info', {
      headers: {
        'Authorization': `Bearer ${user.accessToken}` 
      }
    });

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

    console.log(list_info)

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