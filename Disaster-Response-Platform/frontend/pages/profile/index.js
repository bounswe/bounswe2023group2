
import { Inter } from 'next/font/google';
import MainLayout from '@/layouts/MainLayout';
// import { withSessionSsr } from '../../path/to/sessionMiddleware'; // Import your session middleware
import MainInfo from '@/components/profile/MainInfo';
import OptionalInfo from '@/components/profile/OptionalInfo';
import ActivityTable from '@/components/ActivityTable';
import InfoList from '@/components/profile/InfoList';


export default function profile({ user }) {
  const {misc, main_info, optional_info, list_info, activities} = user;
  const {social, skills, languages, professions} = list_info;
  return (
    <>
      <main>
        <div class="flex justify-around space-x-8">
          <MainInfo className="w-60" info={main_info}/>
          <OptionalInfo className="w-96" fields={optional_info} />
        </div>
        <br />
        <InfoList list={social} />
        <InfoList list={skills} />
        <InfoList list={languages} />
        <InfoList list={professions} />
        <ActivityTable />
      </main>
    </>
  )
}
profile.getLayout = function getLayout(page) {
  return <MainLayout>{page}</MainLayout>;
};

export function getServerSideProps(context) {
  return {
    props: {
      "user": {
        "misc": {

        },
        "main_info": {
          "username": "user3",
          "name": "Sample User",
          "phone": "05555555555",
          "email": "sample-user@darp.com"
        },
        "optional_info": [
          {"title": "Date of Birth", "content": "31.09.2000"},
          {"title": "Nationality", "content": "Turkey"},
          {"title": "Blood Type", "content": "0+"},
          {"title": "Address", "content": "really really really long address to test css stuff and overflows and height imbalances"}
        ],
        "list_info": {
          "social": "social media links go here",
          "skills": "skills with certifications go here",
          "languages": "languages go here",
          "professions": "professions go here"
        }
      }
    },
  };
}