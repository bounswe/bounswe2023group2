
import MainLayout from '@/layouts/MainLayout'

export default function login() {
  async function onSubmit(event) {
    event.preventDefault()
 
    const formData = new FormData(event.target)
    console.log(formData)
    // const response = await fetch('l/api/submit', {
    //   method: 'POST',
    //   body: formData,
    // })
 
    // Handle response if necessary
    // const data = await response.json()
    // ...
  }
 
  return (
    <form onSubmit={onSubmit}>
      <input type="text" name="name" />
      <input type="text" name="password" />
      <button type="submit">Submit</button>
    </form>
  )

}
login.getLayout = function getLayout(page) {
  return <MainLayout>{page}</MainLayout>;
};
