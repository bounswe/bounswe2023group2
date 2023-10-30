import InfoField from '../InfoField';
import GrayBox from '../GrayBox';
import { redirect } from 'next/navigation'
import Link from 'next/link'

export default function OptionalInfo({ className, fields }) {
	console.log(`fields: ${fields}`)
	return (
		<GrayBox className={className}>
			<h3 class="object-top text-center text-xl"> Bilgiler </h3>
			{fields.map(InfoField)}
          	<div class="mt-3 w-full text-center">
				<Link href='/profile/edit'>
					<button type="submit" class="mx-auto w-1/2 text-white bg-blue-700 hover:bg-blue-800 focus:ring-4 focus:outline-none focus:ring-blue-300 font-medium rounded-lg text-m w-full sm:w-auto px-5 py-2.5 text-center dark:bg-blue-600 dark:hover:bg-blue-700 dark:focus:ring-blue-800">Düzenle</button>
				</Link>
			</div>
		</GrayBox>
	);
}