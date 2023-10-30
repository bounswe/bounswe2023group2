import Image from 'next/image';
import GrayBox from '../GrayBox';

export default function MainInfo({ className, info }) {
	const {username, first_name, last_name, email, phone_number} = info;
	const name = `${first_name} ${last_name}`;
	const img = null; // TODO
	return (
		<GrayBox className={className}>
			<Image class="block mx-auto" src={img ? img : "/images/default_pp.jpg"} width={135} height={135} alt={"Profile picture of" + name}/>
			<div class="text-center">
				<span class="text-lg lg:text-xl">{name}</span>
				{email ? <> <br /> <span class="text-md lg:text-lg"> {email} </span> </> : null}
				{phone_number ? <> <br /> <span class="text-md lg:text-lg"> {phone_number} </span> </> : null}
			</div>
		</GrayBox>
	)
}