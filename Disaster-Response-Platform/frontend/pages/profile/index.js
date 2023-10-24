
import { Inter } from 'next/font/google'

import MainLayout from '@/layouts/MainLayout'
import { withSessionSsr } from '../../path/to/sessionMiddleware'; // Import your session middleware


export default function profile({ user }) {
  return (
    
      <div>
        <h1>Welcome, {user.username}</h1>
      </div>
  
  )
}
profile.getLayout = function getLayout(page) {
  return <MainLayout>{withSessionSsr(page)}</MainLayout>;
};
