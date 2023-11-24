import { withIronSessionSsr } from 'iron-session/next';
import sessionConfig from '@/lib/sessionConfig';
import MainLayout from '@/layouts/MainLayout';
import { useRouter } from 'next/router';

export default function OtherProfile({guest, expired, main_info, optional_info, list_info }) {
	const router = useRouter();
	const username = router.query.user;
	return <p> You're viewing the profile of {router.query.username}! </p>;
};
OtherProfile.getLayout = function getLayout(page) {
  return <MainLayout>{page}</MainLayout>;
};

export const getServerSideProps = withIronSessionSsr(
  async function getServerSideProps({ req, locale }) {
  	return {"props": {

  	}};
  },
  sessionConfig
)