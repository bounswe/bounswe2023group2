
import MainLayout from '@/layouts/MainLayout'
import { api } from '@/lib/apiUtils'
import { useRouter } from 'next/navigation';

export default function login() {
    const router = useRouter();
  async function onSubmit(event) {
    event.preventDefault();
    const form = new FormData(event.target);
    const formData = Object.fromEntries(form);

    const response = await fetch('/api/login', { method: 'POST', body: JSON.stringify(formData) });
    if (response.status === 200) {
      router.push('/profile');
    }
  }

  return (
    <form class="mb-6 w-full" onSubmit={onSubmit}>
      <div class="mb-6">
        <label htmlFor="username" class="block mb-1 text-m font-medium text-gray-900 dark:text-black">username</label>
        <input type="text" id="username" name="username" class="bg-gray-50 border border-gray-300 text-gray-900 text-m rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-blue-500 dark:focus:border-blue-500" placeholder="username" required />
      </div>
      <div class="mb-6">
        <label htmlFor="password" class="block mb-1 text-m font-medium text-gray-900 dark:text-black">password</label>
        <input type="password" id="password" name="password" class="bg-gray-50 border border-gray-300 text-gray-900 text-m rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-blue-500 dark:focus:border-blue-500" required />
      </div>
      <button type="submit" class="text-white bg-blue-700 hover:bg-blue-800 focus:ring-4 focus:outline-none focus:ring-blue-300 font-medium rounded-lg text-m w-full sm:w-auto px-5 py-2.5 text-center dark:bg-blue-600 dark:hover:bg-blue-700 dark:focus:ring-blue-800">Submit</button>
    </form>
  )

}
login.getLayout = function getLayout(page) {
  return <MainLayout>{page}</MainLayout>;
};
