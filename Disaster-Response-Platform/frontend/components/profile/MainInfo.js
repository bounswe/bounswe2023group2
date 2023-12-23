import Image from 'next/image';
import GrayBox from '../GrayBox';
import { Button, Divider } from "@nextui-org/react";

export default function MainInfo({ className, info, report, onOpen, contact=true, img, labels, onOpenDelete }) {
	let {username, first_name, last_name, email, phone_number, user_role, proficiency} = info;
	const name = `${first_name} ${last_name}`;
	if (user_role === null) user_role = "GUEST";
	return (
		<GrayBox className={className}>
			<Image className="block mx-auto my-3" src={img ? img : "/images/default_pp.jpg"} width={135} height={135} alt={"Profile picture of" + name}/>
			<div className="text-center">
				<span className="text-md lg:text-lg">{username}</span>
				<br /> <span className="text-md lg:text-lg">{labels.user_roles[user_role]}</span>
				{proficiency?.proficiency ? (
					<> <br /> <span className="text-md lg:text-lg"> {labels.proficiency[proficiency.proficiency]} </span> </>
				) : null}
				<Divider className="my-6" />
				<h3 class="object-top text-center text-xl mb-2"> {labels.profile_pages.contact_info} </h3>
				{contact ? (
					<>
						<span className="text-lg lg:text-xl">{name}</span>
						{email ? <> <br /> <span class="text-md lg:text-lg"> {email} </span> </> : null}
						{phone_number ? <> <br /> <span class="text-md lg:text-lg"> {phone_number} </span> </> : null}
					</>
				) : (
					<span className="text-gray-600 text-sm lg:text-md"> {labels.profile_pages.contact_hidden} </span>
				)}
			</div>
			{report ? <Button onPress={onOpen} className="mx-auto my-3 block text-white bg-red-600 hover:bg-red-700 focus:ring-4 focus:outline-none focus:ring-red-200 rounded-lg px-5 py-2.5 text-center dark:bg-red-500 dark:hover:bg-red-600 dark:focus:ring-red-700">{labels.admin.report}</Button> : null}
			{onOpenDelete ? <Button onPress={onOpenDelete} className="mx-auto my-3 block text-white bg-red-600 hover:bg-red-700 focus:ring-4 focus:outline-none focus:ring-red-200 rounded-lg px-5 py-2.5 text-center dark:bg-red-500 dark:hover:bg-red-600 dark:focus:ring-red-700">{labels.profile_pages.delete_account}</Button> : null}
		</GrayBox>
	)
}