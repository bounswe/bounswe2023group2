
import { Inter } from 'next/font/google'

import MainLayout from '@/layouts/MainLayout'


export default function profile({ user }) {
  return (
    
      <div>
        <h1>Welcome</h1>
      </div>
  
  )
}
profile.getLayout = function getLayout(page) {
  return <MainLayout>{(page)}</MainLayout>;
};
