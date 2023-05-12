
export default function dummyHome() {
  return (
    <main className="flex min-h-screen flex-col items-center justify-between p-24">
      <div> You are entered the system ! </div>
      <button type='submit' className="bg-red-500 hover:bg-blue-400 text-white font-bold py-2 px-4 border-b-4 border-blue-700 hover:border-blue-500 rounded">
        <a href="http://localhost:3000/login"> Logout </a>    
        </button>
    </main>
  )
}
