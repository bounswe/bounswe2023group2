import Image from 'next/image';
import GrayBox from '../GrayBox';
import { Button } from "@nextui-org/react";

export default function MainInfo({ className, info, report, onOpen, contact }) {
	const {username, first_name, last_name, email, phone_number} = info;
	const name = `${first_name} ${last_name}`;
	const img = null; // TODO
	return (
		<GrayBox className={className}>
			<Image class="block mx-auto" src={img ? img : "/images/default_pp.jpg"} width={135} height={135} alt={"Profile picture of" + name}/>
			<div class="text-center">
				<span class="text-m lg:text-lg">{username}</span>
				{contact ? (
					<>
						<br /> <span class="text-lg lg:text-xl">{name}</span>
						{email ? <> <br /> <span class="text-md lg:text-lg"> {email} </span> </> : null}
						{phone_number ? <> <br /> <span class="text-md lg:text-lg"> {phone_number} </span> </> : null}
					</>
				) : null}
			</div>
			{report ? <Button onPress={onOpen} className="mx-auto block text-white bg-red-600 hover:bg-red-700 focus:ring-4 focus:outline-none focus:ring-red-200 rounded-lg px-5 py-2.5 text-center dark:bg-red-500 dark:hover:bg-red-600 dark:focus:ring-red-700">Raporla</Button> : null}
		</GrayBox>
	)
}