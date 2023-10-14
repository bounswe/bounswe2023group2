import Head from 'next/head'
import Image from 'next/image'
import { Inter } from 'next/font/google'
import styles from '@/styles/Home.module.css'
import MainLayout from '@/layouts/MainLayout'

const inter = Inter({ subsets: ['latin'] })

export default function home() {
  return (
    <>
      <main >
        <div >
          Welcome to DaRP
        </div>
      </main>
    </>
  )
}
home.getLayout = function getLayout(page) {
  return <MainLayout>{page}</MainLayout>;
};
