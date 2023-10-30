
import MainLayout from '@/layouts/MainLayout'
import { api } from '@/lib/apiUtils'

export default function login() {
  async function onSubmit(event) {
    event.preventDefault()
    const form = new FormData(event.target)
    const formData = Object.fromEntries(form.entries())

    const { data } = await fetch('/api/login', { method: 'POST', body: formData });
  }

  return (
    <form class="mb-6 w-full" onSubmit={onSubmit}>
      <div class="mb-6">
        <label for="username" class="block mb-1 text-m font-medium text-gray-900 dark:text-black">username</label>
        <input type="username" id="username" class="bg-gray-50 border border-gray-300 text-gray-900 text-m rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-blue-500 dark:focus:border-blue-500" placeholder="username" required />
      </div>
      <div class="mb-6">
        <label for="password" class="block mb-1 text-m font-medium text-gray-900 dark:text-black">password</label>
        <input type="password" id="password" class="bg-gray-50 border border-gray-300 text-gray-900 text-m rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-blue-500 dark:focus:border-blue-500" required />
      </div>
      <button type="submit" class="text-white bg-blue-700 hover:bg-blue-800 focus:ring-4 focus:outline-none focus:ring-blue-300 font-medium rounded-lg text-m w-full sm:w-auto px-5 py-2.5 text-center dark:bg-blue-600 dark:hover:bg-blue-700 dark:focus:ring-blue-800">Submit</button>
    </form>
  )

}
login.getLayout = function getLayout(page) {
  return <MainLayout>{page}</MainLayout>;
};
