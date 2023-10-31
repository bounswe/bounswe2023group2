export default function InputField({ key, title, placeholder, type, required=false }) {
    return (
        <div key={key} class="my-2">
            <label htmlFor={key} class="block mb-1 text-m font-medium text-gray-900 dark:text-black">{title}</label>
            <input name={key} id={key} type={type} class="bg-gray-50 border border-gray-300 text-gray-900 text-m rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full py-1.5 px-2.5 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-blue-500 dark:focus:border-blue-500" defaultValue={placeholder} required={required} />
        </div>
    );
}