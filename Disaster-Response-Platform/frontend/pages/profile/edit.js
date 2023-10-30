import MainLayout from '@/layouts/MainLayout';
import GrayBox from '@/components/GrayBox';
import InputField from '@/components/InputField';
import { useRouter } from 'next/navigation';
import { api } from '@/lib/apiUtils';

export default function Edit({ current_fields }) {
  const router = useRouter();
  async function onSubmit(event) {
    event.preventDefault();
    const form = new FormData(event.target);
    const formData = Object.fromEntries(form.entries());
    console.log(form);
    for ([key, val] in Object.entries(formData)) {
      if (val === "") {
        //await api.delete("/profiles/delete-user-optional-info");
        delete formData["key"];
      }
    }
    //await api.post("/profiles/set-user-optional-info", { body: formData });
    router.push('/profile');


  }

  // key, title, required
  const all_fields = [
    ["username", "Username", true],
    ["date_of_birth", "Date of birth", false],
    ["nationality", "Nationality", false],
    ["identity_number", "ID number",false],
    ["education", "Education", false],
    ["health_condition", "Health Condition", false],
    ["blood_type", "Blood Type", false],
    ["Address", "Address", false]
  ];

  return (
    <main>
      <form onSubmit={onSubmit}>
        <GrayBox className="w-96">
          <h3 class="object-top text-center text-xl"> Change Info </h3>
          {all_fields.map(field => InputField({
                  'key': field[0],
                  'title': field[1],
                  'placeholder': field[0] in current_fields ? current_fields[field[0]] : "",
                  'required': field[2]}))}
          <div class="my-6 w-full text-center">
            <button type="submit" class="mx-auto w-1/2 text-white bg-blue-700 hover:bg-blue-800 focus:ring-4 focus:outline-none focus:ring-blue-300 font-medium rounded-lg text-m w-full sm:w-auto px-5 py-2.5 text-center dark:bg-blue-600 dark:hover:bg-blue-700 dark:focus:ring-blue-800">Submit</button>
          </div>
        </GrayBox>
      </form>
    </main>
  )
}
Edit.getLayout = function getLayout(page) {
  return <MainLayout>{page}</MainLayout>;
};

export async function getServerSideProps() {
  //const current_fields = await api.get("/profiles/all-user-optional-infos");
  return {"props":
    {
      "current_fields": {
        "username": "cbora",
        "blood_type": "0 Rh+",
        "health_condition": "Healthy",
        "nationality": "Turkey"
      }
    }
  };
}