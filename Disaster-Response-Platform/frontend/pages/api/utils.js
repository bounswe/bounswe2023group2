import { withIronSession } from 'iron-session';
const hostType = process.env.NEXT_PUBLIC_HOST_TYPE;

export default function withSession(handler) {
  return withIronSession(handler, {
    cookieOptions: {
      // the next line allows to use the session in non-https environments like
      // Next.js dev mode (http://localhost:3000)
      maxAge: 60 * 60 * 24 * 30, // 30 days
      secure: hostType === 'staging' || hostType === 'local' || hostType === 'dev' ? false : true,
    },
  });
}